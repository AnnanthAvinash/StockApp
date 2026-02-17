package avinash.app.mystocks.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = colorScheme.primary
        )
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val shimmerColors = listOf(
        colorScheme.surfaceVariant,
        colorScheme.outline,
        colorScheme.surfaceVariant
    )
    
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(brush)
    )
}

@Composable
fun StockListItemShimmer(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerEffect(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(24.dp))
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            ShimmerEffect(
                modifier = Modifier
                    .height(16.dp)
                    .width(80.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerEffect(
                modifier = Modifier
                    .height(12.dp)
                    .width(120.dp)
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            ShimmerEffect(
                modifier = Modifier
                    .height(16.dp)
                    .width(60.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerEffect(
                modifier = Modifier
                    .height(12.dp)
                    .width(40.dp)
            )
        }
    }
}
