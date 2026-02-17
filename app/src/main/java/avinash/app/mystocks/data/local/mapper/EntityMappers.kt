package avinash.app.mystocks.data.local.mapper

import avinash.app.mystocks.data.local.entity.*
import avinash.app.mystocks.data.remote.dto.*
import avinash.app.mystocks.domain.model.*
import avinash.app.mystocks.domain.model.HomeData

// Stock Mappers
fun StockDto.toEntity(): StockEntity = StockEntity(
    symbol = symbol,
    name = name,
    logoUrl = logoUrl,
    currentPrice = currentPrice,
    previousPrice = previousPrice,
    dayHigh = dayHigh,
    dayLow = dayLow,
    volume = volume,
    openPrice = openPrice
)

fun StockEntity.toDomain(): Stock = Stock(
    symbol = symbol,
    name = name,
    logoUrl = logoUrl,
    currentPrice = currentPrice,
    previousPrice = previousPrice,
    dayHigh = dayHigh,
    dayLow = dayLow,
    volume = volume,
    openPrice = openPrice
)

fun StockDto.toDomain(): Stock = Stock(
    symbol = symbol,
    name = name,
    logoUrl = logoUrl,
    currentPrice = currentPrice,
    previousPrice = previousPrice,
    dayHigh = dayHigh,
    dayLow = dayLow,
    volume = volume,
    openPrice = openPrice
)

fun List<StockDto>.toStockEntities(): List<StockEntity> = map { it.toEntity() }
fun List<StockEntity>.toDomainList(): List<Stock> = map { it.toDomain() }

fun List<StockDto>.toDtoStockList(): List<Stock> = map { it.toDomain() }

// Home Response Mapper
fun HomeResponse.toDomain(): HomeData = HomeData(
    topGainers = topGainers.toDtoStockList(),
    topLosers = topLosers.toDtoStockList(),
    mostBought = mostBought.toDtoStockList(),
    mostSold = mostSold.toDtoStockList()
)

// Stock Detail Mappers
fun StockDetailDto.toDomain(): StockDetail = StockDetail(
    stock = stock.toDomain(),
    historicalData = historicalData.map { PricePoint(it.timestamp, it.price) }
)

// Holding Mappers
fun HoldingDto.toEntity(): PortfolioEntity = PortfolioEntity(
    symbol = symbol,
    quantity = quantity,
    averagePrice = averagePrice,
    totalInvested = totalInvested
)

fun PortfolioEntity.toDomain(stock: Stock?): Holding = Holding(
    symbol = symbol,
    name = stock?.name ?: symbol,
    logoUrl = stock?.logoUrl ?: "",
    quantity = quantity,
    averagePrice = averagePrice,
    totalInvested = totalInvested,
    currentPrice = stock?.currentPrice ?: averagePrice
)

fun List<HoldingDto>.toPortfolioEntities(): List<PortfolioEntity> = map { it.toEntity() }

// Trade Result Mapper
fun TradeResultDto.toDomain(stock: Stock?): TradeResult = TradeResult(
    success = success,
    message = message,
    symbol = symbol,
    action = if (action == "BUY") TradeAction.BUY else TradeAction.SELL,
    quantity = quantity,
    pricePerUnit = pricePerUnit,
    totalValue = totalValue,
    updatedHolding = holding?.let {
        Holding(
            symbol = it.symbol,
            name = stock?.name ?: it.symbol,
            logoUrl = stock?.logoUrl ?: "",
            quantity = it.quantity,
            averagePrice = it.averagePrice,
            totalInvested = it.totalInvested,
            currentPrice = stock?.currentPrice ?: it.averagePrice
        )
    }
)

// Stock Update Mapper
fun StockUpdateDto.applyToEntity(entity: StockEntity): StockEntity = entity.copy(
    currentPrice = currentPrice,
    previousPrice = previousPrice,
    dayHigh = dayHigh,
    dayLow = dayLow,
    volume = volume,
    lastUpdated = System.currentTimeMillis()
)

// Pending Order Mappers
fun PendingOrderResponseDto.toEntity(): PendingOrderEntity = PendingOrderEntity(
    orderId = orderId,
    symbol = symbol,
    action = action,
    quantity = quantity,
    priceAtOrder = priceAtOrder,
    status = status,
    message = message
)

fun PendingOrderResponseDto.toDomain(): PendingOrder = PendingOrder(
    orderId = orderId,
    symbol = symbol,
    action = if (action == "BUY") TradeAction.BUY else TradeAction.SELL,
    quantity = quantity,
    priceAtOrder = priceAtOrder,
    status = OrderStatus.fromString(status),
    createdAt = System.currentTimeMillis(),
    message = message
)

fun PendingOrderEntity.toDomain(): PendingOrder = PendingOrder(
    orderId = orderId,
    symbol = symbol,
    action = if (action == "BUY") TradeAction.BUY else TradeAction.SELL,
    quantity = quantity,
    priceAtOrder = priceAtOrder,
    status = OrderStatus.fromString(status),
    createdAt = createdAt,
    message = message
)

fun PendingOrder.toEntity(): PendingOrderEntity = PendingOrderEntity(
    orderId = orderId,
    symbol = symbol,
    action = action.name,
    quantity = quantity,
    priceAtOrder = priceAtOrder,
    status = status.name,
    createdAt = createdAt,
    message = message
)

fun List<PendingOrderEntity>.toPendingOrderList(): List<PendingOrder> = map { it.toDomain() }

// Order Result Mapper (from WebSocket)
fun OrderResultDto.toDomain(): PendingOrder = PendingOrder(
    orderId = orderId,
    symbol = symbol,
    action = if (action == "BUY") TradeAction.BUY else TradeAction.SELL,
    quantity = quantity,
    priceAtOrder = pricePerUnit,
    status = OrderStatus.fromString(status),
    createdAt = timestamp,
    message = message
)

fun OrderResultDto.toHoldingEntity(): PortfolioEntity? = holding?.let {
    PortfolioEntity(
        symbol = it.symbol,
        quantity = it.quantity,
        averagePrice = it.averagePrice,
        totalInvested = it.totalInvested
    )
}
