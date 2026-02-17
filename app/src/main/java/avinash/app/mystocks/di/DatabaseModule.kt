package avinash.app.mystocks.di

import android.content.Context
import androidx.room.Room
import avinash.app.mystocks.data.local.dao.*
import avinash.app.mystocks.data.local.db.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StockDatabase {
        return Room.databaseBuilder(
            context,
            StockDatabase::class.java,
            StockDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    @Provides
    fun provideStockDao(database: StockDatabase): StockDao = database.stockDao()
    
    @Provides
    fun provideRecentViewedDao(database: StockDatabase): RecentViewedDao = database.recentViewedDao()
    
    @Provides
    fun provideWishlistDao(database: StockDatabase): WishlistDao = database.wishlistDao()
    
    @Provides
    fun providePortfolioDao(database: StockDatabase): PortfolioDao = database.portfolioDao()
    
    @Provides
    fun providePendingOrderDao(database: StockDatabase): PendingOrderDao = database.pendingOrderDao()
}
