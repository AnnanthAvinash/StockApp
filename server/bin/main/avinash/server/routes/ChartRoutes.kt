package avinash.server.routes

import avinash.server.data.StockManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.chartRoutes(stockManager: StockManager) {
    
    // GET /chart/{symbol} - HTML page with Chart.js for WebView
    get("/chart/{symbol}") {
        val symbol = call.parameters["symbol"]?.uppercase()
            ?: return@get call.respond(HttpStatusCode.BadRequest, "Symbol required")
        
        val stock = stockManager.getStock(symbol)
            ?: return@get call.respond(HttpStatusCode.NotFound, "Stock not found: $symbol")
        
        val chartHtml = generateChartHtml(symbol, stock.name)
        call.respondText(chartHtml, ContentType.Text.Html)
    }
}

private fun generateChartHtml(symbol: String, stockName: String): String {
    return """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>$stockName ($symbol)</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        html, body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #0a0a0a;
            color: #ffffff;
            padding: 12px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;
        }
        .stock-name {
            font-size: 18px;
            font-weight: 600;
        }
        .price-info {
            text-align: right;
        }
        .current-price {
            font-size: 24px;
            font-weight: 700;
        }
        .change {
            font-size: 14px;
        }
        .positive { color: #22c55e; }
        .negative { color: #ef4444; }
        .chart-container {
            position: relative;
            height: 55vw;
            max-height: 280px;
            margin-bottom: 12px;
        }
        .period-selector {
            display: flex;
            gap: 6px;
            justify-content: center;
        }
        .period-btn {
            padding: 6px 12px;
            border: 1px solid #333;
            background: transparent;
            color: #888;
            border-radius: 8px;
            cursor: pointer;
            font-size: 13px;
            transition: all 0.2s ease;
        }
        .period-btn:hover {
            border-color: #555;
            color: #aaa;
        }
        .period-btn.active {
            background: #1a1a1a;
            color: #fff;
            border-color: #22c55e;
        }
        .period-btn:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }
        .status {
            text-align: center;
            margin-top: 10px;
            font-size: 11px;
            color: #666;
        }
        .status.connected { color: #22c55e; }
        .status.disconnected { color: #ef4444; }
        .status.loading { color: #f59e0b; }
    </style>
</head>
<body>
    <div class="header">
        <div class="stock-name">$symbol</div>
        <div class="price-info">
            <div class="current-price" id="currentPrice">--</div>
            <div class="change" id="changeInfo">--</div>
        </div>
    </div>
    
    <div class="chart-container">
        <canvas id="priceChart"></canvas>
    </div>
    
    <div class="period-selector">
        <button class="period-btn active" data-period="1D">1D</button>
        <button class="period-btn" data-period="1W">1W</button>
        <button class="period-btn" data-period="1M">1M</button>
        <button class="period-btn" data-period="1Y">1Y</button>
        <button class="period-btn" data-period="MAX">MAX</button>
    </div>
    
    <div class="status" id="status">Loading...</div>

    <script>
        const symbol = '$symbol';
        let chart;
        let priceData = [];
        let labels = [];
        let ws;
        let currentPeriod = '1D';
        let currentChangePercent = 0;
        
        // Initialize Chart
        function initChart() {
            const ctx = document.getElementById('priceChart').getContext('2d');
            chart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Price',
                        data: priceData,
                        borderColor: '#22c55e',
                        backgroundColor: 'rgba(34, 197, 94, 0.1)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4,
                        pointRadius: 0,
                        pointHoverRadius: 4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return '₹' + context.parsed.y.toFixed(2);
                                }
                            }
                        }
                    },
                    scales: {
                        x: {
                            display: true,
                            grid: {
                                color: '#1a1a1a'
                            },
                            ticks: {
                                color: '#666',
                                maxRotation: 0,
                                autoSkip: true,
                                maxTicksLimit: 6
                            }
                        },
                        y: {
                            grid: {
                                color: '#222'
                            },
                            ticks: {
                                color: '#888',
                                callback: function(value) {
                                    return '₹' + value.toFixed(0);
                                }
                            }
                        }
                    },
                    interaction: {
                        intersect: false,
                        mode: 'index'
                    }
                }
            });
        }
        
        // Format timestamp based on period
        function formatLabel(timestamp, period) {
            const date = new Date(timestamp);
            switch(period) {
                case '1D':
                    return date.getHours().toString().padStart(2, '0') + ':' + 
                           date.getMinutes().toString().padStart(2, '0');
                case '1W':
                    const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
                    return days[date.getDay()] + ' ' + date.getHours() + ':00';
                case '1M':
                    return date.getDate() + '/' + (date.getMonth() + 1);
                case '1Y':
                    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 
                                   'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                    return months[date.getMonth()] + ' ' + date.getDate();
                case 'MAX':
                    const monthsShort = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                                        'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                    return monthsShort[date.getMonth()] + ' ' + date.getFullYear().toString().slice(-2);
                default:
                    return date.toLocaleDateString();
            }
        }
        
        // Fetch historical data via HTTP
        async function loadPeriodData(period) {
            const statusEl = document.getElementById('status');
            statusEl.textContent = 'Loading...';
            statusEl.className = 'status loading';
            
            // Disable buttons during load
            document.querySelectorAll('.period-btn').forEach(b => b.disabled = true);
            
            try {
                const response = await fetch('/api/stocks/' + symbol + '/history?period=' + period);
                if (!response.ok) throw new Error('Failed to fetch data');
                
                const data = await response.json();
                
                // Clear existing data
                priceData.length = 0;
                labels.length = 0;
                
                // Populate with historical data
                data.forEach(point => {
                    priceData.push(point.price);
                    labels.push(formatLabel(point.timestamp, period));
                });
                
                // Calculate period change percent
                if (priceData.length >= 2) {
                    const startPrice = priceData[0];
                    const endPrice = priceData[priceData.length - 1];
                    const periodChange = ((endPrice - startPrice) / startPrice) * 100;
                    updateChartColor(periodChange);
                }
                
                // Update chart
                chart.update();
                
                currentPeriod = period;
                updateStatus();
                
            } catch (error) {
                console.error('Error loading period data:', error);
                statusEl.textContent = 'Error loading data';
                statusEl.className = 'status disconnected';
            } finally {
                // Re-enable buttons
                document.querySelectorAll('.period-btn').forEach(b => b.disabled = false);
            }
        }
        
        // Update chart color based on trend
        function updateChartColor(changePercent) {
            if (chart) {
                const color = changePercent >= 0 ? '#22c55e' : '#ef4444';
                chart.data.datasets[0].borderColor = color;
                chart.data.datasets[0].backgroundColor = changePercent >= 0 
                    ? 'rgba(34, 197, 94, 0.1)' 
                    : 'rgba(239, 68, 68, 0.1)';
            }
        }
        
        // WebSocket Connection for real-time updates
        function connectWebSocket() {
            const host = window.location.host;
            ws = new WebSocket('ws://' + host + '/ws/stocks');
            
            ws.onopen = function() {
                ws.send(JSON.stringify({ action: 'SUBSCRIBE', type: 'SYMBOL:' + symbol }));
                updateStatus();
            };
            
            ws.onclose = function() {
                document.getElementById('status').textContent = 'Disconnected';
                document.getElementById('status').className = 'status disconnected';
                setTimeout(connectWebSocket, 3000);
            };
            
            ws.onmessage = function(event) {
                const message = JSON.parse(event.data);
                handleMessage(message);
            };
            
            ws.onerror = function(error) {
                console.error('WebSocket error:', error);
            };
        }
        
        function handleMessage(message) {
            if (message.type === 'INITIAL') {
                // Find our stock in gainers or losers
                const allStocks = [...message.topGainers, ...message.topLosers];
                const stock = allStocks.find(s => s.symbol === symbol);
                if (stock) {
                    updatePrice(stock.currentPrice, stock.changePercent);
                }
            } else if (message.type === 'PRICE_UPDATE') {
                const update = message.updates.find(u => u.symbol === symbol);
                if (update) {
                    updatePrice(update.currentPrice, update.changePercent);
                    // Only append real-time data when viewing 1D
                    if (currentPeriod === '1D') {
                        addPricePoint(update.currentPrice);
                    }
                }
            }
        }
        
        function updatePrice(price, changePercent) {
            document.getElementById('currentPrice').textContent = '₹' + price.toFixed(2);
            const changeEl = document.getElementById('changeInfo');
            const sign = changePercent >= 0 ? '+' : '';
            changeEl.textContent = sign + changePercent.toFixed(2) + '%';
            changeEl.className = 'change ' + (changePercent >= 0 ? 'positive' : 'negative');
            currentChangePercent = changePercent;
        }
        
        function addPricePoint(price) {
            const now = new Date();
            const timeLabel = formatLabel(now.getTime(), '1D');
            
            priceData.push(price);
            labels.push(timeLabel);
            
            // Keep only last 100 points for 1D view
            if (priceData.length > 100) {
                priceData.shift();
                labels.shift();
            }
            
            // Update chart color based on overall trend
            if (priceData.length >= 2) {
                const startPrice = priceData[0];
                const periodChange = ((price - startPrice) / startPrice) * 100;
                updateChartColor(periodChange);
            }
            
            chart.update('none');
        }
        
        function updateStatus() {
            const statusEl = document.getElementById('status');
            if (ws && ws.readyState === WebSocket.OPEN) {
                if (currentPeriod === '1D') {
                    statusEl.textContent = 'Live';
                    statusEl.className = 'status connected';
                } else {
                    statusEl.textContent = 'Historical Data';
                    statusEl.className = 'status';
                }
            }
        }
        
        // Period selector click handler
        document.querySelectorAll('.period-btn').forEach(btn => {
            btn.addEventListener('click', function() {
                const period = this.dataset.period;
                if (period === currentPeriod) return;
                
                document.querySelectorAll('.period-btn').forEach(b => b.classList.remove('active'));
                this.classList.add('active');
                
                loadPeriodData(period);
            });
        });
        
        // Initialize
        initChart();
        loadPeriodData('1D');  // Load initial 1D data via HTTP
        connectWebSocket();    // Connect WebSocket for real-time updates
    </script>
</body>
</html>
    """.trimIndent()
}
