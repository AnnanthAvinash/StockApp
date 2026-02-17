package avinash.server.data

import avinash.server.models.*
import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class WebSocketManager(private val json: Json) {
    
    // Map of session ID to WebSocketSession
    private val clients = ConcurrentHashMap<String, WebSocketSession>()
    
    // Map of user ID to session ID (for targeted messages)
    private val userSessions = ConcurrentHashMap<String, String>()
    
    // Map of session ID to subscriptions (HOME, PORTFOLIO, WISHLIST, SYMBOL:AAPL)
    private val clientSubscriptions = ConcurrentHashMap<String, MutableSet<String>>()
    
    private val mutex = Mutex()
    
    suspend fun addClient(sessionId: String, session: WebSocketSession, userId: String? = null) {
        mutex.withLock {
            clients[sessionId] = session
            userId?.let { userSessions[it] = sessionId }
            clientSubscriptions[sessionId] = mutableSetOf()
        }
        println("Client connected: $sessionId (Total: ${clients.size})")
    }
    
    suspend fun removeClient(sessionId: String) {
        mutex.withLock {
            clients.remove(sessionId)
            clientSubscriptions.remove(sessionId)
            // Remove from userSessions if exists
            userSessions.entries.removeIf { it.value == sessionId }
        }
        println("Client disconnected: $sessionId (Total: ${clients.size})")
    }
    
    fun subscribe(sessionId: String, type: String) {
        clientSubscriptions[sessionId]?.add(type)
        println("Client $sessionId subscribed to: $type")
    }
    
    fun unsubscribe(sessionId: String, type: String) {
        clientSubscriptions[sessionId]?.remove(type)
        println("Client $sessionId unsubscribed from: $type")
    }
    
    fun getSubscriptions(sessionId: String): Set<String> {
        return clientSubscriptions[sessionId] ?: emptySet()
    }
    
    suspend fun broadcastPriceUpdate(updates: List<StockUpdate>, stockManager: StockManager) {
        clients.forEach { (sessionId, session) ->
            val subscriptions = clientSubscriptions[sessionId] ?: return@forEach

            val relevantUpdates = updates.filter { update ->
                subscriptions.any { sub ->
                    when {
                        sub == "HOME" || sub == "WISHLIST" -> {
                            stockManager.getHomeSymbols().contains(update.symbol)
                        }
                        sub == "PORTFOLIO" -> {
                            val userId = userSessions.entries.find { it.value == sessionId }?.key
                            userId != null && stockManager.getPortfolioSymbols(userId).contains(update.symbol)
                        }
                        sub.startsWith("SYMBOL:") -> {
                            update.symbol == sub.removePrefix("SYMBOL:")
                        }
                        else -> false
                    }
                }
            }

            if (relevantUpdates.isNotEmpty()) {
                val message = PriceUpdateMessage(updates = relevantUpdates)
                try {
                    session.send(Frame.Text(json.encodeToString(message)))
                } catch (e: Exception) {
                    println("Failed to send price update to $sessionId: ${e.message}")
                }
            }
        }
    }
    
    suspend fun broadcastRankingUpdate(gainers: List<Stock>, losers: List<Stock>) {
        val message = RankingUpdateMessage(topGainers = gainers, topLosers = losers)
        val messageJson = json.encodeToString(message)
        
        // Only send to clients subscribed to HOME
        clients.forEach { (sessionId, session) ->
            val subscriptions = clientSubscriptions[sessionId] ?: return@forEach
            if (subscriptions.contains("HOME")) {
                try {
                    session.send(Frame.Text(messageJson))
                } catch (e: Exception) {
                    println("Failed to send ranking update to $sessionId: ${e.message}")
                }
            }
        }
    }
    
    suspend fun sendTradeResult(userId: String, result: TradeResult) {
        val sessionId = userSessions[userId] ?: return
        val session = clients[sessionId] ?: return
        
        val message = TradeResultMessage(result = result)
        try {
            session.send(Frame.Text(json.encodeToString(message)))
        } catch (e: Exception) {
            println("Failed to send trade result to user $userId: ${e.message}")
        }
    }
    
    suspend fun sendOrderResult(userId: String, result: OrderResult) {
        val sessionId = userSessions[userId] ?: return
        val session = clients[sessionId] ?: return
        
        val message = OrderResultMessage(result = result)
        try {
            session.send(Frame.Text(json.encodeToString(message)))
        } catch (e: Exception) {
            println("Failed to send order result to user $userId: ${e.message}")
        }
    }
    
    suspend fun sendInitialData(sessionId: String, gainers: List<Stock>, losers: List<Stock>) {
        val session = clients[sessionId] ?: return
        val message = InitialMessage(topGainers = gainers, topLosers = losers)
        try {
            session.send(Frame.Text(json.encodeToString(message)))
        } catch (e: Exception) {
            println("Failed to send initial data: ${e.message}")
        }
    }
    
    private suspend fun broadcast(message: String) {
        println("Broadcasting to ${clients.size} clients")
        clients.values.forEach { session ->
            try {
                session.send(Frame.Text(message))
            } catch (e: Exception) {
                println("Failed to send to client: ${e.message}")
            }
        }
    }
    
    fun getActiveSymbols(stockManager: StockManager): Set<String> {
        val symbols = mutableSetOf<String>()

        clientSubscriptions.forEach { (sessionId, subs) ->
            subs.forEach { sub ->
                when {
                    sub == "HOME" || sub == "WISHLIST" -> symbols.addAll(stockManager.getHomeSymbols())
                    sub == "PORTFOLIO" -> {
                        val userId = userSessions.entries.find { it.value == sessionId }?.key
                        if (userId != null) symbols.addAll(stockManager.getPortfolioSymbols(userId))
                    }
                    sub.startsWith("SYMBOL:") -> symbols.add(sub.removePrefix("SYMBOL:"))
                }
            }
        }

        return symbols
    }

    fun getClientCount(): Int = clients.size
}
