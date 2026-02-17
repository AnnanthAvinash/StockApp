package avinash.app.mystocks.ui.screens.auth

import androidx.lifecycle.ViewModel
import avinash.app.mystocks.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AuthState(
    val mode: AuthMode = AuthMode.Biometric,
    val biometricAttempts: Int = 0,
    val enteredPin: String = "",
    val pinError: String? = null,
    val isAuthenticated: Boolean = false
)

sealed class AuthMode {
    object Biometric : AuthMode()
    object Pin : AuthMode()
}

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    
    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()
    
    fun onBiometricSuccess() {
        _state.update { it.copy(isAuthenticated = true) }
    }
    
    fun onBiometricFailed() {
        val newAttempts = _state.value.biometricAttempts + 1
        _state.update { 
            it.copy(
                biometricAttempts = newAttempts,
                mode = if (newAttempts >= Constants.MAX_BIOMETRIC_ATTEMPTS) AuthMode.Pin else AuthMode.Biometric
            )
        }
    }
    
    fun onBiometricError(errorCode: Int, errorMessage: String) {
        // User clicked "Use PIN instead" or other error
        _state.update { it.copy(mode = AuthMode.Pin) }
    }
    
    fun switchToPin() {
        _state.update { it.copy(mode = AuthMode.Pin) }
    }
    
    fun onPinDigitEntered(digit: String) {
        if (_state.value.enteredPin.length < 4) {
            _state.update { 
                it.copy(
                    enteredPin = it.enteredPin + digit,
                    pinError = null
                )
            }
            
            // Auto-verify when 4 digits entered
            if (_state.value.enteredPin.length == 4) {
                verifyPin()
            }
        }
    }
    
    fun onPinBackspace() {
        if (_state.value.enteredPin.isNotEmpty()) {
            _state.update { 
                it.copy(
                    enteredPin = it.enteredPin.dropLast(1),
                    pinError = null
                )
            }
        }
    }
    
    fun clearPin() {
        _state.update { it.copy(enteredPin = "", pinError = null) }
    }
    
    private fun verifyPin() {
        if (_state.value.enteredPin == Constants.CORRECT_PIN) {
            _state.update { it.copy(isAuthenticated = true) }
        } else {
            _state.update { 
                it.copy(
                    enteredPin = "",
                    pinError = "Incorrect PIN. Please try again."
                )
            }
        }
    }
}
