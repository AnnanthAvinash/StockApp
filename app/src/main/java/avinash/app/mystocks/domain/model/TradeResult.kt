package avinash.app.mystocks.domain.model

data class TradeResult(
    val success: Boolean,
    val message: String,
    val symbol: String,
    val action: TradeAction,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalValue: Double,
    val updatedHolding: Holding? = null
)

enum class TradeAction {
    BUY, SELL
}
