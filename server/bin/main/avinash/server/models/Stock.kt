package avinash.server.models

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val symbol: String,
    val name: String,
    val logoUrl: String,
    var currentPrice: Double,
    var previousPrice: Double,
    var dayHigh: Double,
    var dayLow: Double,
    var volume: Long,
    var openPrice: Double
) {
    val change: Double
        get() = currentPrice - previousPrice
    
    val changePercent: Double
        get() = if (previousPrice > 0) ((currentPrice - previousPrice) / previousPrice) * 100 else 0.0
    
    val dayChangePercent: Double
        get() = if (openPrice > 0) ((currentPrice - openPrice) / openPrice) * 100 else 0.0
}

@Serializable
data class StockDetail(
    val stock: Stock,
    val historicalData: List<PricePoint>
)

@Serializable
data class PricePoint(
    val timestamp: Long,
    val price: Double
)
