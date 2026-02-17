package avinash.app.mystocks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_orders")
data class PendingOrderEntity(
    @PrimaryKey
    val orderId: String,
    val symbol: String,
    val action: String, // "BUY" or "SELL"
    val quantity: Int,
    val priceAtOrder: Double,
    val status: String, // "PENDING", "SUCCESS", "FAILED"
    val createdAt: Long = System.currentTimeMillis(),
    val message: String? = null
)
