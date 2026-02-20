package avinash.app.mystocks.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StockDto(
    val symbol: String,
    val name: String,
    val logoUrl: String,
    val currentPrice: Double,
    val previousPrice: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val volume: Long,
    val openPrice: Double
)

@Serializable
data class HomeResponse(
    val topGainers: List<StockDto>,
    val topLosers: List<StockDto>,
    val mostBought: List<StockDto>,
    val mostSold: List<StockDto>
)

@Serializable
data class StockDetailDto(
    val stock: StockDto,
    val historicalData: List<PricePointDto>
)

@Serializable
data class PricePointDto(
    val timestamp: Long,
    val price: Double
)

@Serializable
data class PortfolioDto(
    val userId: String,
    val holdings: List<HoldingDto>
)

@Serializable
data class HoldingDto(
    val symbol: String,
    val quantity: Int,
    val averagePrice: Double,
    val totalInvested: Double
)

@Serializable
data class TradeRequestDto(
    val userId: String,
    val symbol: String,
    val quantity: Int,
    val action: String // "BUY" or "SELL"
)

@Serializable
data class TradeAcknowledgmentDto(
    val acknowledged: Boolean,
    val message: String,
    val success: Boolean,
    val tradeMessage: String
)

// Pending Order Response (from POST /api/trade)
@Serializable
data class PendingOrderResponseDto(
    val orderId: String,
    val symbol: String,
    val action: String,
    val quantity: Int,
    val priceAtOrder: Double,
    val status: String, // "SUCCESS", "FAILED"
    val message: String,
    val updatedWalletBalance: Double? = null
)

// Order Result (from WebSocket)
@Serializable
data class OrderResultDto(
    val orderId: String,
    val userId: String,
    val symbol: String,
    val action: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalValue: Double,
    val status: String, // "SUCCESS", "FAILED", "CANCELED"
    val message: String,
    val holding: HoldingDto? = null,
    val updatedWalletBalance: Double? = null,
    val timestamp: Long
)

// WebSocket Messages
@Serializable
data class WsInitialMessage(
    val type: String,
    val topGainers: List<StockDto>,
    val topLosers: List<StockDto>
)

@Serializable
data class WsPriceUpdateMessage(
    val type: String,
    val updates: List<StockUpdateDto>,
    val timestamp: Long
)

@Serializable
data class StockUpdateDto(
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
data class WsRankingUpdateMessage(
    val type: String,
    val topGainers: List<StockDto>,
    val topLosers: List<StockDto>
)

@Serializable
data class WsTradeResultMessage(
    val type: String,
    val result: TradeResultDto
)

@Serializable
data class TradeResultDto(
    val success: Boolean,
    val message: String,
    val userId: String,
    val symbol: String,
    val action: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalValue: Double,
    val holding: HoldingDto? = null,
    val timestamp: Long
)

@Serializable
data class WsOrderResultMessage(
    val type: String,
    val result: OrderResultDto
)

@Serializable
data class WalletResponseDto(
    val balance: Double
)
