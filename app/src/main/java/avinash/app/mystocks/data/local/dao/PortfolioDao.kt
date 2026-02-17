package avinash.app.mystocks.data.local.dao

import androidx.room.*
import avinash.app.mystocks.data.local.entity.PortfolioEntity
import avinash.app.mystocks.data.local.entity.StockEntity
import kotlinx.coroutines.flow.Flow

data class PortfolioWithStock(
    val portfolio: PortfolioEntity,
    val stock: StockEntity?
)

@Dao
interface PortfolioDao {
    
    @Query("SELECT * FROM portfolio ORDER BY symbol ASC")
    fun getAllHoldings(): Flow<List<PortfolioEntity>>
    
    @Query("SELECT * FROM portfolio WHERE symbol = :symbol")
    suspend fun getHolding(symbol: String): PortfolioEntity?
    
    @Query("SELECT * FROM portfolio WHERE symbol = :symbol")
    fun getHoldingFlow(symbol: String): Flow<PortfolioEntity?>
    
    @Transaction
    @Query("""
        SELECT p.*, s.* FROM portfolio p
        LEFT JOIN stocks s ON p.symbol = s.symbol
        ORDER BY p.symbol ASC
    """)
    fun getHoldingsWithStocks(): Flow<Map<PortfolioEntity, StockEntity?>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolding(holding: PortfolioEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(holdings: List<PortfolioEntity>)
    
    @Update
    suspend fun updateHolding(holding: PortfolioEntity)
    
    @Query("DELETE FROM portfolio WHERE symbol = :symbol")
    suspend fun deleteHolding(symbol: String)
    
    @Query("DELETE FROM portfolio")
    suspend fun clearPortfolio()
    
    @Query("SELECT SUM(totalInvested) FROM portfolio")
    fun getTotalInvested(): Flow<Double?>
}
