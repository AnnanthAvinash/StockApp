package avinash.app.mystocks.ui.screens.home

import avinash.app.mystocks.domain.model.HomeData

// State — single source of truth for HomeScreen UI
data class HomeState(
    val isLoading: Boolean = true,
    val homeData: HomeData = HomeData()
)

// Intent — user actions
sealed class HomeIntent {
    object Retry : HomeIntent()
    object ScreenVisible : HomeIntent()
}

// Effect — one-shot events, never replayed on config change
sealed class HomeEffect {
    data class ShowError(val message: String) : HomeEffect()
    data class ShowNoInternet(val message: String = "No internet connection") : HomeEffect()
}
