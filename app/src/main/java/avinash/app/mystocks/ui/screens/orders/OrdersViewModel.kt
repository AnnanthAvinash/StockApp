package avinash.app.mystocks.ui.screens.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import avinash.app.mystocks.data.repository.PortfolioRepository
import avinash.app.mystocks.domain.model.PendingOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OrderFilter { ALL, PENDING, FAILED }

data class OrdersUiState(
    val orders: List<PendingOrder> = emptyList(),
    val filter: OrderFilter = OrderFilter.ALL,
    val isLoading: Boolean = true
) {
    val filteredOrders: List<PendingOrder>
        get() = when (filter) {
            OrderFilter.ALL -> orders
            OrderFilter.PENDING -> orders.filter { it.isPending }
            OrderFilter.FAILED -> orders.filter { it.isFailed || it.isCanceled }
        }
}

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        cleanupOldOrders()
        observeOrders()
    }

    private fun cleanupOldOrders() {
        viewModelScope.launch {
            portfolioRepository.cleanupExpiredOrders()
        }
    }

    private fun observeOrders() {
        viewModelScope.launch {
            portfolioRepository.allOrders.collect { orders ->
                _uiState.update {
                    it.copy(
                        orders = orders.sortedByDescending { o -> o.createdAt },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun setFilter(filter: OrderFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun dismissOrder(orderId: String) {
        viewModelScope.launch {
            portfolioRepository.removePendingOrder(orderId)
        }
    }
}
