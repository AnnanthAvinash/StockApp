package avinash.app.mystocks.data.repository

import avinash.app.mystocks.data.local.dao.PendingOrderDao
import avinash.app.mystocks.data.local.dao.PortfolioDao
import avinash.app.mystocks.data.local.dao.StockDao
import avinash.app.mystocks.data.local.mapper.*
import avinash.app.mystocks.data.remote.api.StockApi
import avinash.app.mystocks.data.remote.dto.PendingOrderResponseDto
import avinash.app.mystocks.data.remote.dto.TradeRequestDto
import avinash.app.mystocks.data.remote.websocket.WebSocketEvent
import avinash.app.mystocks.domain.model.*
import avinash.app.mystocks.util.Constants
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PortfolioRepository @Inject constructor(
    private val stockApi: StockApi,
    private val portfolioDao: PortfolioDao,
    private val pendingOrderDao: PendingOrderDao,
    private val stockDao: StockDao
) {
    // Portfolio with current stock prices
    val portfolio: Flow<Portfolio> = combine(
        portfolioDao.getAllHoldings(),
        stockDao.getAllStocks()
    ) { holdings, stocks ->
        val stockMap = stocks.associateBy { it.symbol }
        val holdingsWithPrices = holdings.map { holding ->
            val stock = stockMap[holding.symbol]?.toDomain()
            holding.toDomain(stock)
        }
        Portfolio(holdingsWithPrices)
    }
    
    // Pending orders
    val pendingOrders: Flow<List<PendingOrder>> = pendingOrderDao.getPendingOrders()
        .map { it.toPendingOrderList() }
    
    // Pending order count
    val pendingOrderCount: Flow<Int> = pendingOrderDao.getPendingOrderCount()
    
    // Get single holding
    fun getHolding(symbol: String): Flow<Holding?> = combine(
        portfolioDao.getHoldingFlow(symbol),
        stockDao.getStockFlow(symbol)
    ) { holding, stock ->
        holding?.toDomain(stock?.toDomain())
    }
    
    // Fetch portfolio from server
    suspend fun fetchPortfolio(): Result<Unit> = runCatching {
        val response = stockApi.getPortfolio(Constants.USER_ID)
        portfolioDao.insertHoldings(response.holdings.toPortfolioEntities())
    }
    
    // Execute trade - returns pending order
    suspend fun executeTrade(
        symbol: String,
        quantity: Int,
        action: TradeAction
    ): Result<PendingOrder> = runCatching {
        val request = TradeRequestDto(
            userId = Constants.USER_ID,
            symbol = symbol,
            quantity = quantity,
            action = action.name
        )
        
        val response = stockApi.executeTrade(request)
        
        // Save pending order locally
        val pendingOrder = response.toDomain()
        pendingOrderDao.insertOrder(response.toEntity())
        
        pendingOrder
    }
    
    // Handle order result from WebSocket
    suspend fun handleOrderResult(event: WebSocketEvent.OrderResult) {
        val result = event.result
        
        // Update pending order status
        pendingOrderDao.updateOrderStatus(
            orderId = result.orderId,
            status = result.status,
            message = result.message
        )
        
        // If successful, update portfolio
        if (result.status == "SUCCESS") {
            result.toHoldingEntity()?.let { holdingEntity ->
                portfolioDao.insertHolding(holdingEntity)
            } ?: run {
                // Holding is null means sold all shares
                portfolioDao.deleteHolding(result.symbol)
            }
            
            // Remove completed order after short delay (keep for UI feedback)
            // User can see it briefly then it disappears
        }
        
        // If failed, keep the order in the list with FAILED status
        // User can dismiss or retry
    }
    
    // Handle legacy trade result from WebSocket (for backwards compatibility)
    suspend fun handleTradeResult(result: WebSocketEvent.TradeResult) {
        val tradeResult = result.result
        if (tradeResult.success) {
            tradeResult.holding?.let { holding ->
                portfolioDao.insertHolding(holding.toEntity())
            } ?: run {
                // Holding is null means sold all shares
                portfolioDao.deleteHolding(tradeResult.symbol)
            }
        }
    }
    
    // Remove a pending order
    suspend fun removePendingOrder(orderId: String) {
        pendingOrderDao.deleteOrder(orderId)
    }
    
    // Clear completed orders
    suspend fun clearCompletedOrders() {
        pendingOrderDao.deleteCompletedOrders()
    }
    
    // Get total invested
    val totalInvested: Flow<Double> = portfolioDao.getTotalInvested()
        .map { it ?: 0.0 }
}
