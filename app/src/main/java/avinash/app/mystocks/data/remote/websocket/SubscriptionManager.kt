package avinash.app.mystocks.data.remote.websocket

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionManager @Inject constructor(
    private val wsConnectionManager: WebSocketConnectionManager
) {
    private val currentSubs = mutableSetOf<String>()

    fun subscribeTab(type: String) {
        currentSubs.add(type)
        wsConnectionManager.subscribe(type)
    }

    fun subscribeScreen(type: String) {
        currentSubs.forEach { wsConnectionManager.unsubscribe(it) }
        currentSubs.clear()
        currentSubs.add(type)
        wsConnectionManager.subscribe(type)
    }

    fun exit() {
        currentSubs.forEach { wsConnectionManager.unsubscribe(it) }
        currentSubs.clear()
    }

    fun clear() {
        currentSubs.clear()
    }
}
