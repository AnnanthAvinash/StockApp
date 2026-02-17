package avinash.app.mystocks.data.repository

import avinash.app.mystocks.data.cache.StockCacheDataSource
import avinash.app.mystocks.data.local.mapper.toDomain
import avinash.app.mystocks.data.remote.api.StockApi
import avinash.app.mystocks.domain.model.HomeData
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val stockApi: StockApi,
    private val cache: StockCacheDataSource
) {
    val homeData: StateFlow<HomeData> = cache.homeData

    suspend fun fetchHomeData(): Result<Unit> = runCatching {
        val response = stockApi.getHomeData()
        cache.setHomeData(response.toDomain())
    }
}
