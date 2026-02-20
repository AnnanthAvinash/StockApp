package avinash.server.data

import avinash.server.config.ServerConfig
import avinash.server.models.*
import kotlinx.serialization.Serializable
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Serializable
data class HomeCategories(
    val topGainers: List<Stock>,
    val topLosers: List<Stock>,
    val mostBought: List<Stock>,
    val mostSold: List<Stock>
)

class StockManager(private val webSocketManager: WebSocketManager) {
    
    // Stocks Map: symbol -> Stock
    private val stocks = ConcurrentHashMap<String, Stock>()
    
    // Portfolios Map: userId -> Portfolio
    private val portfolios = ConcurrentHashMap<String, Portfolio>()
    
    // Pending Orders Map: orderId -> PendingOrder
    private val pendingOrders = ConcurrentHashMap<String, PendingOrder>()
    
    // Wallet Map: userId -> balance
    private val userWallets = ConcurrentHashMap<String, Double>()
    
    // Historical Data: symbol -> List of PricePoints (circular buffer)
    private val historicalData = ConcurrentHashMap<String, MutableList<PricePoint>>()
    
    // Cache for top gainers/losers
    private var cachedGainers: List<Stock> = emptyList()
    private var cachedLosers: List<Stock> = emptyList()
    
    private val mutex = Mutex()
    
    private val orderScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    init {
        // Initialize with stock data
        StockData.getInitialStocks().forEach { stock ->
            stocks[stock.symbol] = stock
            historicalData[stock.symbol] = mutableListOf(
                PricePoint(System.currentTimeMillis(), stock.currentPrice)
            )
        }
        updateRankings()
    }
    
    // Stock Operations
    fun getAllStocks(): List<Stock> = stocks.values.toList()
    
    fun getStock(symbol: String): Stock? = stocks[symbol]
    
    fun getStocksPaginated(limit: Int, offset: Int): List<Stock> {
        return stocks.values.toList()
            .sortedBy { it.symbol }
            .drop(offset)
            .take(limit)
    }
    
    fun searchStocks(query: String): List<Stock> {
        val lowerQuery = query.lowercase()
        return stocks.values.filter {
            it.symbol.lowercase().contains(lowerQuery) ||
            it.name.lowercase().contains(lowerQuery)
        }
    }
    
    fun getTopGainers(limit: Int = ServerConfig.TOP_GAINERS_COUNT): List<Stock> {
        return cachedGainers.take(limit)
    }
    
    fun getTopLosers(limit: Int = ServerConfig.TOP_LOSERS_COUNT): List<Stock> {
        return cachedLosers.take(limit)
    }
    
    fun getMostBought(limit: Int = ServerConfig.MOST_BUY_COUNT): List<Stock> {
        return stocks.values
            .sortedByDescending { it.volume }
            .take(limit)
    }
    
    fun getMostSold(limit: Int = ServerConfig.MOST_SELL_COUNT): List<Stock> {
        return stocks.values
            .sortedBy { it.dayChangePercent }
            .take(limit)
    }
    
    fun getRecentlyUpdated(limit: Int = 10): List<Stock> {
        return stocks.values
            .sortedByDescending { historicalData[it.symbol]?.lastOrNull()?.timestamp ?: 0L }
            .take(limit)
    }
    
    fun getStockDetail(symbol: String): StockDetail? {
        val stock = stocks[symbol] ?: return null
        val history = historicalData[symbol]?.toList() ?: emptyList()
        return StockDetail(stock = stock, historicalData = history)
    }
    
