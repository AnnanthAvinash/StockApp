package avinash.app.mystocks.ui.screens.home

import avinash.app.mystocks.domain.model.HomeData
import avinash.app.mystocks.domain.model.RecentStock

data class HomeState(
    val isLoading: Boolean = true,
    val homeData: HomeData = HomeData(),
    val recentStocks: List<RecentStock> = emptyList()
)

sealed class HomeIntent {
    object Retry : HomeIntent()
    object ScreenVisible : HomeIntent()
}

sealed class HomeEffect {
    data class ShowError(val message: String) : HomeEffect()
    data class ShowNoInternet(val message: String = "No internet connection") : HomeEffect()
}
