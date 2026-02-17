package avinash.server.models

import kotlinx.serialization.Serializable

@Serializable
data class Portfolio(
    val userId: String,
    val holdings: MutableList<Holding> = mutableListOf()
) {
    val totalInvested: Double
        get() = holdings.sumOf { it.totalInvested }
}

@Serializable
data class Holding(
    val symbol: String,
    var quantity: Int,
    var averagePrice: Double,
    var totalInvested: Double
) {
    fun currentValue(currentPrice: Double): Double = quantity * currentPrice
    
    fun profitLoss(currentPrice: Double): Double = currentValue(currentPrice) - totalInvested
    
    fun profitLossPercent(currentPrice: Double): Double {
        return if (totalInvested > 0) (profitLoss(currentPrice) / totalInvested) * 100 else 0.0
    }
}
