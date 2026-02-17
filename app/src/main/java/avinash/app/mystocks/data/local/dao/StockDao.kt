package avinash.app.mystocks.data.local.dao

import androidx.room.*
import avinash.app.mystocks.data.local.entity.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    
    @Query("SELECT * FROM stocks ORDER BY symbol ASC")
    fun getAllStocks(): Flow<List<StockEntity>>
    
    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    suspend fun getStock(symbol: String): StockEntity?
    
    @Query("SELECT * FROM stocks WHERE symbol = :symbol")
    fun getStockFlow(symbol: String): Flow<StockEntity?>
    
    @Query("SELECT * FROM stocks ORDER BY (currentPrice - openPrice) / openPrice * 100 DESC LIMIT :limit")
    fun getTopGainers(limit: Int = 5): Flow<List<StockEntity>>
    
    @Query("SELECT * FROM stocks ORDER BY (currentPrice - openPrice) / openPrice * 100 ASC LIMIT :limit")
    fun getTopLosers(limit: Int = 5): Flow<List<StockEntity>>
    
    @Query("SELECT * FROM stocks WHERE symbol LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    fun searchStocks(query: String): Flow<List<StockEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: StockEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStocks(stocks: List<StockEntity>)
    
    @Update
    suspend fun updateStock(stock: StockEntity)
    
    @Query("UPDATE stocks SET currentPrice = :price, previousPrice = currentPrice, dayHigh = CASE WHEN :price > dayHigh THEN :price ELSE dayHigh END, dayLow = CASE WHEN :price < dayLow THEN :price ELSE dayLow END, volume = volume + :volumeIncrease, lastUpdated = :timestamp WHERE symbol = :symbol")
    suspend fun updateStockPrice(symbol: String, price: Double, volumeIncrease: Long, timestamp: Long)
    
    @Query("DELETE FROM stocks")
    suspend fun deleteAllStocks()
}
