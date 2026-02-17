package avinash.app.mystocks.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.remote.websocket.SubscriptionManager
import avinash.app.mystocks.data.repository.PortfolioRepository
import avinash.app.mystocks.domain.model.PendingOrder
import avinash.app.mystocks.domain.model.Portfolio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PortfolioUiState(
    val isLoading: Boolean = false,
    val portfolio: Portfolio = Portfolio(emptyList()),
    val pendingOrders: List<PendingOrder> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val portfolioRepository: PortfolioRepository,
    private val subscriptionManager: SubscriptionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PortfolioUiState())
    val uiState: StateFlow<PortfolioUiState> = _uiState.asStateFlow()
    
    init {
        observePortfolio()
        observePendingOrders()
    }
    
    private fun observePortfolio() {
        viewModelScope.launch {
            portfolioRepository.portfolio.collect { portfolio ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        portfolio = portfolio
                    )
                }
            }
        }
    }
    
    private fun observePendingOrders() {
        viewModelScope.launch {
            portfolioRepository.pendingOrders.collect { orders ->
                _uiState.update { it.copy(pendingOrders = orders) }
            }
        }
    }
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            portfolioRepository.fetchPortfolio()
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }
    
    fun dismissPendingOrder(orderId: String) {
        viewModelScope.launch {
            portfolioRepository.removePendingOrder(orderId)
        }
    }
    
    fun clearCompletedOrders() {
        viewModelScope.launch {
            portfolioRepository.clearCompletedOrders()
        }
    }
    
    fun onScreenVisible() {
        subscriptionManager.subscribeTab("PORTFOLIO")
    }
}