    /**
     * Generate mock historical data based on period
     * @param symbol Stock symbol
     * @param period One of: 1D, 1W, 1M, 1Y, MAX
     * @return List of PricePoint with timestamp and price
     */
    fun generateHistoricalData(symbol: String, period: String): List<PricePoint> {
        val stock = stocks[symbol] ?: return emptyList()
        val currentPrice = stock.currentPrice
        val now = System.currentTimeMillis()
        
        // Define period parameters: (dataPoints, intervalMs, volatility)
        val (dataPoints, intervalMs, volatility) = when (period.uppercase()) {
            "1D" -> Triple(78, 5 * 60 * 1000L, 0.002)           // 78 points, 5 min intervals, 0.2% volatility
            "1W" -> Triple(35, 4 * 60 * 60 * 1000L, 0.008)      // 35 points, 4 hour intervals, 0.8% volatility
            "1M" -> Triple(30, 24 * 60 * 60 * 1000L, 0.015)     // 30 points, 1 day intervals, 1.5% volatility
            "1Y" -> Triple(52, 7 * 24 * 60 * 60 * 1000L, 0.03)  // 52 points, 1 week intervals, 3% volatility
            "MAX" -> Triple(60, 30 * 24 * 60 * 60 * 1000L, 0.05) // 60 points, ~1 month intervals, 5% volatility
            else -> Triple(78, 5 * 60 * 1000L, 0.002)           // Default to 1D
        }
        
        // Use symbol hashcode as seed for consistent data per stock
        val seed = symbol.hashCode().toLong()
        val random = java.util.Random(seed + period.hashCode())
        
        // Generate price points using random walk backwards from current price
        val prices = mutableListOf<Double>()
        var price = currentPrice
        
        // Walk backwards to generate historical prices
        for (i in 0 until dataPoints) {
            prices.add(0, price)
            // Random walk: price changes by volatility percentage
            val change = (random.nextGaussian() * volatility * price)
            price = (price - change).coerceIn(currentPrice * 0.5, currentPrice * 1.5)
        }
        
        // Create PricePoints with timestamps
        return prices.mapIndexed { index, priceValue ->
            val timestamp = now - (dataPoints - 1 - index) * intervalMs
            PricePoint(timestamp, priceValue)
        }
    }
    
    // Price Update (called by PriceEngine)
    suspend fun updateStockPrice(symbol: String, newPrice: Double): StockUpdate? {
        val stock = stocks[symbol] ?: return null
        
        mutex.withLock {
            stock.previousPrice = stock.currentPrice
            stock.currentPrice = newPrice
            
            if (newPrice > stock.dayHigh) stock.dayHigh = newPrice
            if (newPrice < stock.dayLow) stock.dayLow = newPrice
            
            stock.volume += (100..1000).random().toLong()
            
            // Add to historical data
            val history = historicalData.getOrPut(symbol) { mutableListOf() }
            history.add(PricePoint(System.currentTimeMillis(), newPrice))
            if (history.size > ServerConfig.HISTORICAL_POINTS) {
                history.removeAt(0)
            }
        }
        
        return StockUpdate(
            symbol = stock.symbol,
            currentPrice = stock.currentPrice,
            previousPrice = stock.previousPrice,
            change = stock.change,
            changePercent = stock.changePercent,
            dayHigh = stock.dayHigh,
            dayLow = stock.dayLow,
            volume = stock.volume
        )
    }
    
    fun updateRankings() {
        val sortedStocks = stocks.values.sortedByDescending { it.dayChangePercent }
        cachedGainers = sortedStocks.take(10)
        cachedLosers = sortedStocks.takeLast(10)
    }
    
    // Portfolio Operations
    fun getPortfolio(userId: String): Portfolio {
        return portfolios.getOrPut(userId) { Portfolio(userId) }
    }
    
    fun getWallet(userId: String): Double = userWallets.getOrPut(userId) { 100_000.0 }
    
