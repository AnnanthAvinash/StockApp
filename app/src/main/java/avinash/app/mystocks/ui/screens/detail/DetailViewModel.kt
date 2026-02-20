package avinash.app.mystocks.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.remote.websocket.SubscriptionManager
import avinash.app.mystocks.data.repository.PortfolioRepository
import avinash.app.mystocks.data.repository.StockRepository
import avinash.app.mystocks.domain.model.Holding
import avinash.app.mystocks.domain.model.PricePoint
import avinash.app.mystocks.domain.model.RecentStock
import avinash.app.mystocks.domain.model.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val stock: Stock? = null,
    val holding: Holding? = null,
    val recentStocks: List<RecentStock> = emptyList(),
    val isWishlisted: Boolean = false,
    val pricePoints: List<PricePoint> = emptyList(),
    val selectedPeriod: String = "1D",
    val isChartLoading: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val subscriptionManager: SubscriptionManager
) : ViewModel() {
    
    val symbol: String = savedStateHandle.get<String>("symbol") ?: ""
    
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    
    init {
        if (symbol.isNotEmpty()) {
            fetchDetail()
            observeStock()
            observeWishlist()
            observeHolding()
            observePriceForChart()
            observeRecentStocks()
        }
    }
    
    private fun fetchDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            stockRepository.fetchStockDetail(symbol)
                .onSuccess {
                    addToRecentViewed()
                    loadChartData("1D")
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
    
    private fun observeStock() {
        viewModelScope.launch {
            stockRepository.observeStock(symbol).collect { stock ->
                if (stock != null) {
                    _uiState.update { it.copy(stock = stock, isLoading = false) }
                }
            }
        }
    }
    
    private fun observePriceForChart() {
        viewModelScope.launch {
            stockRepository.observeStock(symbol)
                .filterNotNull()
                .collect { stock ->
                    val current = _uiState.value
                    if (current.selectedPeriod == "1D" && current.pricePoints.isNotEmpty()) {
                        val newPoint = PricePoint(System.currentTimeMillis(), stock.currentPrice)
                        _uiState.update { it.copy(pricePoints = it.pricePoints + newPoint) }
                    }
                }
        }
    }
    
    fun loadChartData(period: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(selectedPeriod = period, isChartLoading = true) }
            stockRepository.fetchHistory(symbol, period)
                .onSuccess { points ->
                    _uiState.update { it.copy(pricePoints = points, isChartLoading = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isChartLoading = false) }
                }
        }
    }
    
    private fun observeWishlist() {
        viewModelScope.launch {
            stockRepository.isInWishlist(symbol).collect { isWishlisted ->
                _uiState.update { it.copy(isWishlisted = isWishlisted) }
            }
        }
    }
    
    private fun observeHolding() {
        viewModelScope.launch {
            portfolioRepository.getHolding(symbol).collect { holding ->
                _uiState.update { it.copy(holding = holding) }
            }
        }
    }
    
    private fun addToRecentViewed() {
        viewModelScope.launch {
            stockRepository.addToRecentViewed(symbol)
        }
    }
    
    private fun observeRecentStocks() {
        viewModelScope.launch {
            stockRepository.recentViewedItems.collect { entities ->
                val recent = entities
                    .filter { it.symbol != symbol }
                    .map { RecentStock(symbol = it.symbol, name = it.name, logoUrl = it.logoUrl) }
                _uiState.update { it.copy(recentStocks = recent) }
            }
        }
    }
    
    fun toggleWishlist() {
        viewModelScope.launch {
            stockRepository.toggleWishlist(symbol)
        }
    }
    
    fun onScreenVisible() {
        if (symbol.isNotEmpty()) {
            subscriptionManager.subscribeScreen("SYMBOL:$symbol")
        }
    }

    fun onScreenHidden() {
        subscriptionManager.exit()
    }
}
