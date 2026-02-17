package avinash.app.mystocks.ui.screens.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import avinash.app.mystocks.domain.model.OrderStatus
import avinash.app.mystocks.domain.model.TradeAction
import avinash.app.mystocks.ui.components.PriceText
import avinash.app.mystocks.ui.theme.AppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(
    viewModel: TradeViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onTradeComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    LaunchedEffect(uiState.orderPlaced) {
        if (uiState.orderPlaced) {
            val pendingOrder = uiState.pendingOrder
            if (pendingOrder != null && pendingOrder.status == OrderStatus.PENDING) {
                snackbarHostState.showSnackbar(
                    message = "Order placed successfully! Processing...",
                    duration = SnackbarDuration.Short
                )
                delay(500)
                onTradeComplete()
            } else if (pendingOrder?.status == OrderStatus.FAILED) {
                snackbarHostState.showSnackbar(
                    message = pendingOrder.message ?: "Order failed",
                    duration = SnackbarDuration.Short
                )
                viewModel.resetTradeState()
            }
        }
    }
    
    Scaffold(
        containerColor = colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = ext.cardBackground,
                    contentColor = colorScheme.onSurface
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Trade", color = colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            uiState.stock?.let { stock ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = stock.logoUrl,
                            contentDescription = stock.name,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(colorScheme.outline),
                            contentScale = ContentScale.Crop
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stock.symbol,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onSurface
                            )
                            Text(
                                text = stock.name,
                                fontSize = 14.sp,
                                color = ext.textSecondary
                            )
                        }
                        
                        PriceText(
                            price = stock.currentPrice,
                            previousPrice = stock.previousPrice,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.surfaceVariant)
                    .padding(4.dp)
            ) {
                TradeActionButton(
                    text = "BUY",
                    isSelected = uiState.action == TradeAction.BUY,
                    color = ext.stockUp,
                    onClick = { viewModel.setAction(TradeAction.BUY) },
                    modifier = Modifier.weight(1f)
                )
                
                TradeActionButton(
                    text = "SELL",
                    isSelected = uiState.action == TradeAction.SELL,
                    color = ext.stockDown,
                    onClick = { viewModel.setAction(TradeAction.SELL) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Quantity",
                fontSize = 14.sp,
                color = ext.textSecondary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { viewModel.decrementQuantity() },
                    modifier = Modifier
                        .size(56.dp)
                        .background(colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                Text(
                    text = "${uiState.quantity}",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.width(32.dp))
                
                IconButton(
                    onClick = { viewModel.incrementQuantity() },
                    modifier = Modifier
                        .size(56.dp)
                        .background(colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = colorScheme.onSurface
                    )
                }
            }
            
            if (uiState.action == TradeAction.SELL) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Available: ${uiState.maxSellQuantity} shares",
                    fontSize = 14.sp,
                    color = ext.textSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Value",
                        fontSize = 16.sp,
                        color = ext.textSecondary
                    )
                    Text(
                        text = "₹${String.format("%,.2f", uiState.totalValue)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = ext.surfaceElevated)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = ext.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Order will be placed and processed. Check Portfolio for status.",
                        fontSize = 12.sp,
                        color = ext.textSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val buttonColor = if (uiState.action == TradeAction.BUY) ext.stockUp else ext.stockDown
            val buttonText = if (uiState.action == TradeAction.BUY) "Place Buy Order" else "Place Sell Order"
            
            Button(
                onClick = { viewModel.executeTrade() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                enabled = uiState.canTrade && !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = buttonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TradeActionButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ext = AppTheme.extendedColors
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) color else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else ext.textSecondary
        )
    }
}
