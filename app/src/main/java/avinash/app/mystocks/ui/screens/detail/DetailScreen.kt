package avinash.app.mystocks.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import avinash.app.mystocks.ui.components.ChangeText
import avinash.app.mystocks.ui.components.LoadingIndicator
import avinash.app.mystocks.ui.components.PriceLineChart
import avinash.app.mystocks.ui.components.PriceText
import avinash.app.mystocks.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onTradeClick: (String) -> Unit
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
                            AsyncImage(
                                model = stock.logoUrl,
                                contentDescription = stock.name,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(colorScheme.outline),
                                contentScale = ContentScale.Crop
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
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (uiState.isChartLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = ext.textSecondary,
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
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Your Holdings",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onSurface
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Quantity", fontSize = 12.sp, color = ext.textSecondary)
                                    Text("${holding.quantity}", color = colorScheme.onSurface, fontWeight = FontWeight.Medium)
                                }
                                Column {
                                    Text("Avg Price", fontSize = 12.sp, color = ext.textSecondary)
                                    Text("₹${String.format("%.2f", holding.averagePrice)}", color = colorScheme.onSurface, fontWeight = FontWeight.Medium)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("P&L", fontSize = 12.sp, color = ext.textSecondary)
                                    val color = if (holding.isProfit) ext.stockUp else ext.stockDown
                                    val prefix = if (holding.isProfit) "+" else ""
                                    Text(
                                        "$prefix₹${String.format("%.2f", holding.profitLoss)}",
                                        color = color,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { onTradeClick(stock.symbol) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.secondary
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
                
                Spacer(modifier = Modifier.height(24.dp))
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
