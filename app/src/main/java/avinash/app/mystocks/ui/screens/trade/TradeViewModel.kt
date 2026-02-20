package avinash.app.mystocks.ui.screens.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.remote.api.StockApi
import avinash.app.mystocks.data.repository.PortfolioRepository
import avinash.app.mystocks.data.repository.StockRepository
import avinash.app.mystocks.domain.model.Holding
import avinash.app.mystocks.domain.model.OrderStatus
import avinash.app.mystocks.domain.model.PendingOrder
import avinash.app.mystocks.domain.model.Stock
import avinash.app.mystocks.domain.model.TradeAction
import avinash.app.mystocks.util.Constants
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
    val error: String? = null,
    val availableBalance: Double = 0.0
) {
    val totalValue: Double
        get() = (stock?.currentPrice ?: 0.0) * quantity

    val maxSellQuantity: Int
        get() = holding?.quantity ?: 0

    val validationError: String?
        get() = when {
            quantity < 1 -> "Quantity must be at least 1"
            action == TradeAction.BUY && totalValue > availableBalance -> "Insufficient balance"
            action == TradeAction.SELL && quantity > maxSellQuantity -> "Not enough shares to sell"
            else -> null
        }

    val canConfirm: Boolean
        get() = quantity >= 1 &&
            (action == TradeAction.BUY && totalValue <= availableBalance ||
             action == TradeAction.SELL && quantity <= maxSellQuantity)

    val canDecrement: Boolean
        get() = quantity > 1

    val canIncrement: Boolean
        get() = when (action) {
            TradeAction.BUY -> (stock?.currentPrice ?: 0.0) * (quantity + 1) <= availableBalance
            TradeAction.SELL -> quantity + 1 <= maxSellQuantity
        }
}

@HiltViewModel
class TradeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val stockApi: StockApi
) : ViewModel() {

    private val symbol: String = savedStateHandle.get<String>("symbol") ?: ""

    private val _uiState = MutableStateFlow(TradeUiState())
    val uiState: StateFlow<TradeUiState> = _uiState.asStateFlow()

    init {
        if (symbol.isNotEmpty()) {
            observeStock()
            observeHolding()
            observeBalance()
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

    private fun observeBalance() {
        viewModelScope.launch {
            try {
                val wallet = stockApi.getWallet(Constants.USER_ID)
                _uiState.update { it.copy(availableBalance = wallet.balance) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to load balance") }
            }
        }
    }

    fun setAction(action: TradeAction) {
        _uiState.update { it.copy(action = action, quantity = 1, error = null) }
    }

    fun setQuantity(quantity: Int) {
        if (quantity >= 1) {
            _uiState.update { it.copy(quantity = quantity, error = null) }
        }
    }

    fun incrementQuantity() {
        val state = _uiState.value
        if (state.canIncrement) {
            _uiState.update { it.copy(quantity = it.quantity + 1, error = null) }
        }
    }

    fun decrementQuantity() {
        if (_uiState.value.canDecrement) {
            _uiState.update { it.copy(quantity = it.quantity - 1, error = null) }
        }
    }

    fun executeTrade() {
        val state = _uiState.value
        if (!state.canConfirm || state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            portfolioRepository.executeTrade(
                symbol = symbol,
                quantity = state.quantity,
                action = state.action
            ).onSuccess { pendingOrder ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        orderPlaced = true,
                        pendingOrder = pendingOrder,
                        availableBalance = pendingOrder.updatedWalletBalance ?: it.availableBalance,
                        error = if (pendingOrder.status == OrderStatus.FAILED) pendingOrder.message else null,
                        quantity = 1
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    fun resetOrderPlaced() {
        _uiState.update { it.copy(orderPlaced = false, pendingOrder = null) }
    }

    fun resetTradeState() {
        _uiState.update { it.copy(orderPlaced = false, pendingOrder = null, error = null) }
    }
}
