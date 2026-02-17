package avinash.server.routes

import avinash.server.data.StockManager
import avinash.server.data.WebSocketManager
import avinash.server.models.SubscriptionMessage
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.json.Json
import java.util.UUID

fun Route.webSocketRoutes(
    stockManager: StockManager,
    webSocketManager: WebSocketManager,
    json: Json
) {
    
    // WebSocket endpoint for real-time stock updates
    webSocket("/ws/stocks") {
        val sessionId = UUID.randomUUID().toString()
        
        // Get userId from query parameter if provided
        val userId = call.request.queryParameters["userId"]
        
        // Add client to manager
        webSocketManager.addClient(sessionId, this, userId)
        
        try {
            // Send initial data
            webSocketManager.sendInitialData(
                sessionId = sessionId,
                gainers = stockManager.getTopGainers(),
                losers = stockManager.getTopLosers()
            )
            
            // Handle incoming messages
            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        println("Received from $sessionId: $text")
                        
                        // Handle subscription messages
                        try {
                            val message = json.decodeFromString<SubscriptionMessage>(text)
                            when (message.action) {
                                "SUBSCRIBE" -> webSocketManager.subscribe(sessionId, message.type)
                                "UNSUBSCRIBE" -> webSocketManager.unsubscribe(sessionId, message.type)
                            }
                        } catch (e: Exception) {
                            println("Failed to parse message from $sessionId: ${e.message}")
                        }
                    }
                    is Frame.Close -> {
                        println("Client $sessionId requested close")
                        break
                    }
                    else -> {}
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            println("Client $sessionId channel closed")
        } catch (e: Exception) {
            println("WebSocket error for $sessionId: ${e.message}")
        } finally {
            webSocketManager.removeClient(sessionId)
        }
    }
}
