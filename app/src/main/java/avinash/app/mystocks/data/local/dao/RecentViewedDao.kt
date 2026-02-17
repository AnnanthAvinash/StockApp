package avinash.app.mystocks.data.local.dao

import androidx.room.*
import avinash.app.mystocks.data.local.entity.RecentViewedEntity
import avinash.app.mystocks.data.local.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewedDao {
    
    @Query("""
        SELECT s.* FROM stocks s 
        INNER JOIN recent_viewed r ON s.symbol = r.symbol 
        ORDER BY r.viewedAt DESC 
        LIMIT :limit
    """)
    fun getRecentViewedStocks(limit: Int = 10): Flow<List<StockEntity>>
    
    @Query("SELECT * FROM recent_viewed ORDER BY viewedAt DESC")
    fun getAllRecentViewed(): Flow<List<RecentViewedEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentViewed(recentViewed: RecentViewedEntity)
    
    @Query("DELETE FROM recent_viewed WHERE symbol = :symbol")
    suspend fun deleteRecentViewed(symbol: String)
    
    @Query("DELETE FROM recent_viewed")
    suspend fun deleteAllRecentViewed()
    
    // Keep only the most recent N items
    @Query("""
        DELETE FROM recent_viewed 
        WHERE symbol NOT IN (
            SELECT symbol FROM recent_viewed 
            ORDER BY viewedAt DESC 
            LIMIT :keepCount
        )
    """)
    suspend fun keepOnlyRecent(keepCount: Int = 20)
}
