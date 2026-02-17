package avinash.app.mystocks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey
    val symbol: String,
    val addedAt: Long = System.currentTimeMillis()
)
