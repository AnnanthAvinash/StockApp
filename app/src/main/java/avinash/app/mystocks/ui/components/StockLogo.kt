package avinash.app.mystocks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun StockLogo(
    logoUrl: String,
    name: String,
    size: Dp = 40.dp,
    fontSize: TextUnit = (size.value * 0.4f).sp,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    SubcomposeAsyncImage(
        model = logoUrl,
        contentDescription = name,
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        loading = { LetterFallback(name, size, fontSize) },
        error = { LetterFallback(name, size, fontSize) }
    )
}

@Composable
private fun LetterFallback(
    name: String,
    size: Dp,
    fontSize: TextUnit
) {
    val colorScheme = MaterialTheme.colorScheme
    val letter = name.firstOrNull()?.uppercase() ?: "?"

    Box(
        modifier = Modifier
            .size(size)
            .background(colorScheme.secondaryContainer, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSecondaryContainer
        )
    }
}
