package avinash.server.data

import avinash.server.models.Stock

object StockData {
    
    private fun createStock(symbol: String, name: String, domain: String, basePrice: Double): Stock {
        return Stock(
            symbol = symbol,
            name = name,
            logoUrl = "https://logo.clearbit.com/$domain",
            currentPrice = basePrice,
            previousPrice = basePrice,
            dayHigh = basePrice,
            dayLow = basePrice,
            volume = (10000..100000).random().toLong(),
            openPrice = basePrice
        )
    }
    
    fun getInitialStocks(): List<Stock> = listOf(
        // IT Sector (6)
        createStock("TCS", "Tata Consultancy Services", "tcs.com", 3850.0),
        createStock("INFY", "Infosys Limited", "infosys.com", 1520.0),
        createStock("WIPRO", "Wipro Limited", "wipro.com", 425.0),
        createStock("HCLTECH", "HCL Technologies", "hcltech.com", 1380.0),
        createStock("TECHM", "Tech Mahindra", "techmahindra.com", 1250.0),
        createStock("LTIM", "LTIMindtree Limited", "ltimindtree.com", 5200.0),
        
        // Banking Sector (8)
        createStock("HDFCBANK", "HDFC Bank", "hdfcbank.com", 1650.0),
        createStock("ICICIBANK", "ICICI Bank", "icicibank.com", 1020.0),
        createStock("SBIN", "State Bank of India", "sbi.co.in", 625.0),
        createStock("KOTAKBANK", "Kotak Mahindra Bank", "kotak.com", 1780.0),
        createStock("AXISBANK", "Axis Bank", "axisbank.com", 1050.0),
        createStock("INDUSINDBK", "IndusInd Bank", "indusind.com", 1420.0),
        createStock("BANKBARODA", "Bank of Baroda", "bankofbaroda.in", 245.0),
        createStock("PNB", "Punjab National Bank", "pnbindia.in", 98.0),
        
        // Pharma Sector (5)
        createStock("SUNPHARMA", "Sun Pharmaceutical", "sunpharma.com", 1180.0),
        createStock("DRREDDY", "Dr. Reddy's Laboratories", "drreddys.com", 5650.0),
        createStock("CIPLA", "Cipla Limited", "cipla.com", 1220.0),
        createStock("DIVISLAB", "Divi's Laboratories", "divislabs.com", 3580.0),
        createStock("LUPIN", "Lupin Limited", "lupin.com", 1350.0),
        
        // Auto Sector (5)
        createStock("TATAMOTORS", "Tata Motors", "tatamotors.com", 720.0),
        createStock("MARUTI", "Maruti Suzuki India", "marutisuzuki.com", 10250.0),
        createStock("M&M", "Mahindra & Mahindra", "mahindra.com", 1580.0),
        createStock("BAJAJ-AUTO", "Bajaj Auto", "bajajauto.com", 6850.0),
        createStock("HEROMOTOCO", "Hero MotoCorp", "heromotocorp.com", 4320.0),
        
        // Energy/Oil Sector (4)
        createStock("RELIANCE", "Reliance Industries", "ril.com", 2450.0),
        createStock("ONGC", "Oil & Natural Gas Corp", "ongcindia.com", 245.0),
        createStock("BPCL", "Bharat Petroleum", "bharatpetroleum.in", 365.0),
        createStock("IOC", "Indian Oil Corporation", "iocl.com", 142.0),
        
        // FMCG Sector (5)
        createStock("ITC", "ITC Limited", "itcportal.com", 445.0),
        createStock("HINDUNILVR", "Hindustan Unilever", "hul.co.in", 2520.0),
        createStock("NESTLEIND", "Nestle India", "nestle.in", 24500.0),
        createStock("BRITANNIA", "Britannia Industries", "britannia.co.in", 4850.0),
        createStock("DABUR", "Dabur India", "dabur.com", 545.0),
        
        // Telecom Sector (2)
        createStock("BHARTIARTL", "Bharti Airtel", "airtel.in", 1120.0),
        createStock("IDEA", "Vodafone Idea", "myvi.in", 14.5),
        
        // Metal/Steel Sector (4)
        createStock("TATASTEEL", "Tata Steel", "tatasteel.com", 135.0),
        createStock("JSWSTEEL", "JSW Steel", "jsw.in", 825.0),
        createStock("HINDALCO", "Hindalco Industries", "hindalco.com", 520.0),
        createStock("COALINDIA", "Coal India", "coalindia.in", 385.0),
        
        // Cement Sector (3)
        createStock("ULTRACEMCO", "UltraTech Cement", "ultratechcement.com", 9850.0),
        createStock("SHREECEM", "Shree Cement", "shreecement.com", 26500.0),
        createStock("AMBUJACEM", "Ambuja Cements", "ambujacement.com", 580.0),
        
        // Finance/NBFC Sector (4)
        createStock("BAJFINANCE", "Bajaj Finance", "bajajfinserv.in", 7250.0),
        createStock("BAJAJFINSV", "Bajaj Finserv", "bajajfinserv.in", 1620.0),
        createStock("HDFCLIFE", "HDFC Life Insurance", "hdfclife.com", 645.0),
        createStock("SBILIFE", "SBI Life Insurance", "sbilife.co.in", 1380.0),
        
        // Others (4)
        createStock("ASIANPAINT", "Asian Paints", "asianpaints.com", 3250.0),
        createStock("TITAN", "Titan Company", "titan.co.in", 3180.0),
        createStock("LT", "Larsen & Toubro", "larsentoubro.com", 3450.0),
        createStock("ADANIENT", "Adani Enterprises", "adani.com", 2850.0)
    )
}
