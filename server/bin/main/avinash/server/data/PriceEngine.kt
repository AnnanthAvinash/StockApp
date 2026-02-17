package avinash.server.data

import avinash.server.config.ServerConfig
import avinash.server.models.StockUpdate
import kotlinx.coroutines.*
import kotlin.math.roundToLong
import kotlin.random.Random

class PriceEngine(
    private val stockManager: StockManager,
    private val webSocketManager: WebSocketManager
) {
    private var job: Job? = null
    private var previousGainers: List<String> = emptyList()
    private var previousLosers: List<String> = emptyList()
    
    fun start(scope: CoroutineScope) {
        job = scope.launch {
            println("Price Engine started")
            
            while (isActive) {
                try {
                    // Random delay between updates
                    val delay = ServerConfig.PRICE_UPDATE_INTERVAL_MS + 
                        Random.nextLong(0, 1000)
                    delay(delay)
                    
                    // Update prices
                    val updates = updatePrices()
                    
                    if (updates.isNotEmpty()) {
                        println("Broadcasting ${updates.size} price updates: ${updates.map { "${it.symbol}=$${it.currentPrice}" }}")
                        webSocketManager.broadcastPriceUpdate(updates, stockManager)
                        
                        // Update rankings and check if changed
                        stockManager.updateRankings()
                        checkAndBroadcastRankingChanges()
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    println("Price Engine error: ${e.message}")
                }
            }
        }
    }
    
    fun stop() {
        job?.cancel()
        job = null
        println("Price Engine stopped")
    }
    
    private suspend fun updatePrices(): List<StockUpdate> {
        val activeSymbols = webSocketManager.getActiveSymbols(stockManager).toList()
        if (activeSymbols.isEmpty()) return emptyList()

        val stocksToUpdate = activeSymbols.shuffled().take(
            minOf(Random.nextInt(8, ServerConfig.STOCKS_PER_UPDATE + 1), activeSymbols.size)
        )
        
        val updates = mutableListOf<StockUpdate>()
        
        stocksToUpdate.forEach { symbol ->
            val stock = stockManager.getStock(symbol) ?: return@forEach
            
            // Generate random price change
            val changePercent = Random.nextDouble(
                ServerConfig.MIN_PRICE_CHANGE_PERCENT,
                ServerConfig.MAX_PRICE_CHANGE_PERCENT
            ) / 100.0
            
            val priceChange = stock.currentPrice * changePercent
            var newPrice = stock.currentPrice + priceChange
            
            // Ensure minimum price of 1.0
            newPrice = newPrice.coerceAtLeast(1.0)
            
            // Round to 2 decimal places
            newPrice = (newPrice * 100).roundToLong() / 100.0
            
            // Update stock and get update object
            stockManager.updateStockPrice(symbol, newPrice)?.let {
                updates.add(it)
            }
        }
        
        return updates
    }
    
    private suspend fun checkAndBroadcastRankingChanges() {
        val currentGainers = stockManager.getTopGainers(ServerConfig.TOP_GAINERS_COUNT)
            .map { it.symbol }
        val currentLosers = stockManager.getTopLosers(ServerConfig.TOP_LOSERS_COUNT)
            .map { it.symbol }
        
        if (currentGainers != previousGainers || currentLosers != previousLosers) {
            previousGainers = currentGainers
            previousLosers = currentLosers
            
            webSocketManager.broadcastRankingUpdate(
                gainers = stockManager.getTopGainers(ServerConfig.TOP_GAINERS_COUNT),
                losers = stockManager.getTopLosers(ServerConfig.TOP_LOSERS_COUNT)
            )
        }
    }
}
