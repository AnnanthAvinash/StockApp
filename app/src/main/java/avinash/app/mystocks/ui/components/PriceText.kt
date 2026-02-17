package avinash.app.mystocks.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun PriceText(
    price: Double,
    previousPrice: Double? = null,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    showArrow: Boolean = true,
    modifier: Modifier = Modifier
) {
    val ext = AppTheme.extendedColors
    val colorScheme = MaterialTheme.colorScheme
    val isUp = previousPrice?.let { price > it } ?: true
    val color = when {
        previousPrice == null -> colorScheme.onSurface
        price > previousPrice -> ext.stockUp
        price < previousPrice -> ext.stockDown
        else -> colorScheme.onSurface
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = price,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { -it } + fadeIn() togetherWith 
                    slideOutVertically { it } + fadeOut()
                } else {
                    slideInVertically { it } + fadeIn() togetherWith 
                    slideOutVertically { -it } + fadeOut()
                }
            },
            label = "price_animation"
        ) { targetPrice ->
            Text(
                text = "₹${String.format("%.2f", targetPrice)}",
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color
            )
        }
        
        if (showArrow && previousPrice != null && price != previousPrice) {
            Icon(
                imageVector = if (isUp) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = if (isUp) "Up" else "Down",
                tint = color
            )
        }
    }
}

@Composable
fun ChangeText(
    changePercent: Double,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    modifier: Modifier = Modifier
) {
    val ext = AppTheme.extendedColors
    val isPositive = changePercent >= 0
    val color = if (isPositive) ext.stockUp else ext.stockDown
    val prefix = if (isPositive) "+" else ""
    
    Text(
        text = "$prefix${String.format("%.2f", changePercent)}%",
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        modifier = modifier
    )
}
