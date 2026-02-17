package avinash.app.mystocks.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import avinash.app.mystocks.domain.model.Stock
import avinash.app.mystocks.ui.components.LoadingIndicator
import avinash.app.mystocks.ui.components.StockGridCard
import avinash.app.mystocks.ui.components.ViewAllCard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is HomeEffect.ShowNoInternet -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = "\u2715",
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.ScreenVisible)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (state.isLoading) {
            LoadingIndicator()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    Text(
                        text = "MyStocks",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                stockGridSection(
                    title = "Top Gainers",
                    stocks = state.homeData.topGainers,
                    onStockClick = onStockClick,
                    onViewAllClick = { }
                )

                stockGridSection(
                    title = "Top Losers",
                    stocks = state.homeData.topLosers,
                    onStockClick = onStockClick,
                    onViewAllClick = { }
                )

                stockGridSection(
                    title = "Most Bought",
                    stocks = state.homeData.mostBought,
                    onStockClick = onStockClick,
                    onViewAllClick = { }
                )

                stockGridSection(
                    title = "Most Sold",
                    stocks = state.homeData.mostSold,
                    onStockClick = onStockClick,
                    onViewAllClick = { }
                )
            }
        }
    }
}

private sealed class GridItem {
    data class StockItem(val stock: Stock) : GridItem()
    data object ViewAll : GridItem()
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

private fun LazyListScope.stockGridSection(
    title: String,
    stocks: List<Stock>,
    onStockClick: (String) -> Unit,
    onViewAllClick: () -> Unit
) {
    if (stocks.isEmpty()) return

    item {
        SectionTitle(title)
    }

    val gridItems: List<GridItem> =
        stocks.map { GridItem.StockItem(it) } + GridItem.ViewAll
    val rows = gridItems.chunked(2)

    items(rows.size) { rowIndex ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rows[rowIndex].forEach { gridItem ->
                when (gridItem) {
                    is GridItem.StockItem -> {
                        StockGridCard(
                            stock = gridItem.stock,
                            onClick = { onStockClick(gridItem.stock.symbol) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    is GridItem.ViewAll -> {
                        ViewAllCard(
                            onClick = onViewAllClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            if (rows[rowIndex].size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    item { Spacer(modifier = Modifier.height(16.dp)) }
}
