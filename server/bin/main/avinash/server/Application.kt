package avinash.server

import avinash.server.config.ServerConfig
import avinash.server.data.PriceEngine
import avinash.server.data.StockManager
import avinash.server.data.WebSocketManager
import avinash.server.routes.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.Duration

fun main() {
    embeddedServer(
        Netty,
        port = ServerConfig.PORT,
        host = ServerConfig.HOST,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    // JSON configuration
    val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    // Initialize managers
    val webSocketManager = WebSocketManager(json)
    val stockManager = StockManager(webSocketManager)
    val priceEngine = PriceEngine(stockManager, webSocketManager)
    
    // Install plugins
    install(ContentNegotiation) {
        json(json)
    }
    
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
    }
    
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.message ?: "Unknown error"))
            )
        }
    }
    
    // Configure routing
    routing {
        // Health check
        get("/health") {
            call.respond(mapOf(
                "status" to "healthy",
                "clients" to webSocketManager.getClientCount(),
                "stocks" to stockManager.getAllStocks().size
            ))
        }
        
        // API routes
        homeRoutes(stockManager)
        stockRoutes(stockManager)
        portfolioRoutes(stockManager)
        chartRoutes(stockManager)
        webSocketRoutes(stockManager, webSocketManager, json)
    }
    
    // Start price engine
    launch {
        priceEngine.start(this)
    }
    
    // Log startup
    environment.log.info("=".repeat(50))
    environment.log.info("Stock Server started")
    environment.log.info("HTTP:      http://${ServerConfig.HOST}:${ServerConfig.PORT}")
    environment.log.info("WebSocket: ws://${ServerConfig.HOST}:${ServerConfig.PORT}/ws/stocks")
    environment.log.info("Stocks:    ${stockManager.getAllStocks().size}")
    environment.log.info("=".repeat(50))
}
