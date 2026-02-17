package avinash.app.mystocks.ui.screens.wishlist

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import avinash.app.mystocks.ui.components.StockListItem
import avinash.app.mystocks.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: WishlistViewModel = hiltViewModel(),
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "Wishlist",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${uiState.stocks.size} stocks saved",
                    fontSize = 14.sp,
                    color = ext.textSecondary
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            if (uiState.stocks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ext.cardBackground)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "❤️",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Your wishlist is empty",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorScheme.onSurface
                            )
                            Text(
                                text = "Add stocks you want to track",
                                fontSize = 14.sp,
                                color = ext.textSecondary
                            )
                        }
                    }
                }
            } else {
                items(
                    items = uiState.stocks,
                    key = { it.symbol }
                ) { stock ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.removeFromWishlist(stock.symbol)
                                true
                            } else {
                                false
                            }
                        }
                    )
                    
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        colorScheme.error,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = colorScheme.onError
                                )
                            }
                        },
                        enableDismissFromStartToEnd = false
                    ) {
                        StockListItem(
                            stock = stock,
                            onClick = { onStockClick(stock.symbol) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
