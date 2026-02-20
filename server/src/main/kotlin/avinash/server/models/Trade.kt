package avinash.server.models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
enum class TradeAction {
    BUY, SELL
}

@Serializable
enum class OrderStatus {
    PENDING, SUCCESS, FAILED, CANCELED
}

@Serializable
data class TradeRequest(
    val userId: String,
    val symbol: String,
    val quantity: Int,
    val action: TradeAction
)

@Serializable
data class PendingOrder(
    val orderId: String = UUID.randomUUID().toString(),
    val userId: String,
    val symbol: String,
    val action: TradeAction,
    val quantity: Int,
    val priceAtOrder: Double,
    val status: OrderStatus = OrderStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val message: String? = null
)

@Serializable
data class PendingOrderResponse(
    val orderId: String,
    val symbol: String,
    val action: TradeAction,
    val quantity: Int,
    val priceAtOrder: Double,
    val status: OrderStatus,
    val message: String,
    val updatedWalletBalance: Double? = null
)

@Serializable
data class OrderResult(
    val orderId: String,
    val userId: String,
    val symbol: String,
    val action: TradeAction,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalValue: Double,
    val status: OrderStatus,
    val message: String,
    val holding: Holding? = null,
    val updatedWalletBalance: Double? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class TradeResult(
    val success: Boolean,
    val message: String,
    val userId: String,
    val symbol: String,
    val action: TradeAction,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalValue: Double,
    val holding: Holding? = null,
    val timestamp: Long = System.currentTimeMillis()
)