    /**
     * Create pending order. BUY reserves wallet immediately. Actual execution is async.
     * Returns PENDING always (or FAILED for instant validation errors).
     */
    fun createPendingOrder(request: TradeRequest): PendingOrderResponse {
        val stock = stocks[request.symbol]
        
        if (stock == null) {
            return PendingOrderResponse(
                orderId = UUID.randomUUID().toString(),
                symbol = request.symbol,
                action = request.action,
                quantity = request.quantity,
                priceAtOrder = 0.0,
                status = OrderStatus.FAILED,
                message = "Stock ${request.symbol} not found"
            )
        }
        
        val currentPrice = stock.currentPrice
        val totalValue = currentPrice * request.quantity
        val orderId = UUID.randomUUID().toString()
        
        // BUY: reserve wallet immediately to prevent double-spend
        if (request.action == TradeAction.BUY) {
            val balance = getWallet(request.userId)
            if (totalValue > balance) {
                return PendingOrderResponse(
                    orderId = orderId,
                    symbol = request.symbol,
                    action = request.action,
                    quantity = request.quantity,
                    priceAtOrder = currentPrice,
                    status = OrderStatus.FAILED,
                    message = "Insufficient balance",
                    updatedWalletBalance = balance
                )
            }
            userWallets[request.userId] = balance - totalValue
        }
        
        val pendingOrder = PendingOrder(
            orderId = orderId,
            userId = request.userId,
            symbol = request.symbol,
            action = request.action,
            quantity = request.quantity,
            priceAtOrder = currentPrice,
            status = OrderStatus.PENDING,
            message = "Order placed successfully"
        )
        
        pendingOrders[orderId] = pendingOrder
        
        orderScope.launch {
            processOrderAsync(pendingOrder, request)
        }
        
        return PendingOrderResponse(
            orderId = orderId,
            symbol = request.symbol,
            action = request.action,
            quantity = request.quantity,
            priceAtOrder = currentPrice,
            status = OrderStatus.PENDING,
            message = "Order placed successfully. Processing...",
            updatedWalletBalance = getWallet(request.userId)
        )
    }
    
    private suspend fun processOrderAsync(pendingOrder: PendingOrder, request: TradeRequest) {
        delay((1000L..3000L).random())
        
        val stock = stocks[request.symbol]
        if (stock == null) {
            sendOrderFailure(pendingOrder, "Stock not found")
            return
        }
        
        val portfolio = getPortfolio(request.userId)
        val currentPrice = stock.currentPrice
        val totalValue = currentPrice * request.quantity
        
        val result = mutex.withLock {
            when (request.action) {
                TradeAction.BUY -> executeBuyOrder(pendingOrder, portfolio, currentPrice, totalValue)
                TradeAction.SELL -> executeSellOrder(pendingOrder, portfolio, currentPrice, totalValue)
            }
        }
        
        pendingOrders.remove(pendingOrder.orderId)
        webSocketManager.sendOrderResult(request.userId, result)
    }
    
    private fun executeBuyOrder(
        pendingOrder: PendingOrder,
        portfolio: Portfolio,
        currentPrice: Double,
        totalValue: Double
    ): OrderResult {
        val existingHolding = portfolio.holdings.find { it.symbol == pendingOrder.symbol }
        
        val holding = if (existingHolding != null) {
            val newTotalInvested = existingHolding.totalInvested + totalValue
            val newQuantity = existingHolding.quantity + pendingOrder.quantity
            existingHolding.quantity = newQuantity
            existingHolding.totalInvested = newTotalInvested
            existingHolding.averagePrice = newTotalInvested / newQuantity
            existingHolding
        } else {
            val newHolding = Holding(
                symbol = pendingOrder.symbol,
                quantity = pendingOrder.quantity,
                averagePrice = currentPrice,
                totalInvested = totalValue
            )
            portfolio.holdings.add(newHolding)
            newHolding
        }
        
        return OrderResult(
            orderId = pendingOrder.orderId,
            userId = pendingOrder.userId,
            symbol = pendingOrder.symbol,
            action = pendingOrder.action,
            quantity = pendingOrder.quantity,
            pricePerUnit = currentPrice,
            totalValue = totalValue,
            status = OrderStatus.SUCCESS,
            message = "Successfully bought ${pendingOrder.quantity} shares of ${pendingOrder.symbol}",
            holding = holding.copy(),
            updatedWalletBalance = getWallet(pendingOrder.userId)
        )
    }
    
