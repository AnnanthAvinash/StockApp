package avinash.app.mystocks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_viewed")
data class RecentViewedEntity(
    @PrimaryKey
    val symbol: String,
    val viewedAt: Long = System.currentTimeMillis()
)
