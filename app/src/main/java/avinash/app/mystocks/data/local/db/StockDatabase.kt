package avinash.app.mystocks.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import avinash.app.mystocks.data.local.dao.*
import avinash.app.mystocks.data.local.entity.*

@Database(
    entities = [
        StockEntity::class,
        RecentViewedEntity::class,
        WishlistEntity::class,
        PortfolioEntity::class,
        PendingOrderEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun recentViewedDao(): RecentViewedDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun pendingOrderDao(): PendingOrderDao
    
    companion object {
        const val DATABASE_NAME = "mystocks_db"
    }
}
