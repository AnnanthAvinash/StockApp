package avinash.app.mystocks.domain.model

data class HomeData(
    val topGainers: List<Stock> = emptyList(),
    val topLosers: List<Stock> = emptyList(),
    val mostBought: List<Stock> = emptyList(),
    val mostSold: List<Stock> = emptyList()
)
