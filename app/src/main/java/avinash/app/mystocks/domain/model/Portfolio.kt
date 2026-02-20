package avinash.app.mystocks.domain.model

data class Holding(
    val symbol: String,
    val name: String,
    val logoUrl: String,
    val quantity: Int,
    val averagePrice: Double,
    val totalInvested: Double,
    val currentPrice: Double,
    val previousClose: Double = 0.0
) {
    val currentValue: Double
        get() = quantity * currentPrice
    
    val profitLoss: Double
        get() = currentValue - totalInvested
    
    val profitLossPercent: Double
        get() = if (totalInvested > 0) (profitLoss / totalInvested) * 100 else 0.0
    
    val isProfit: Boolean
        get() = profitLoss >= 0

    val todayProfitLoss: Double
        get() = if (previousClose > 0) (currentPrice - previousClose) * quantity else 0.0

    val todayProfitLossPercent: Double
        get() {
            val prevValue = previousClose * quantity
            return if (prevValue > 0) (todayProfitLoss / prevValue) * 100 else 0.0
        }
}

data class Portfolio(
    val holdings: List<Holding>
) {
    val totalInvested: Double
        get() = holdings.sumOf { it.totalInvested }
    
    val totalCurrentValue: Double
        get() = holdings.sumOf { it.currentValue }
    
    val totalProfitLoss: Double
        get() = totalCurrentValue - totalInvested
    
    val totalProfitLossPercent: Double
        get() = if (totalInvested > 0) (totalProfitLoss / totalInvested) * 100 else 0.0

    val todayProfitLoss: Double
        get() = holdings.sumOf { it.todayProfitLoss }

    val todayProfitLossPercent: Double
        get() {
            val prevTotal = holdings.sumOf { it.previousClose * it.quantity }
            return if (prevTotal > 0) (todayProfitLoss / prevTotal) * 100 else 0.0
        }
}
