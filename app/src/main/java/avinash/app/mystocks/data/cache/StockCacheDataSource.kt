package avinash.app.mystocks.data.cache

import avinash.app.mystocks.data.remote.dto.StockUpdateDto
import avinash.app.mystocks.domain.model.HomeData
import avinash.app.mystocks.domain.model.Stock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockCacheDataSource @Inject constructor() {

    private val _homeData = MutableStateFlow(HomeData())
    val homeData: StateFlow<HomeData> = _homeData.asStateFlow()

    private val _stockMap = MutableStateFlow<Map<String, Stock>>(emptyMap())

    fun setHomeData(data: HomeData) {
        _homeData.value = data
        val all = (data.topGainers + data.topLosers +
                data.mostBought + data.mostSold).distinctBy { it.symbol }
        _stockMap.value = all.associateBy { it.symbol }
    }

    fun updatePrices(updates: List<StockUpdateDto>) {
        _stockMap.update { current ->
            val mutable = current.toMutableMap()
            updates.forEach { u ->
                mutable[u.symbol]?.let { stock ->
                    mutable[u.symbol] = stock.copy(
                        currentPrice = u.currentPrice,
                        previousPrice = u.previousPrice,
                        dayHigh = maxOf(stock.dayHigh, u.dayHigh),
                        dayLow = minOf(stock.dayLow, u.dayLow),
                        volume = u.volume
                    )
                }
            }
            mutable
        }
        val map = _stockMap.value
        _homeData.update { current ->
            current.copy(
                topGainers = current.topGainers.mapWith(map),
                topLosers = current.topLosers.mapWith(map),
                mostBought = current.mostBought.mapWith(map),
                mostSold = current.mostSold.mapWith(map)
            )
        }
    }

    fun getStock(symbol: String): Flow<Stock?> =
        _stockMap.map { it[symbol] }.distinctUntilChanged()
}

private fun List<Stock>.mapWith(map: Map<String, Stock>): List<Stock> =
    map { map[it.symbol] ?: it }
