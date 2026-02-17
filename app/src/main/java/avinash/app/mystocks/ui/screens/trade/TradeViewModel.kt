package avinash.app.mystocks.ui.screens.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.repository.PortfolioRepository
import avinash.app.mystocks.data.repository.StockRepository
import avinash.app.mystocks.domain.model.Holding
import avinash.app.mystocks.domain.model.OrderStatus
import avinash.app.mystocks.domain.model.PendingOrder
import avinash.app.mystocks.domain.model.Stock
import avinash.app.mystocks.domain.model.TradeAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TradeUiState(
    val stock: Stock? = null,
    val holding: Holding? = null,
    val action: TradeAction = TradeAction.BUY,
    val quantity: Int = 1,
    val isLoading: Boolean = false,
    val orderPlaced: Boolean = false,
    val pendingOrder: PendingOrder? = null,
    val error: String? = null
) {
    val totalValue: Double
        get() = (stock?.currentPrice ?: 0.0) * quantity
    
    val maxSellQuantity: Int
        get() = holding?.quantity ?: 0
    
    val canSell: Boolean
        get() = action == TradeAction.SELL && quantity <= maxSellQuantity
    
    val canTrade: Boolean
        get() = quantity > 0 && (action == TradeAction.BUY || canSell)
}

@HiltViewModel
class TradeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {
    
    private val symbol: String = savedStateHandle.get<String>("symbol") ?: ""
    
    private val _uiState = MutableStateFlow(TradeUiState())
    val uiState: StateFlow<TradeUiState> = _uiState.asStateFlow()
    
    init {
        if (symbol.isNotEmpty()) {
            observeStock()
            observeHolding()
        }
    }
    
    private fun observeStock() {
        viewModelScope.launch {
            stockRepository.getStockFlow(symbol).collect { stock ->
                _uiState.update { it.copy(stock = stock) }
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
    
    fun setAction(action: TradeAction) {
        _uiState.update { it.copy(action = action, quantity = 1) }
    }
    
    fun setQuantity(quantity: Int) {
        if (quantity >= 1) {
            _uiState.update { it.copy(quantity = quantity) }
        }
    }
    
    fun incrementQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }
    
    fun decrementQuantity() {
        if (_uiState.value.quantity > 1) {
            _uiState.update { it.copy(quantity = it.quantity - 1) }
        }
    }
    
    fun executeTrade() {
        val state = _uiState.value
        if (!state.canTrade) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            portfolioRepository.executeTrade(
                symbol = symbol,
                quantity = state.quantity,
                action = state.action
            ).onSuccess { pendingOrder ->
                // Order placed successfully (pending)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        orderPlaced = true,
                        pendingOrder = pendingOrder,
                        error = if (pendingOrder.status == OrderStatus.FAILED) pendingOrder.message else null
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    fun resetTradeState() {
        _uiState.update { it.copy(orderPlaced = false, pendingOrder = null, error = null) }
    }
}
