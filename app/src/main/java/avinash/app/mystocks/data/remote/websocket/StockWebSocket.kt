package avinash.app.mystocks.data.remote.websocket

import avinash.app.mystocks.data.remote.dto.*
import avinash.app.mystocks.util.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class SubscriptionMessage(
    val action: String,
    val type: String
)

sealed class WebSocketEvent {
    data class Initial(val gainers: List<StockDto>, val losers: List<StockDto>) : WebSocketEvent()
    data class PriceUpdate(val updates: List<StockUpdateDto>) : WebSocketEvent()
    data class RankingUpdate(val gainers: List<StockDto>, val losers: List<StockDto>) : WebSocketEvent()
    data class TradeResult(val result: TradeResultDto) : WebSocketEvent()
    data class OrderResult(val result: OrderResultDto) : WebSocketEvent()
    data class Error(val message: String) : WebSocketEvent()
    object Connected : WebSocketEvent()
    object Disconnected : WebSocketEvent()
}

@Singleton
class StockWebSocket @Inject constructor(
    private val json: Json
) : StockSocketClient {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .pingInterval(15, TimeUnit.SECONDS)
        .build()
    
    private val _connectionState = MutableStateFlow(false)
    override val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()
    
    // Pending subscriptions — queued when WS not yet connected
    private val pendingSubscriptions = mutableSetOf<String>()
    
    override fun connect(userId: String?): Flow<WebSocketEvent> = callbackFlow {
        val url = if (userId != null) {
            "${Constants.WS_URL}?userId=$userId"
        } else {
            Constants.WS_URL
        }
        
        val request = Request.Builder()
            .url(url)
            .build()
        
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionState.value = true
                trySend(WebSocketEvent.Connected)
                // Flush pending subscriptions
                flushPendingSubscriptions()
            }
            
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val event = parseMessage(text)
                    event?.let { trySend(it) }
                } catch (e: Exception) {
                    trySend(WebSocketEvent.Error("Parse error: ${e.message}"))
                }
            }
            
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _connectionState.value = false
                trySend(WebSocketEvent.Disconnected)
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionState.value = false
                trySend(WebSocketEvent.Error(t.message ?: "Connection failed"))
                trySend(WebSocketEvent.Disconnected)
            }
        }
        
        webSocket = client.newWebSocket(request, listener)
        
        awaitClose {
            disconnect()
        }
    }
    
    override fun disconnect() {
        webSocket?.close(1000, "User disconnect")
        webSocket = null
        _connectionState.value = false
        pendingSubscriptions.clear()
    }
    
    override fun subscribe(type: String) {
        val message = SubscriptionMessage(action = "SUBSCRIBE", type = type)
        val sent = webSocket?.send(json.encodeToString(message)) ?: false
        if (sent) {
            println("Subscribed to: $type")
        } else {
            // WS not ready — queue it
            pendingSubscriptions.add(type)
            println("Queued subscription: $type")
        }
    }
    
    override fun unsubscribe(type: String) {
        pendingSubscriptions.remove(type)
        val message = SubscriptionMessage(action = "UNSUBSCRIBE", type = type)
        webSocket?.send(json.encodeToString(message))
        println("Unsubscribed from: $type")
    }
    
    private fun flushPendingSubscriptions() {
        pendingSubscriptions.forEach { type ->
            val message = SubscriptionMessage(action = "SUBSCRIBE", type = type)
            webSocket?.send(json.encodeToString(message))
            println("Flushed pending subscription: $type")
        }
        pendingSubscriptions.clear()
    }
    
    private fun parseMessage(text: String): WebSocketEvent? {
        // First, try to determine the message type
        return try {
            when {
                text.contains("\"INITIAL\"") -> {
                    val message = json.decodeFromString<WsInitialMessage>(text)
                    WebSocketEvent.Initial(message.topGainers, message.topLosers)
                }
                text.contains("\"PRICE_UPDATE\"") -> {
                    val message = json.decodeFromString<WsPriceUpdateMessage>(text)
                    WebSocketEvent.PriceUpdate(message.updates)
                }
                text.contains("\"RANKING_UPDATE\"") -> {
                    val message = json.decodeFromString<WsRankingUpdateMessage>(text)
                    WebSocketEvent.RankingUpdate(message.topGainers, message.topLosers)
                }
                text.contains("\"TRADE_RESULT\"") -> {
                    val message = json.decodeFromString<WsTradeResultMessage>(text)
                    WebSocketEvent.TradeResult(message.result)
                }
                text.contains("\"ORDER_RESULT\"") -> {
                    val message = json.decodeFromString<WsOrderResultMessage>(text)
                    WebSocketEvent.OrderResult(message.result)
                }
                else -> {
                    null
                }
            }
        } catch (e: Exception) {
            WebSocketEvent.Error("Failed to parse: ${e.message}")
        }
    }
}
