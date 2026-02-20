package avinash.app.mystocks.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import avinash.app.mystocks.ui.screens.auth.LauncherScreen
import avinash.app.mystocks.ui.screens.auth.SplashScreen
import avinash.app.mystocks.ui.screens.detail.DetailScreen
import avinash.app.mystocks.ui.screens.home.HomeScreen
import avinash.app.mystocks.ui.screens.orders.OrdersScreen
import avinash.app.mystocks.ui.screens.portfolio.PortfolioScreen
import avinash.app.mystocks.ui.screens.trade.TradeScreen
import avinash.app.mystocks.ui.screens.wishlist.WishlistScreen
import avinash.app.mystocks.ui.theme.AppTheme

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Launcher : Screen("launcher")
    object Home : Screen("home")
    object Portfolio : Screen("portfolio")
    object Wishlist : Screen("wishlist")
    object Orders : Screen("orders")
    object Detail : Screen("detail/{symbol}") {
        fun createRoute(symbol: String) = "detail/$symbol"
    }
    object Trade : Screen("trade/{symbol}") {
        fun createRoute(symbol: String) = "trade/$symbol"
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(Screen.Portfolio, "Portfolio", Icons.Filled.PieChart, Icons.Outlined.PieChart),
    BottomNavItem(Screen.Orders, "Orders", Icons.Filled.Receipt, Icons.Outlined.Receipt),
    BottomNavItem(Screen.Wishlist, "Wishlist", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    val showBottomNav = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.Portfolio.route,
        Screen.Orders.route,
        Screen.Wishlist.route
    )
    
    Scaffold(
        containerColor = colorScheme.background,
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = colorScheme.surface
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { 
                            it.route == item.screen.route 
                        } == true
                        
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colorScheme.secondary,
                                selectedTextColor = colorScheme.secondary,
                                unselectedIconColor = ext.textSecondary,
                                unselectedTextColor = ext.textSecondary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashComplete = {
                        navController.navigate(Screen.Launcher.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Launcher.route) {
                LauncherScreen(
                    onAuthenticated = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Launcher.route) { inclusive = true }
                        }
                    }
                )
            }
            
            composable(Screen.Home.route) {
                HomeScreen(
                    onStockClick = { symbol ->
                        navController.navigate(Screen.Detail.createRoute(symbol))
                    }
                )
            }
            
            composable(Screen.Portfolio.route) {
                PortfolioScreen(
                    onStockClick = { symbol ->
                        navController.navigate(Screen.Detail.createRoute(symbol))
                    }
                )
            }
            
            composable(Screen.Orders.route) {
                OrdersScreen()
            }
            
            composable(Screen.Wishlist.route) {
                WishlistScreen(
                    onStockClick = { symbol ->
                        navController.navigate(Screen.Detail.createRoute(symbol))
                    }
                )
            }
            
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("symbol") { type = NavType.StringType })
            ) {
                DetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onTradeClick = { symbol ->
                        navController.navigate(Screen.Trade.createRoute(symbol))
                    },
                    onStockClick = { symbol ->
                        navController.navigate(Screen.Detail.createRoute(symbol))
                    }
                )
            }
            
            composable(
                route = Screen.Trade.route,
                arguments = listOf(navArgument("symbol") { type = NavType.StringType })
            ) {
                TradeScreen(
                    onBackClick = { navController.popBackStack() },
                    onTradeComplete = { navController.popBackStack() }
                )
            }
        }
    }
}
