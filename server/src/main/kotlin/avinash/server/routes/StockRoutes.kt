package avinash.server.routes

import avinash.server.data.StockManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.stockRoutes(stockManager: StockManager) {
    
    route("/api/stocks") {
        
        // GET /api/stocks - All stocks with optional pagination and search
        get {
            val limit = call.request.queryParameters["limit"]?.toIntOrNull()
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
            val search = call.request.queryParameters["search"]
            
            val stocks = when {
                !search.isNullOrBlank() -> stockManager.searchStocks(search)
                limit != null -> stockManager.getStocksPaginated(limit, offset)
                else -> stockManager.getAllStocks()
            }
            
            call.respond(stocks)
        }
        
        // GET /api/stocks/{symbol} - Single stock detail with history
        get("{symbol}") {
            val symbol = call.parameters["symbol"]?.uppercase()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Symbol required"))
            
            val stockDetail = stockManager.getStockDetail(symbol)
            
            if (stockDetail != null) {
                call.respond(stockDetail)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Stock not found: $symbol"))
            }
        }
        
        // GET /api/stocks/{symbol}/history?period={1D|1W|1M|1Y|MAX} - Historical price data
        get("{symbol}/history") {
            val symbol = call.parameters["symbol"]?.uppercase()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Symbol required"))
            
            val period = call.request.queryParameters["period"] ?: "1D"
            
            val stock = stockManager.getStock(symbol)
            if (stock == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Stock not found: $symbol"))
                return@get
            }
            
            val historicalData = stockManager.generateHistoricalData(symbol, period)
            call.respond(historicalData)
        }
        
        // GET /api/stocks/top/gainers - Top gainers
        route("/top") {
            get("/gainers") {
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 5
                call.respond(stockManager.getTopGainers(limit))
            }
            
            // GET /api/stocks/top/losers - Top losers
            get("/losers") {
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 5
                call.respond(stockManager.getTopLosers(limit))
            }
        }
    }
}
