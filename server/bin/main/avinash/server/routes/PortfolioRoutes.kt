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
    
    // POST /api/trade - Execute buy/sell (returns pending, result sent via WebSocket)
    post("/api/trade") {
        try {
            val request = call.receive<TradeRequest>()
            
            // Validate request
            if (request.quantity <= 0) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Quantity must be positive")
                )
            }
            
            // Create pending order - result will be sent via WebSocket
            val pendingOrder = stockManager.createPendingOrder(request)
            
            // Return pending order response
            val statusCode = when (pendingOrder.status) {
                OrderStatus.PENDING -> HttpStatusCode.Accepted
                OrderStatus.FAILED -> HttpStatusCode.BadRequest
                OrderStatus.SUCCESS -> HttpStatusCode.OK
            }
            
            call.respond(statusCode, pendingOrder)
            
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Invalid trade request: ${e.message}")
            )
        }
    }
}
