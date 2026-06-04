package com.example.madhumarganewmehafuzzzz.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madhumarganewmehafuzzzz.data.repository.AuthRepository
import com.example.madhumarganewmehafuzzzz.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _user = mutableStateOf<User?>(repository.getCurrentUser())
    val user: State<User?> = _user

    fun logout() {
        repository.signOut()
        _user.value = null
    }

    // Sign in logic would go here
}
