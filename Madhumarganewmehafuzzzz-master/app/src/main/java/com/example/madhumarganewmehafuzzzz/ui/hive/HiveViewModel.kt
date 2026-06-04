package com.example.madhumarganewmehafuzzzz.ui.hive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madhumarganewmehafuzzzz.data.repository.HiveRepository
import com.example.madhumarganewmehafuzzzz.domain.model.Hive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HiveViewModel @Inject constructor(
    private val repository: HiveRepository
) : ViewModel() {

    private val _hives = MutableStateFlow<List<Hive>>(emptyList())
    val hives: StateFlow<List<Hive>> = _hives.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getHives().collect {
                _hives.value = it
            }
        }
    }

    fun addHive(hive: Hive) {
        viewModelScope.launch {
            repository.addHive(hive)
        }
    }

    fun updateHive(hive: Hive) {
        viewModelScope.launch {
            repository.updateHive(hive)
        }
    }

    fun deleteHive(hiveId: String) {
        viewModelScope.launch {
            repository.deleteHive(hiveId)
        }
    }
}
