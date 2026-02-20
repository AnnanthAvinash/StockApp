package avinash.app.mystocks.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avinash.app.mystocks.domain.model.RecentStock
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun RecentStocksSection(
    recentStocks: List<RecentStock>,
    onStockClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        Text(
            text = "Recently Viewed",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recentStocks, key = { it.symbol }) { stock ->
                RecentStockChip(
                    stock = stock,
                    onClick = { onStockClick(stock.symbol) }
                )
            }
        }
    }
}

@Composable
private fun RecentStockChip(
    stock: RecentStock,
    onClick: () -> Unit
) {
    val ext = AppTheme.extendedColors

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(64.dp)
            .clickable(onClick = onClick)
    ) {
        StockLogo(
            logoUrl = stock.logoUrl,
            name = stock.name,
            size = 48.dp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stock.name,
            fontSize = 11.sp,
            color = ext.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
