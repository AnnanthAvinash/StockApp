package avinash.app.mystocks.domain.model

data class Stock(
    val symbol: String,
    val name: String,
    val logoUrl: String,
    val currentPrice: Double,
    val previousPrice: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val volume: Long,
    val openPrice: Double
) {
    val change: Double
        get() = currentPrice - previousPrice
    
    val changePercent: Double
        get() = if (previousPrice > 0) ((currentPrice - previousPrice) / previousPrice) * 100 else 0.0
    
    val dayChangePercent: Double
        get() = if (openPrice > 0) ((currentPrice - openPrice) / openPrice) * 100 else 0.0
    
    val isGainer: Boolean
        get() = dayChangePercent > 0
}

data class StockDetail(
    val stock: Stock,
    val historicalData: List<PricePoint>
)

data class PricePoint(
    val timestamp: Long,
    val price: Double
)
