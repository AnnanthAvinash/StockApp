package avinash.app.mystocks.domain.model

data class PendingOrder(
    val orderId: String,
    val symbol: String,
    val action: TradeAction,
    val quantity: Int,
    val priceAtOrder: Double,
    val status: OrderStatus,
    val createdAt: Long,
    val message: String? = null,
    val updatedWalletBalance: Double? = null
) {
    val totalValue: Double
        get() = priceAtOrder * quantity
    
    val isPending: Boolean
        get() = status == OrderStatus.PENDING
    
    val isSuccess: Boolean
        get() = status == OrderStatus.SUCCESS
    
    val isFailed: Boolean
        get() = status == OrderStatus.FAILED
    
    val isCanceled: Boolean
        get() = status == OrderStatus.CANCELED
}

enum class OrderStatus {
    PENDING, SUCCESS, FAILED, CANCELED;
    
    companion object {
        fun fromString(value: String): OrderStatus {
            return when (value.uppercase()) {
                "PENDING" -> PENDING
                "SUCCESS" -> SUCCESS
                "FAILED" -> FAILED
                "CANCELED" -> CANCELED
                else -> PENDING
            }
        }
    }
}
