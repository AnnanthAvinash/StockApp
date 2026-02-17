package avinash.app.mystocks.data.repository

import avinash.app.mystocks.data.local.dao.*
import avinash.app.mystocks.data.local.entity.RecentViewedEntity
import avinash.app.mystocks.data.local.entity.WishlistEntity
import avinash.app.mystocks.data.local.mapper.*
import avinash.app.mystocks.data.remote.api.StockApi
import avinash.app.mystocks.data.remote.websocket.WebSocketConnectionManager
import avinash.app.mystocks.domain.model.PricePoint
import avinash.app.mystocks.domain.model.Stock
import avinash.app.mystocks.domain.model.StockDetail
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val stockApi: StockApi,
    private val stockDao: StockDao,
    private val recentViewedDao: RecentViewedDao,
    private val wishlistDao: WishlistDao,
    private val wsConnectionManager: WebSocketConnectionManager
) {
    val allStocks: Flow<List<Stock>> = stockDao.getAllStocks()
        .map { it.toDomainList() }

    val topGainers: Flow<List<Stock>> = stockDao.getTopGainers()
        .map { it.toDomainList() }

    val topLosers: Flow<List<Stock>> = stockDao.getTopLosers()
        .map { it.toDomainList() }

    val recentViewedStocks: Flow<List<Stock>> = recentViewedDao.getRecentViewedStocks()
        .map { it.toDomainList() }

    val wishlistStocks: Flow<List<Stock>> = wishlistDao.getWishlistStocks()
        .map { it.toDomainList() }

    private val _stockStore = MutableStateFlow<Map<String, Stock>>(emptyMap())

    suspend fun fetchHomeData(): Result<Unit> = runCatching {
        val response = stockApi.getHomeData()
        val allStocks = (response.topGainers + response.topLosers).distinctBy { it.symbol }
        stockDao.insertStocks(allStocks.toStockEntities())
    }

    suspend fun fetchAllStocks(): Result<Unit> = runCatching {
        val stocks = stockApi.getAllStocks()
        stockDao.insertStocks(stocks.toStockEntities())
    }

    suspend fun fetchStockDetail(symbol: String): Result<StockDetail> = runCatching {
        val detail = stockApi.getStock(symbol).toDomain()
        _stockStore.update { it + (symbol to detail.stock) }
        detail
    }

    fun observeStock(symbol: String): Flow<Stock?> {
        val stored = _stockStore.map { it[symbol] }

        val wsUpdated = wsConnectionManager.priceUpdates
            .filter { updates -> updates.any { it.symbol == symbol } }
            .map { updates ->
                val update = updates.first { it.symbol == symbol }
                val current = _stockStore.value[symbol] ?: return@map null
                val updated = current.copy(
                    currentPrice = update.currentPrice,
                    previousPrice = update.previousPrice,
                    dayHigh = maxOf(current.dayHigh, update.dayHigh),
                    dayLow = minOf(current.dayLow, update.dayLow),
                    volume = update.volume
                )
                _stockStore.update { it + (symbol to updated) }
                updated
            }

        return merge(stored, wsUpdated).distinctUntilChanged()
    }

    @Deprecated("Use observeStock() for detail screen", ReplaceWith("observeStock(symbol)"))
    fun getStockFlow(symbol: String): Flow<Stock?> = stockDao.getStockFlow(symbol)
        .map { it?.toDomain() }

    fun searchStocks(query: String): Flow<List<Stock>> = stockDao.searchStocks(query)
        .map { it.toDomainList() }

    suspend fun addToRecentViewed(symbol: String) {
        recentViewedDao.insertRecentViewed(RecentViewedEntity(symbol))
        recentViewedDao.keepOnlyRecent(20)
    }

    fun isInWishlist(symbol: String): Flow<Boolean> = wishlistDao.isInWishlist(symbol)

    suspend fun addToWishlist(symbol: String) {
        wishlistDao.addToWishlist(WishlistEntity(symbol))
    }

    suspend fun removeFromWishlist(symbol: String) {
        wishlistDao.removeFromWishlist(symbol)
    }

    suspend fun toggleWishlist(symbol: String) {
        if (wishlistDao.isInWishlistSync(symbol)) {
            wishlistDao.removeFromWishlist(symbol)
        } else {
            wishlistDao.addToWishlist(WishlistEntity(symbol))
        }
    }

    suspend fun fetchHistory(symbol: String, period: String): Result<List<PricePoint>> = runCatching {
        stockApi.getStockHistory(symbol, period).map { PricePoint(it.timestamp, it.price) }
    }
}
