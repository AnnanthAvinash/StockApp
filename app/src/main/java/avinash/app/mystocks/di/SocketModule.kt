package avinash.app.mystocks.di

import avinash.app.mystocks.data.remote.websocket.StockSocketClient
import avinash.app.mystocks.data.remote.websocket.StockWebSocket
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SocketModule {

    @Binds
    @Singleton
    abstract fun bindSocketClient(impl: StockWebSocket): StockSocketClient
}
