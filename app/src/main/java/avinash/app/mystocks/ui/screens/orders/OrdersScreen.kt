package avinash.app.mystocks.ui.screens.orders

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import avinash.app.mystocks.ui.components.PendingOrderItem
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Text(
            text = "Orders",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        FilterChipRow(
            selectedFilter = uiState.filter,
            onFilterSelected = viewModel::setFilter,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorScheme.secondary)
            }
        } else if (uiState.filteredOrders.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = ext.textSecondary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No orders yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = ext.textSecondary
                    )
                    Text(
                        text = "Your orders will appear here",
                        fontSize = 14.sp,
                        color = ext.textSecondary.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = uiState.filteredOrders,
                    key = { it.orderId }
                ) { order ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        PendingOrderItem(
                            order = order,
                            onDismiss = if (!order.isPending) {
                                { viewModel.dismissOrder(order.orderId) }
                            } else null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChipRow(
    selectedFilter: OrderFilter,
    onFilterSelected: (OrderFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OrderFilter.entries.forEach { filter ->
            val selected = filter == selectedFilter
            FilterChip(
                selected = selected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = when (filter) {
                            OrderFilter.ALL -> "All"
                            OrderFilter.PENDING -> "Pending"
                            OrderFilter.FAILED -> "Failed"
                        },
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colorScheme.secondary,
                    selectedLabelColor = colorScheme.onSecondary,
                    containerColor = ext.cardBackground,
                    labelColor = ext.textSecondary
                ),
                shape = RoundedCornerShape(20.dp),
                border = if (!selected) FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = colorScheme.outline.copy(alpha = 0.3f)
                ) else null
            )
        }
    }
}
