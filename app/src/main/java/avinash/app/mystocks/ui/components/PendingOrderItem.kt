package avinash.app.mystocks.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import avinash.app.mystocks.domain.model.OrderStatus
import avinash.app.mystocks.domain.model.PendingOrder
import avinash.app.mystocks.domain.model.TradeAction
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun PendingOrderItem(
    order: PendingOrder,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    val backgroundColor by animateColorAsState(
        targetValue = when (order.status) {
            OrderStatus.PENDING -> ext.cardBackground
            OrderStatus.SUCCESS -> ext.successContainer
            OrderStatus.FAILED -> ext.destructiveContainer
        },
        animationSpec = tween(300),
        label = "bg_color"
    )
    
    val statusColor = when (order.status) {
        OrderStatus.PENDING -> ext.warning
        OrderStatus.SUCCESS -> ext.stockUp
        OrderStatus.FAILED -> ext.stockDown
    }
    
    val actionColor = if (order.action == TradeAction.BUY) {
        ext.stockUp
    } else {
        ext.stockDown
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                when (order.status) {
                    OrderStatus.PENDING -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = statusColor,
                            strokeWidth = 2.dp
                        )
                    }
                    OrderStatus.SUCCESS -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = statusColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    OrderStatus.FAILED -> {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Failed",
                            tint = statusColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.symbol,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(actionColor.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = order.action.name,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = actionColor
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${order.quantity} shares @ ₹${String.format("%,.2f", order.priceAtOrder)}",
                    fontSize = 13.sp,
                    color = ext.textSecondary
                )
                
                if (order.message != null && order.status != OrderStatus.PENDING) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = order.message,
                        fontSize = 11.sp,
                        color = statusColor
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "₹${String.format("%,.2f", order.totalValue)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = when (order.status) {
                        OrderStatus.PENDING -> "Processing..."
                        OrderStatus.SUCCESS -> "Completed"
                        OrderStatus.FAILED -> "Failed"
                    },
                    fontSize = 11.sp,
                    color = statusColor
                )
            }
            
            if (order.status != OrderStatus.PENDING) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = ext.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
