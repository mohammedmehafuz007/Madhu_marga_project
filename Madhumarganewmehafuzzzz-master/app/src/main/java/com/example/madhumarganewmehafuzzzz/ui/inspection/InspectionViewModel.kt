package com.example.madhumarganewmehafuzzzz.ui.inspection

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madhumarganewmehafuzzzz.data.repository.InspectionRepository
import com.example.madhumarganewmehafuzzzz.data.repository.StorageRepository
import com.example.madhumarganewmehafuzzzz.domain.model.InspectionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionViewModel @Inject constructor(
    private val repository: InspectionRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _inspections = MutableStateFlow<List<InspectionLog>>(emptyList())
    val inspections: StateFlow<List<InspectionLog>> = _inspections.asStateFlow()

    fun loadInspections(hiveId: String) {
        viewModelScope.launch {
            repository.getInspectionsForHive(hiveId).collect {
                _inspections.value = it
            }
        }
    }

    fun addInspectionLog(log: InspectionLog) {
        viewModelScope.launch {
            repository.addInspectionLog(log)
        }
    }

    suspend fun uploadImage(uri: Uri, hiveId: String): Result<String> {
        return storageRepository.uploadInspectionImage(uri, hiveId)
    }

    fun deleteInspectionLog(logId: String) {
        viewModelScope.launch {
            repository.deleteInspectionLog(logId)
        }
    }
}
