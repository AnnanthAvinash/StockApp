package avinash.server.config

object ServerConfig {
    const val PORT = 8080
    const val HOST = "0.0.0.0"
    
    // Stock Settings
    const val STOCK_COUNT = 50
    const val PRICE_UPDATE_INTERVAL_MS = 1000L
    const val STOCKS_PER_UPDATE = 15
    const val HISTORICAL_POINTS = 100
    
    // Price Change Range
    const val MIN_PRICE_CHANGE_PERCENT = -3.0
    const val MAX_PRICE_CHANGE_PERCENT = 3.0
    
    // Top Lists
    const val TOP_GAINERS_COUNT = 3
    const val TOP_LOSERS_COUNT = 3
    const val MOST_BUY_COUNT = 3
    const val MOST_SELL_COUNT = 3
}
