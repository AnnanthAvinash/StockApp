package avinash.app.mystocks.ui.screens.portfolio

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import avinash.app.mystocks.domain.model.Holding
import avinash.app.mystocks.domain.model.PendingOrder
import avinash.app.mystocks.ui.components.ChangeText
import avinash.app.mystocks.ui.components.LoadingIndicator
import avinash.app.mystocks.ui.components.PendingOrderItem
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isPendingOrdersExpanded by remember { mutableStateOf(true) }
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    LaunchedEffect(Unit) {
        viewModel.onScreenVisible()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        if (uiState.isLoading) {
            LoadingIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "Portfolio",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                item {
                    PortfolioSummaryCard(
                        totalInvested = uiState.portfolio.totalInvested,
                        currentValue = uiState.portfolio.totalCurrentValue,
                        profitLoss = uiState.portfolio.totalProfitLoss,
                        profitLossPercent = uiState.portfolio.totalProfitLossPercent
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                if (uiState.pendingOrders.isNotEmpty()) {
                    item {
                        PendingOrdersHeader(
                            count = uiState.pendingOrders.size,
                            isExpanded = isPendingOrdersExpanded,
                            onToggle = { isPendingOrdersExpanded = !isPendingOrdersExpanded }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    if (isPendingOrdersExpanded) {
                        items(
                            items = uiState.pendingOrders,
                            key = { it.orderId }
                        ) { order ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                PendingOrderItem(
                                    order = order,
                                    onDismiss = { viewModel.dismissPendingOrder(order.orderId) }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                
                item {
                    Text(
                        text = "Holdings (${uiState.portfolio.holdings.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                if (uiState.portfolio.holdings.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No holdings yet. Start trading!",
                                    color = ext.textSecondary
                                )
                            }
                        }
                    }
                } else {
                    items(uiState.portfolio.holdings) { holding ->
                        HoldingItem(
                            holding = holding,
                            onClick = { onStockClick(holding.symbol) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PendingOrdersHeader(
    count: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ext.surfaceElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = ext.warning,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "Pending Orders",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ext.warning.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ext.warning
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = ext.textSecondary
            )
        }
    }
}

@Composable
private fun PortfolioSummaryCard(
    totalInvested: Double,
    currentValue: Double,
    profitLoss: Double,
    profitLossPercent: Double
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Total Value",
                fontSize = 14.sp,
                color = ext.textSecondary
            )
            
            Text(
                text = "₹${String.format("%,.2f", currentValue)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                val isProfit = profitLoss >= 0
                val color = if (isProfit) ext.stockUp else ext.stockDown
                val prefix = if (isProfit) "+" else ""
                
                Text(
                    text = "$prefix₹${String.format("%,.2f", profitLoss)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                ChangeText(changePercent = profitLossPercent)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(color = colorScheme.outline)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Invested",
                        fontSize = 12.sp,
                        color = ext.textSecondary
                    )
                    Text(
                        text = "₹${String.format("%,.2f", totalInvested)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onSurface
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Returns",
                        fontSize = 12.sp,
                        color = ext.textSecondary
                    )
                    ChangeText(changePercent = profitLossPercent, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun HoldingItem(
    holding: Holding,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = holding.logoUrl,
                contentDescription = holding.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorScheme.outline),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = holding.symbol,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
                Text(
                    text = "${holding.quantity} shares • Avg ₹${String.format("%.2f", holding.averagePrice)}",
                    fontSize = 12.sp,
                    color = ext.textSecondary
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${String.format("%.2f", holding.currentValue)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
                
                val isProfit = holding.isProfit
                val color = if (isProfit) ext.stockUp else ext.stockDown
                val prefix = if (isProfit) "+" else ""
                
                Text(
                    text = "$prefix₹${String.format("%.2f", holding.profitLoss)} (${String.format("%.1f", holding.profitLossPercent)}%)",
                    fontSize = 12.sp,
                    color = color
                )
            }
        }
    }
}
