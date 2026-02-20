package avinash.server.routes

import avinash.server.data.StockManager
import avinash.server.models.OrderStatus
import avinash.server.models.TradeRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.portfolioRoutes(stockManager: StockManager) {
    
    // GET /api/portfolio/{userId} - Get user portfolio
    get("/api/portfolio/{userId}") {
        val userId = call.parameters["userId"]
            ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "User ID required"))
        
        val portfolio = stockManager.getPortfolio(userId)
        call.respond(portfolio)
    }
    
    // POST /api/trade - Place order (returns PENDING, result via WebSocket)
    post("/api/trade") {
        try {
            val request = call.receive<TradeRequest>()
            
            if (request.quantity <= 0) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Quantity must be positive")
                )
            }
            
            val result = stockManager.createPendingOrder(request)
            
            val statusCode = when (result.status) {
                OrderStatus.PENDING -> HttpStatusCode.Accepted
                OrderStatus.FAILED -> HttpStatusCode.BadRequest
                else -> HttpStatusCode.OK
            }
            
            call.respond(statusCode, result)
            
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Invalid trade request: ${e.message}")
            )
        }
    }
    
    // GET /api/wallet/{userId} - Get user wallet balance
    get("/api/wallet/{userId}") {
        val userId = call.parameters["userId"]
            ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "User ID required"))
        val balance = stockManager.getWallet(userId)
        call.respond(mapOf("balance" to balance))
    }
}
