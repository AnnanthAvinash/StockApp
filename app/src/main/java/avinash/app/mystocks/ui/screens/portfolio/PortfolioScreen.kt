package avinash.app.mystocks.ui.screens.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import avinash.app.mystocks.domain.model.Holding
import avinash.app.mystocks.ui.components.LoadingIndicator
import avinash.app.mystocks.ui.components.StockLogo
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                        overallProfitLoss = uiState.portfolio.totalProfitLoss,
                        overallProfitLossPercent = uiState.portfolio.totalProfitLossPercent,
                        todayProfitLoss = uiState.portfolio.todayProfitLoss,
                        todayProfitLossPercent = uiState.portfolio.todayProfitLossPercent
                    )
                    Spacer(modifier = Modifier.height(24.dp))
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
private fun PortfolioSummaryCard(
    totalInvested: Double,
    currentValue: Double,
    overallProfitLoss: Double,
    overallProfitLossPercent: Double,
    todayProfitLoss: Double,
    todayProfitLossPercent: Double
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            SummaryRow(
                label = "Invested Value",
                value = "₹${String.format("%,.2f", totalInvested)}",
                valueColor = colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(14.dp))
            
            SummaryRow(
                label = "Current Value",
                value = "₹${String.format("%,.2f", currentValue)}",
                valueColor = colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(color = colorScheme.outline.copy(alpha = 0.3f))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val overallIsProfit = overallProfitLoss >= 0
            val overallColor = if (overallIsProfit) ext.stockUp else ext.stockDown
            val overallPrefix = if (overallIsProfit) "+" else ""
            
            SummaryRow(
                label = "Overall P&L",
                value = "$overallPrefix₹${String.format("%,.2f", overallProfitLoss)} (${String.format("%.2f", overallProfitLossPercent)}%)",
                valueColor = overallColor
            )
            
            Spacer(modifier = Modifier.height(14.dp))
            
            val todayIsProfit = todayProfitLoss >= 0
            val todayColor = if (todayIsProfit) ext.stockUp else ext.stockDown
            val todayPrefix = if (todayIsProfit) "+" else ""
            
            SummaryRow(
                label = "Today's P&L",
                value = "$todayPrefix₹${String.format("%,.2f", todayProfitLoss)} (${String.format("%.2f", todayProfitLossPercent)}%)",
                valueColor = todayColor
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color
) {
    val ext = AppTheme.extendedColors
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = ext.textSecondary
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
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
            StockLogo(
                logoUrl = holding.logoUrl,
                name = holding.name,
                size = 48.dp
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
