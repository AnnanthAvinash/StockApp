package avinash.app.mystocks.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avinash.app.mystocks.domain.model.Stock
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun StockGridCard(
    stock: Stock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = ext.cardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StockLogo(
                    logoUrl = stock.logoUrl,
                    name = stock.name,
                    size = 36.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stock.symbol,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface,
                        maxLines = 1
                    )
                    Text(
                        text = stock.name,
                        fontSize = 11.sp,
                        color = ext.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "\u20B9${String.format("%.2f", stock.currentPrice)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorScheme.onSurface
            )
            val changeColor = if (stock.isGainer) ext.stockUp else ext.stockDown
            val prefix = if (stock.isGainer) "+" else ""
            Text(
                text = "$prefix${String.format("%.2f", stock.dayChangePercent)}%",
                fontSize = 12.sp,
                color = changeColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
