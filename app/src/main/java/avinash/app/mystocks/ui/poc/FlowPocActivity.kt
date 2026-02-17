package avinash.app.mystocks.ui.poc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avinash.app.mystocks.ui.theme.AppTheme
import avinash.app.mystocks.ui.theme.MyStocksTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

data class StockItem(val name: String,val key:Int, val price: Double)

fun stockPriceFlow(initial: List<StockItem>): Flow<List<StockItem>> = flow {
    var current = initial
    while (true) {
        delay(1000)
        current = current.map {
            val change = Random.nextDouble(-2.0, 2.0)
            it.copy(price = (it.price + change).coerceAtLeast(1.0))
        }
        emit(current)
    }
}

class FlowPocActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialList = (1 until 500).map { i ->
            val name = ('A' + (i / 26)).toString() + if (i >= 26) ('A' + (i % 26)).toString() else ""

            StockItem(name = name, i,price = Random.nextDouble(1.0*i, 10.0*i))
        }

        setContent {
            MyStocksTheme {
                StockListScreen(initialList)
            }
        }
    }
}

@Composable
fun StockListScreen(initial: List<StockItem>) {
    val stocks by remember { stockPriceFlow(initial) }
        .collectAsState(initial = initial)
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp)
    ) {
        items(stocks, key = { it.key }) { stock ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stock.name,
                    color = colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹%.2f".format(stock.price),
                    color = ext.stockUp,
                    fontSize = 16.sp
                )
            }
        }
    }
}
