package avinash.app.mystocks.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import avinash.app.mystocks.data.remote.websocket.SubscriptionManager
import avinash.app.mystocks.data.remote.websocket.WebSocketConnectionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLifecycleObserver @Inject constructor(
    private val wsConnectionManager: WebSocketConnectionManager,
    private val subscriptionManager: SubscriptionManager
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        wsConnectionManager.connect()
    }

    override fun onStop(owner: LifecycleOwner) {
        subscriptionManager.clear()
        wsConnectionManager.disconnect()
    }
}
