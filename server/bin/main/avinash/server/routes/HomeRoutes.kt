package avinash.server.routes

import avinash.server.data.HomeCategories
import avinash.server.data.StockManager
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.homeRoutes(stockManager: StockManager) {
    
    route("/api/home") {
        
        // GET /api/home - Returns home screen data with unique stocks per category
        get {
            val categories: HomeCategories = stockManager.getHomeCategories()
            call.respond(categories)
        }
    }
}
