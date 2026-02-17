package avinash.server.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class WsMessage {
    abstract val type: String
}

@Serializable
@SerialName("INITIAL")
data class InitialMessage(
    override val type: String = "INITIAL",
    val topGainers: List<Stock>,
    val topLosers: List<Stock>
) : WsMessage()

@Serializable
@SerialName("PRICE_UPDATE")
data class PriceUpdateMessage(
    override val type: String = "PRICE_UPDATE",
    val updates: List<StockUpdate>,
    val timestamp: Long = System.currentTimeMillis()
) : WsMessage()

@Serializable
data class StockUpdate(
    val symbol: String,
    val currentPrice: Double,
    val previousPrice: Double,
    val change: Double,
    val changePercent: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val volume: Long
)

@Serializable
@SerialName("RANKING_UPDATE")
data class RankingUpdateMessage(
    override val type: String = "RANKING_UPDATE",
    val topGainers: List<Stock>,
    val topLosers: List<Stock>
) : WsMessage()

@Serializable
@SerialName("TRADE_RESULT")
data class TradeResultMessage(
    override val type: String = "TRADE_RESULT",
    val result: TradeResult
) : WsMessage()

@Serializable
@SerialName("ORDER_RESULT")
data class OrderResultMessage(
    override val type: String = "ORDER_RESULT",
    val result: OrderResult
) : WsMessage()

// Client -> Server subscription messages
@Serializable
data class SubscriptionMessage(
    val action: String,  // "SUBSCRIBE" or "UNSUBSCRIBE"
    val type: String     // "HOME", "PORTFOLIO", "WISHLIST", "SYMBOL:AAPL"
)
