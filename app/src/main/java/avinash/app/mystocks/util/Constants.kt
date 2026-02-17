package avinash.app.mystocks.util

object Constants {
    // Server Configuration
    // For Android Emulator use 10.0.2.2, for physical device use actual IP
    const val BASE_URL = "http://10.0.2.2:8080"
    const val WS_URL = "ws://10.0.2.2:8080/ws/stocks"
    
    // API Endpoints
    const val API_HOME = "/api/home"
    const val API_STOCKS = "/api/stocks"
    const val API_PORTFOLIO = "/api/portfolio"
    const val API_TRADE = "/api/trade"
    
    // Authentication
    const val CORRECT_PIN = "1111"
    const val MAX_BIOMETRIC_ATTEMPTS = 3
    
    // User ID (hardcoded for demo)
    const val USER_ID = "user123"
    
    // Splash delay
    const val SPLASH_DELAY_MS = 1500L
}
