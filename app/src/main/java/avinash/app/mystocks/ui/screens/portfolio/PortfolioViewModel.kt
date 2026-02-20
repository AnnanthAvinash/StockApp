package avinash.app.mystocks.ui.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.remote.websocket.SubscriptionManager
import avinash.app.mystocks.data.repository.PortfolioRepository
import avinash.app.mystocks.domain.model.Portfolio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PortfolioUiState(
    val isLoading: Boolean = true,
    val portfolio: Portfolio = Portfolio(emptyList()),
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
    
    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            portfolioRepository.fetchPortfolio()
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
        }
    }
    
    fun onScreenVisible() {
        subscriptionManager.subscribeTab("PORTFOLIO")
        refresh()
    }
}
