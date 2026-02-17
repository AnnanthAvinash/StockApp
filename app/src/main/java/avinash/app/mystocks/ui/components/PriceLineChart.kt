package avinash.app.mystocks.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import avinash.app.mystocks.domain.model.PricePoint
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun PriceLineChart(
    pricePoints: List<PricePoint>,
    isGainer: Boolean,
    modifier: Modifier = Modifier
) {
    val ext = AppTheme.extendedColors
    val lineColor = if (isGainer) ext.stockUp else ext.stockDown
    val fillColor = if (isGainer) ext.stockUp else ext.stockDown

    Canvas(modifier = modifier) {
        if (pricePoints.size < 2) return@Canvas

        val prices = pricePoints.map { it.price }
        val minPrice = prices.min()
        val maxPrice = prices.max()
        val range = (maxPrice - minPrice).coerceAtLeast(0.01)

        val paddingTop = 8f
        val paddingBottom = 8f
        val drawHeight = size.height - paddingTop - paddingBottom

        val stepX = size.width / (pricePoints.size - 1).toFloat()

        fun priceToY(price: Double): Float {
            return paddingTop + drawHeight - ((price - minPrice) / range * drawHeight).toFloat()
        }

        val linePath = Path().apply {
            moveTo(0f, priceToY(prices[0]))
            for (i in 1 until prices.size) {
                lineTo(i * stepX, priceToY(prices[i]))
            }
        }

        val fillPath = Path().apply {
            addPath(linePath)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        clipRect {
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(fillColor.copy(alpha = 0.5f), fillColor.copy(alpha = 0f)),
                    startY = 0f,
                    endY = size.height
                )
            )
        }

        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(
                width = 2.5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        val lastX = (prices.size - 1) * stepX
        val lastY = priceToY(prices.last())
        drawCircle(
            color = lineColor,
            radius = 4f,
            center = Offset(lastX, lastY)
        )
        drawCircle(
            color = lineColor.copy(alpha = 0.3f),
            radius = 8f,
            center = Offset(lastX, lastY)
        )
    }
}
