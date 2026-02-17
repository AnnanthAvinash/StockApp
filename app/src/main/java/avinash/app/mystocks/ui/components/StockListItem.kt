package avinash.app.mystocks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import avinash.app.mystocks.domain.model.Stock
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun StockListItem(
    stock: Stock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isWishlisted: Boolean = false,
    onWishlistClick: (() -> Unit)? = null,
    showWishlistIcon: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = ext.cardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
                Text(
                    text = stock.name,
                    fontSize = 12.sp,
                    color = ext.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${String.format("%.2f", stock.currentPrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
                
                val changeColor = if (stock.isGainer) ext.stockUp else ext.stockDown
                val changePrefix = if (stock.isGainer) "+" else ""
                Text(
                    text = "$changePrefix${String.format("%.2f", stock.dayChangePercent)}%",
                    fontSize = 12.sp,
                    color = changeColor,
                    fontWeight = FontWeight.Medium
                )
            }
            
            if (showWishlistIcon && onWishlistClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onWishlistClick) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) colorScheme.error else ext.textSecondary
                    )
                }
            }
        }
    }
}
