package avinash.app.mystocks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey
    val symbol: String,
    val quantity: Int,
    val averagePrice: Double,
    val totalInvested: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)
