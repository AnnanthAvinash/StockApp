package avinash.app.mystocks.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.remote.websocket.SubscriptionManager
import avinash.app.mystocks.data.repository.HomeRepository
import avinash.app.mystocks.data.repository.StockRepository
import avinash.app.mystocks.domain.model.RecentStock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val stockRepository: StockRepository,
    private val subscriptionManager: SubscriptionManager
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect: Flow<HomeEffect> = _effect.receiveAsFlow()

    init {
        wireRepoToState()
        observeRecentStocks()
        loadInitialData()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Retry -> loadInitialData()
            HomeIntent.ScreenVisible -> {
                subscriptionManager.subscribeTab("HOME")
            }
        }
    }

    private fun wireRepoToState() {
        viewModelScope.launch {
            homeRepository.homeData.collect { data ->
                _state.update { it.copy(homeData = data, isLoading = false) }
            }
        }
    }

    private fun observeRecentStocks() {
        viewModelScope.launch {
            stockRepository.recentViewedItems.collect { entities ->
                val recent = entities.map { e ->
                    RecentStock(
                        symbol = e.symbol,
                        name = e.name,
                        logoUrl = e.logoUrl
                    )
                }
                _state.update { it.copy(recentStocks = recent) }
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            homeRepository.fetchHomeData()
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(HomeEffect.ShowError(e.message ?: "Something went wrong"))
                }
        }
    }

}
