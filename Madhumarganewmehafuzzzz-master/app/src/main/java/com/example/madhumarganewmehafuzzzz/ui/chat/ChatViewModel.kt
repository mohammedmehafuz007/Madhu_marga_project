package com.example.madhumarganewmehafuzzzz.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * ChatViewModel manages the state of the AI assistant chat.
 * It uses Google's Gemini AI to provide expert beekeeping advice.
 */
@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Gemini API Key for AI Assistant
    private val apiKey = "AIzaSyCLiLTuHwuHwfleQVvbSxcXMrZuxD2ND2M"
    
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("You are a beekeeping expert assistant for the Madhu-Marga app. Help farmers with hive management, honey production, and disease prevention.") },
            content(role = "model") { text("Understood. I am ready to assist beekeepers with expert advice.") }
        )
    )

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(text, true)
        _messages.value = _messages.value + userMessage

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = chat.sendMessage(text)
                response.text?.let { responseText ->
                    _messages.value = _messages.value + ChatMessage(responseText, false)
                }
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage("Error: ${e.localizedMessage}", false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
