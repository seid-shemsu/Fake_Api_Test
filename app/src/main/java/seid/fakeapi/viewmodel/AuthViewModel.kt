package seid.fakeapi.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import seid.fakeapi.data.repo.auth.AuthRepository
import seid.fakeapi.model.auth.LoginRequest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    fun login(username: String, password: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val token = repository.login(LoginRequest(username, password)).token
                sharedPreferences.edit().putString("token", token).apply()
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Login failed: ${e.message}")
            }
        }
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    fun checkLogin() {
        _uiState.value = UiState.Idle
        val isLoggedIn = sharedPreferences.contains("token")
        if (isLoggedIn) {
            _uiState.value = UiState.Success
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }
}