    private fun executeSellOrder(
        pendingOrder: PendingOrder,
        portfolio: Portfolio,
        currentPrice: Double,
        totalValue: Double
    ): OrderResult {
        val existingHolding = portfolio.holdings.find { it.symbol == pendingOrder.symbol }
        
        if (existingHolding == null || existingHolding.quantity < pendingOrder.quantity) {
            return OrderResult(
                orderId = pendingOrder.orderId,
                userId = pendingOrder.userId,
                symbol = pendingOrder.symbol,
                action = pendingOrder.action,
                quantity = pendingOrder.quantity,
                pricePerUnit = currentPrice,
                totalValue = totalValue,
                status = OrderStatus.FAILED,
                message = "Insufficient shares. You have ${existingHolding?.quantity ?: 0} shares.",
                updatedWalletBalance = getWallet(pendingOrder.userId)
            )
        }
        
        existingHolding.quantity -= pendingOrder.quantity
        existingHolding.totalInvested = existingHolding.averagePrice * existingHolding.quantity
        
        val holdingCopy = existingHolding.copy()
        
        if (existingHolding.quantity == 0) {
            portfolio.holdings.remove(existingHolding)
        }
        
        // SELL: add to wallet on success
        val newBalance = getWallet(pendingOrder.userId) + totalValue
        userWallets[pendingOrder.userId] = newBalance
        
        return OrderResult(
            orderId = pendingOrder.orderId,
            userId = pendingOrder.userId,
            symbol = pendingOrder.symbol,
            action = pendingOrder.action,
            quantity = pendingOrder.quantity,
            pricePerUnit = currentPrice,
            totalValue = totalValue,
            status = OrderStatus.SUCCESS,
            message = "Successfully sold ${pendingOrder.quantity} shares of ${pendingOrder.symbol}",
            holding = if (holdingCopy.quantity > 0) holdingCopy else null,
            updatedWalletBalance = newBalance
        )
    }
    
    private suspend fun sendOrderFailure(pendingOrder: PendingOrder, errorMessage: String) {
        // BUY: refund reserved wallet
        if (pendingOrder.action == TradeAction.BUY) {
            val refund = pendingOrder.priceAtOrder * pendingOrder.quantity
            val currentBalance = getWallet(pendingOrder.userId)
            userWallets[pendingOrder.userId] = currentBalance + refund
        }
        
        pendingOrders.remove(pendingOrder.orderId)
        
        val result = OrderResult(
            orderId = pendingOrder.orderId,
            userId = pendingOrder.userId,
            symbol = pendingOrder.symbol,
            action = pendingOrder.action,
            quantity = pendingOrder.quantity,
            pricePerUnit = pendingOrder.priceAtOrder,
            totalValue = pendingOrder.priceAtOrder * pendingOrder.quantity,
            status = OrderStatus.FAILED,
            message = errorMessage,
            updatedWalletBalance = getWallet(pendingOrder.userId)
        )
        
        webSocketManager.sendOrderResult(pendingOrder.userId, result)
    }
    
    // Legacy method - kept for backwards compatibility
    suspend fun executeTrade(request: TradeRequest): TradeResult {
        val stock = stocks[request.symbol]
            ?: return TradeResult(
                success = false,
                message = "Stock ${request.symbol} not found",
                userId = request.userId,
                symbol = request.symbol,
                action = request.action,
                quantity = request.quantity,
                pricePerUnit = 0.0,
                totalValue = 0.0
            )
        
        val portfolio = getPortfolio(request.userId)
        val currentPrice = stock.currentPrice
        val totalValue = currentPrice * request.quantity
        
        val result = mutex.withLock {
            when (request.action) {
                TradeAction.BUY -> executeBuy(portfolio, request, currentPrice, totalValue)
                TradeAction.SELL -> executeSell(portfolio, request, currentPrice, totalValue)
            }
        }
        
        // Send trade result via WebSocket
        if (result.success) {
            webSocketManager.sendTradeResult(request.userId, result)
        }
        
        return result
    }
    
