package avinash.app.mystocks.ui.screens.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.remote.websocket.SubscriptionManager
import avinash.app.mystocks.data.repository.StockRepository
import avinash.app.mystocks.domain.model.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WishlistUiState(
    val stocks: List<Stock> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val stockRepository: StockRepository,
    private val subscriptionManager: SubscriptionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()
    
    init {
        observeWishlist()
    }
    
    private fun observeWishlist() {
        viewModelScope.launch {
            stockRepository.wishlistStocks.collect { stocks ->
                _uiState.update { it.copy(stocks = stocks, isLoading = false) }
            }
        }
    }
    
    fun removeFromWishlist(symbol: String) {
        viewModelScope.launch {
            stockRepository.removeFromWishlist(symbol)
        }
    }
    
    fun onScreenVisible() {
        subscriptionManager.subscribeTab("WISHLIST")
    }
}
