package avinash.app.mystocks.data.local.dao

import androidx.room.*
import avinash.app.mystocks.data.local.entity.StockEntity
import avinash.app.mystocks.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    
    @Query("""
        SELECT s.* FROM stocks s 
        INNER JOIN wishlist w ON s.symbol = w.symbol 
        ORDER BY w.addedAt DESC
    """)
    fun getWishlistStocks(): Flow<List<StockEntity>>
    
    @Query("SELECT * FROM wishlist ORDER BY addedAt DESC")
    fun getAllWishlistItems(): Flow<List<WishlistEntity>>
    
    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE symbol = :symbol)")
    fun isInWishlist(symbol: String): Flow<Boolean>
    
    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE symbol = :symbol)")
    suspend fun isInWishlistSync(symbol: String): Boolean
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(wishlistEntity: WishlistEntity)
    
    @Query("DELETE FROM wishlist WHERE symbol = :symbol")
    suspend fun removeFromWishlist(symbol: String)
    
    @Query("DELETE FROM wishlist")
    suspend fun clearWishlist()
}
