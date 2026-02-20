package avinash.app.mystocks.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import avinash.app.mystocks.ui.components.ChangeText
import avinash.app.mystocks.ui.components.LoadingIndicator
import avinash.app.mystocks.ui.components.PriceLineChart
import avinash.app.mystocks.ui.components.PriceText
import avinash.app.mystocks.ui.components.RecentStocksSection
import avinash.app.mystocks.ui.components.StockLogo
import avinash.app.mystocks.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onTradeClick: (String) -> Unit,
    onStockClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val symbol = viewModel.symbol
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    DisposableEffect(symbol) {
        viewModel.onScreenVisible()
        onDispose { viewModel.onScreenHidden() }
    }
    
    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    uiState.stock?.let { stock ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StockLogo(
                                logoUrl = stock.logoUrl,
                                name = stock.name,
                                size = 32.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stock.symbol,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleWishlist() }) {
                        Icon(
                            imageVector = if (uiState.isWishlisted) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (uiState.isWishlisted) colorScheme.error else ext.textSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background
                )
            )
        },
        bottomBar = {
            if (!uiState.isLoading && uiState.stock != null) {
                Surface(
                    color = colorScheme.background,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = { onTradeClick(uiState.stock!!.symbol) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.secondary,
                            contentColor = colorScheme.onSecondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Trade",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading || uiState.stock == null) {
            LoadingIndicator()
        } else {
            val stock = uiState.stock!!
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stock.name,
                        fontSize = 14.sp,
                        color = ext.textSecondary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PriceText(
                            price = stock.currentPrice,
                            previousPrice = stock.previousPrice,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        ChangeText(
                            changePercent = stock.dayChangePercent,
                            fontSize = 16.sp
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                        if (uiState.isChartLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = colorScheme.secondary,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        } else {
                            PriceLineChart(
                                pricePoints = uiState.pricePoints,
                                isGainer = stock.isGainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("1D", "1W", "1M", "6M", "1Y").forEach { period ->
                                val isSelected = uiState.selectedPeriod == period
                                TextButton(
                                    onClick = { viewModel.loadChartData(period) },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = if (isSelected) ext.info else ext.textSecondary
                                    ),
                                    modifier = Modifier
                                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                                        .then(
                                            if (isSelected) Modifier.background(
                                                ext.info.copy(alpha = 0.12f),
                                                RoundedCornerShape(8.dp)
                                            ) else Modifier
                                        ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = period,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Today's Stats",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatItem("Open", "₹${String.format("%.2f", stock.openPrice)}")
                            StatItem("High", "₹${String.format("%.2f", stock.dayHigh)}")
                            StatItem("Low", "₹${String.format("%.2f", stock.dayLow)}")
                            StatItem("Volume", formatVolume(stock.volume))
                        }
                    }
                }
                
                uiState.holding?.let { holding ->
                    Spacer(modifier = Modifier.height(16.dp))
                    HoldingsCard(holding = holding)
                }
                
                if (uiState.recentStocks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    RecentStocksSection(
                        recentStocks = uiState.recentStocks,
                        onStockClick = onStockClick
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun HoldingsCard(holding: Holding) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    val isProfit = holding.isProfit
    val plColor = if (isProfit) ext.stockUp else ext.stockDown
    val plPrefix = if (isProfit) "+" else ""
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Holdings",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            HorizontalDivider(color = colorScheme.outline.copy(alpha = 0.3f))
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(
                        text = "Total Qty ",
                        fontSize = 13.sp,
                        color = ext.textSecondary
                    )
                    Text(
                        text = "${holding.quantity}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                }
                Text(
                    text = "Invested  ₹${String.format("%,.2f", holding.totalInvested)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(
                        text = "Avg Price ",
                        fontSize = 13.sp,
                        color = ext.textSecondary
                    )
                    Text(
                        text = "₹${String.format("%,.2f", holding.averagePrice)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )
                }
                Text(
                    text = "P&L  $plPrefix₹${String.format("%,.2f", holding.profitLoss)} (${String.format("%.1f", holding.profitLossPercent)}%)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = plColor
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    val ext = AppTheme.extendedColors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = ext.textSecondary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatVolume(volume: Long): String {
    return when {
        volume >= 10_000_000 -> "${volume / 1_000_000}M"
        volume >= 100_000 -> "${volume / 1_000}K"
        else -> volume.toString()
    }
}
