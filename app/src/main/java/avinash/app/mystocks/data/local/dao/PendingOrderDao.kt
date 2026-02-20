package avinash.app.mystocks.data.local.dao

import androidx.room.*
import avinash.app.mystocks.data.local.entity.PendingOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingOrderDao {
    
    @Query("SELECT * FROM pending_orders WHERE status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingOrders(): Flow<List<PendingOrderEntity>>
    
    @Query("SELECT * FROM pending_orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<PendingOrderEntity>>
    
    @Query("SELECT * FROM pending_orders WHERE status != 'SUCCESS' ORDER BY createdAt DESC")
    fun getNonSuccessOrders(): Flow<List<PendingOrderEntity>>
    
    @Query("SELECT * FROM pending_orders WHERE orderId = :orderId")
    suspend fun getOrder(orderId: String): PendingOrderEntity?
    
    @Query("SELECT * FROM pending_orders WHERE orderId = :orderId")
    fun getOrderFlow(orderId: String): Flow<PendingOrderEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: PendingOrderEntity)
    
    @Update
    suspend fun updateOrder(order: PendingOrderEntity)
    
    @Query("UPDATE pending_orders SET status = :status, message = :message WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String, message: String?)
    
    @Query("DELETE FROM pending_orders WHERE orderId = :orderId")
    suspend fun deleteOrder(orderId: String)
    
    @Query("DELETE FROM pending_orders WHERE status != 'PENDING'")
    suspend fun deleteCompletedOrders()
    
    @Query("DELETE FROM pending_orders")
    suspend fun clearAllOrders()
    
    @Query("SELECT COUNT(*) FROM pending_orders WHERE status = 'PENDING'")
    fun getPendingOrderCount(): Flow<Int>
    
    @Query("DELETE FROM pending_orders WHERE status IN ('FAILED', 'CANCELED') AND createdAt < :cutoffTime")
    suspend fun deleteExpiredOrders(cutoffTime: Long)
}
