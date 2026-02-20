package avinash.app.mystocks.ui.screens.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import avinash.app.mystocks.domain.model.OrderStatus
import avinash.app.mystocks.domain.model.TradeAction
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
            val order = uiState.pendingOrder
            when (order?.status) {
                OrderStatus.PENDING -> {
                    snackbarHostState.showSnackbar(
                        message = "Order placed! Check Orders tab.",
                        duration = SnackbarDuration.Short
                    )
                    delay(400)
                    viewModel.resetOrderPlaced()
                    onTradeComplete()
                }
                OrderStatus.FAILED -> {
                    snackbarHostState.showSnackbar(
                        message = order.message ?: "Order failed",
                        duration = SnackbarDuration.Short
                    )
                    viewModel.resetTradeState()
                }
                else -> {
                    viewModel.resetOrderPlaced()
                    onTradeComplete()
                }
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
                title = {
                    uiState.stock?.let { stock ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stock.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground
                            )
                            Text(
                                text = "₹${String.format("%.2f", stock.currentPrice)}",
                                fontSize = 14.sp,
                                color = ext.textSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(48.dp))
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
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Buy/Sell Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(colorScheme.surfaceVariant)
                    .padding(4.dp)
            ) {
                TradeActionButton(
                    text = "Buy",
                    isSelected = uiState.action == TradeAction.BUY,
                    onClick = { viewModel.setAction(TradeAction.BUY) },
                    modifier = Modifier.weight(1f)
                )

                TradeActionButton(
                    text = "Sell",
                    isSelected = uiState.action == TradeAction.SELL,
                    onClick = { viewModel.setAction(TradeAction.SELL) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Quantity label
            Text(
                text = if (uiState.action == TradeAction.BUY) "Shares to buy" else "Shares to sell",
                fontSize = 14.sp,
                color = ext.textSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quantity selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { viewModel.decrementQuantity() },
                    enabled = uiState.canDecrement && !uiState.isLoading,
                    modifier = Modifier
                        .size(52.dp)
                        .border(
                            width = 1.5.dp,
                            color = colorScheme.outline,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = colorScheme.onSurface
                    )
                }

                Text(
                    text = "${uiState.quantity}",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(min = 100.dp)
                )

                IconButton(
                    onClick = { viewModel.incrementQuantity() },
                    enabled = uiState.canIncrement && !uiState.isLoading,
                    modifier = Modifier
                        .size(52.dp)
                        .border(
                            width = 1.5.dp,
                            color = colorScheme.outline,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Balance (Buy) or owned shares (Sell)
            Text(
                text = if (uiState.action == TradeAction.BUY) {
                    "Available: ₹${String.format("%,.2f", uiState.availableBalance)}"
                } else {
                    "Owned: ${uiState.maxSellQuantity} shares"
                },
                fontSize = 14.sp,
                color = ext.textSecondary
            )

            Spacer(modifier = Modifier.weight(1f))

            // Cost breakdown card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Estimated Cost",
                            fontSize = 14.sp,
                            color = ext.textSecondary
                        )
                        Text(
                            text = "₹${String.format("%,.2f", uiState.totalValue)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Brokerage Fee",
                            fontSize = 14.sp,
                            color = ext.textSecondary
                        )
                        Text(
                            text = "Free",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = ext.stockUp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(color = colorScheme.outlineVariant)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Payable",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onSurface
                        )
                        Text(
                            text = "₹${String.format("%,.2f", uiState.totalValue)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Error message (validation or server error)
            (uiState.validationError ?: uiState.error)?.let { error ->
                Text(
                    text = error,
                    color = colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Confirm Order button
            Button(
                onClick = { viewModel.executeTrade() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.secondary,
                    contentColor = colorScheme.onSecondary
                ),
                enabled = uiState.canConfirm && !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = colorScheme.onSecondary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm Order",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun TradeActionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) colorScheme.secondary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) colorScheme.onSecondary else ext.textSecondary
        )
    }
}
