package avinash.app.mystocks.data.remote.websocket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StockSocketClient {
    val connectionState: StateFlow<Boolean>
    fun connect(userId: String? = null): Flow<WebSocketEvent>
    fun disconnect()
    fun subscribe(type: String)
    fun unsubscribe(type: String)
}
