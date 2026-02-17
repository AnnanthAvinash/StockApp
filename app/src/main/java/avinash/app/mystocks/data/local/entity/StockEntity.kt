package avinash.app.mystocks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockEntity(
    @PrimaryKey
    val symbol: String,
    val name: String,
    val logoUrl: String,
    val currentPrice: Double,
    val previousPrice: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val volume: Long,
    val openPrice: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)
