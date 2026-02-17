package avinash.app.mystocks.data.remote.api

import avinash.app.mystocks.data.remote.dto.*
import avinash.app.mystocks.util.Constants
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockApi @Inject constructor(
    private val client: HttpClient
) {
    
    suspend fun getHomeData(): HomeResponse {
        return client.get("${Constants.BASE_URL}${Constants.API_HOME}").body()
    }
    
    suspend fun getAllStocks(): List<StockDto> {
        return client.get("${Constants.BASE_URL}${Constants.API_STOCKS}").body()
    }
    
    suspend fun getStock(symbol: String): StockDetailDto {
        return client.get("${Constants.BASE_URL}${Constants.API_STOCKS}/$symbol").body()
    }
    
    suspend fun searchStocks(query: String): List<StockDto> {
        return client.get("${Constants.BASE_URL}${Constants.API_STOCKS}") {
            parameter("search", query)
        }.body()
    }
    
    suspend fun getPortfolio(userId: String): PortfolioDto {
        return client.get("${Constants.BASE_URL}${Constants.API_PORTFOLIO}/$userId").body()
    }
    
    suspend fun executeTrade(request: TradeRequestDto): PendingOrderResponseDto {
        return client.post("${Constants.BASE_URL}${Constants.API_TRADE}") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    
    suspend fun getStockHistory(symbol: String, period: String): List<PricePointDto> {
        return client.get("${Constants.BASE_URL}${Constants.API_STOCKS}/$symbol/history") {
            parameter("period", period)
        }.body()
    }
}
