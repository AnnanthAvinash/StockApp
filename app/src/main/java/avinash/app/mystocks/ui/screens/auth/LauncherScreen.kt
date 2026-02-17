package avinash.app.mystocks.ui.screens.auth

import android.app.Activity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import avinash.app.mystocks.util.BiometricHelper
import avinash.app.mystocks.util.BiometricStatus
import avinash.app.mystocks.ui.theme.AppTheme

@Composable
fun LauncherScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthenticated: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onAuthenticated()
        }
    }
    
    LaunchedEffect(Unit) {
        if (state.mode == AuthMode.Biometric) {
            val biometricHelper = BiometricHelper(activity)
            when (biometricHelper.canAuthenticate()) {
                BiometricStatus.Available -> {
                    biometricHelper.showBiometricPrompt(
                        onSuccess = { viewModel.onBiometricSuccess() },
                        onError = { code, msg -> viewModel.onBiometricError(code, msg) },
                        onFailed = { viewModel.onBiometricFailed() }
                    )
                }
                else -> {
                    viewModel.switchToPin()
                }
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ext.gradientStart,
                        ext.gradientEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            Icon(
                imageVector = Icons.Default.ShowChart,
                contentDescription = "App Logo",
                modifier = Modifier.size(80.dp),
                tint = colorScheme.secondary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "MyStocks",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            AnimatedContent(
                targetState = state.mode,
                transitionSpec = {
                    fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutHorizontally()
                },
                label = "auth_mode"
            ) { mode ->
                when (mode) {
                    AuthMode.Biometric -> BiometricContent(
                        attempts = state.biometricAttempts,
                        onRetry = {
                            val biometricHelper = BiometricHelper(activity)
                            biometricHelper.showBiometricPrompt(
                                onSuccess = { viewModel.onBiometricSuccess() },
                                onError = { code, msg -> viewModel.onBiometricError(code, msg) },
                                onFailed = { viewModel.onBiometricFailed() }
                            )
                        },
                        onUsePin = { viewModel.switchToPin() }
                    )
                    AuthMode.Pin -> PinContent(
                        enteredPin = state.enteredPin,
                        error = state.pinError,
                        onDigitClick = { viewModel.onPinDigitEntered(it) },
                        onBackspace = { viewModel.onPinBackspace() },
                        onClear = { viewModel.clearPin() }
                    )
                }
            }
        }
    }
}

@Composable
private fun BiometricContent(
    attempts: Int,
    onRetry: () -> Unit,
    onUsePin: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val ext = AppTheme.extendedColors
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )
        
        Icon(
            imageVector = Icons.Default.Fingerprint,
            contentDescription = "Biometric",
            modifier = Modifier
                .size(100.dp)
                .scale(scale)
                .clickable { onRetry() },
            tint = colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Touch to authenticate",
            fontSize = 18.sp,
            color = colorScheme.onBackground
        )
        
        if (attempts > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Attempt $attempts of 3",
                fontSize = 14.sp,
                color = colorScheme.error
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        TextButton(onClick = onUsePin) {
            Text(
                text = "Use PIN instead",
                color = ext.textSecondary
            )
        }
    }
}

@Composable
private fun PinContent(
    enteredPin: String,
    error: String?,
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Enter PIN",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(4) { index ->
                PinDot(filled = index < enteredPin.length)
            }
        }
        
        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut()
        ) {
            error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(40.dp))
        
        PinKeypad(
            onDigitClick = onDigitClick,
            onBackspace = onBackspace,
            onClear = onClear
        )
    }
}

@Composable
private fun PinDot(filled: Boolean) {
    val colorScheme = MaterialTheme.colorScheme
    val animatedSize by animateDpAsState(
        targetValue = if (filled) 20.dp else 16.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "dot_size"
    )
    
    Box(
        modifier = Modifier
            .size(animatedSize)
            .clip(CircleShape)
            .background(
                if (filled) colorScheme.secondary else Color.Transparent
            )
            .border(
                width = 2.dp,
                color = if (filled) colorScheme.secondary else colorScheme.outline,
                shape = CircleShape
            )
    )
}

@Composable
private fun PinKeypad(
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("C", "0", "⌫")
    )
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        keys.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                row.forEach { key ->
                    KeypadButton(
                        key = key,
                        onClick = {
                            when (key) {
                                "⌫" -> onBackspace()
                                "C" -> onClear()
                                else -> onDigitClick(key)
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun KeypadButton(
    key: String,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (key == "⌫") {
            Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = "Backspace",
                tint = colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = key,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = colorScheme.onSurface
            )
        }
    }
}