    private fun executeBuy(
        portfolio: Portfolio,
        request: TradeRequest,
        currentPrice: Double,
        totalValue: Double
    ): TradeResult {
        val existingHolding = portfolio.holdings.find { it.symbol == request.symbol }
        
        val holding = if (existingHolding != null) {
            val newTotalInvested = existingHolding.totalInvested + totalValue
            val newQuantity = existingHolding.quantity + request.quantity
            existingHolding.quantity = newQuantity
            existingHolding.totalInvested = newTotalInvested
            existingHolding.averagePrice = newTotalInvested / newQuantity
            existingHolding
        } else {
            val newHolding = Holding(
                symbol = request.symbol,
                quantity = request.quantity,
                averagePrice = currentPrice,
                totalInvested = totalValue
            )
            portfolio.holdings.add(newHolding)
            newHolding
        }
        
        return TradeResult(
            success = true,
            message = "Successfully bought ${request.quantity} shares of ${request.symbol}",
            userId = request.userId,
            symbol = request.symbol,
            action = request.action,
            quantity = request.quantity,
            pricePerUnit = currentPrice,
            totalValue = totalValue,
            holding = holding.copy()
        )
    }
    
    private fun executeSell(
        portfolio: Portfolio,
        request: TradeRequest,
        currentPrice: Double,
        totalValue: Double
    ): TradeResult {
        val existingHolding = portfolio.holdings.find { it.symbol == request.symbol }
        
        if (existingHolding == null || existingHolding.quantity < request.quantity) {
            return TradeResult(
                success = false,
                message = "Insufficient shares to sell. You have ${existingHolding?.quantity ?: 0} shares.",
                userId = request.userId,
                symbol = request.symbol,
                action = request.action,
                quantity = request.quantity,
                pricePerUnit = currentPrice,
                totalValue = totalValue
            )
        }
        
        existingHolding.quantity -= request.quantity
        existingHolding.totalInvested = existingHolding.averagePrice * existingHolding.quantity
        
        val holdingCopy = existingHolding.copy()
        
        if (existingHolding.quantity == 0) {
            portfolio.holdings.remove(existingHolding)
        }
        
        return TradeResult(
            success = true,
            message = "Successfully sold ${request.quantity} shares of ${request.symbol}",
            userId = request.userId,
            symbol = request.symbol,
            action = request.action,
            quantity = request.quantity,
            pricePerUnit = currentPrice,
            totalValue = totalValue,
            holding = if (holdingCopy.quantity > 0) holdingCopy else null
        )
    }
    
    fun getStockSymbols(): List<String> = stocks.keys.toList()

    fun getHomeCategories(): HomeCategories {
        val usedSymbols = mutableSetOf<String>()

        val gainers = stocks.values
            .filter { it.symbol !in usedSymbols }
            .sortedByDescending { it.dayChangePercent }
            .take(ServerConfig.TOP_GAINERS_COUNT)
        usedSymbols.addAll(gainers.map { it.symbol })

        val losers = stocks.values
            .filter { it.symbol !in usedSymbols }
            .sortedBy { it.dayChangePercent }
            .take(ServerConfig.TOP_LOSERS_COUNT)
        usedSymbols.addAll(losers.map { it.symbol })

        val bought = stocks.values
            .filter { it.symbol !in usedSymbols }
            .sortedByDescending { it.volume }
            .take(ServerConfig.MOST_BUY_COUNT)
        usedSymbols.addAll(bought.map { it.symbol })

        val sold = stocks.values
            .filter { it.symbol !in usedSymbols }
            .sortedBy { it.volume }
            .take(ServerConfig.MOST_SELL_COUNT)

        return HomeCategories(gainers, losers, bought, sold)
    }

    fun getHomeSymbols(): Set<String> {
        val categories = getHomeCategories()
        return (categories.topGainers + categories.topLosers +
                categories.mostBought + categories.mostSold)
            .map { it.symbol }.toSet()
    }

    fun getPortfolioSymbols(userId: String): Set<String> {
        val portfolio = getPortfolio(userId)
        return portfolio.holdings.map { it.symbol }.toSet()
    }
    
    fun shutdown() {
        orderScope.cancel()
    }
}
