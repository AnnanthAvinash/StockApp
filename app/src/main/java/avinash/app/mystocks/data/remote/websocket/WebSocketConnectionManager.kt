package avinash.app.mystocks.data.remote.websocket

import avinash.app.mystocks.data.cache.StockCacheDataSource
import avinash.app.mystocks.data.remote.dto.StockUpdateDto
import avinash.app.mystocks.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketConnectionManager @Inject constructor(
    private val socketClient: StockSocketClient,
    private val cache: StockCacheDataSource
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var wsJob: Job? = null

    private val _priceUpdates = MutableSharedFlow<List<StockUpdateDto>>(extraBufferCapacity = 64)
    val priceUpdates: SharedFlow<List<StockUpdateDto>> = _priceUpdates.asSharedFlow()

    fun connect() {
        if (wsJob?.isActive == true) return
        wsJob = scope.launch {
            socketClient.connect(Constants.USER_ID).collect { event ->
                when (event) {
                    is WebSocketEvent.PriceUpdate -> {
                        cache.updatePrices(event.updates)
                        _priceUpdates.emit(event.updates)
                    }
                    else -> {}
                }
            }
        }
    }

    fun disconnect() {
        wsJob?.cancel()
        wsJob = null
        socketClient.disconnect()
    }

    fun subscribe(type: String) = socketClient.subscribe(type)
    fun unsubscribe(type: String) = socketClient.unsubscribe(type)
}
