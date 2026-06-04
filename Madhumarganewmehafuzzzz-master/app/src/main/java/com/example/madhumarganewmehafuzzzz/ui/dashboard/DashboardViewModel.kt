package com.example.madhumarganewmehafuzzzz.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madhumarganewmehafuzzzz.data.repository.HiveRepository
import com.example.madhumarganewmehafuzzzz.data.repository.InspectionRepository
import com.example.madhumarganewmehafuzzzz.domain.model.Hive
import com.example.madhumarganewmehafuzzzz.domain.model.InspectionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val hiveRepository: com.example.madhumarganewmehafuzzzz.data.repository.HiveRepository,
    private val inspectionRepository: com.example.madhumarganewmehafuzzzz.data.repository.InspectionRepository,
    private val diseaseRepository: com.example.madhumarganewmehafuzzzz.data.repository.DiseaseRepository
) : ViewModel() {

    private val _hives = MutableStateFlow<List<Hive>>(emptyList())
    val hives: StateFlow<List<Hive>> = _hives.asStateFlow()

    private val _inspections = MutableStateFlow<List<InspectionLog>>(emptyList())
    val inspections: StateFlow<List<InspectionLog>> = _inspections.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            hiveRepository.getHives().collect { list ->
                if (list.isEmpty()) {
                    seedInitialData()
                } else {
                    _hives.value = list
                }
            }
        }
    }

    private fun seedInitialData() {
        viewModelScope.launch {
            val sampleHives = listOf(
                Hive(hiveId = "HIVE-001", location = "North Orchard", condition = "Excellent", activityLevel = "High", honeyLevel = 85, latitude = 12.9716, longitude = 77.5946),
                Hive(hiveId = "HIVE-002", location = "South Field", condition = "Critical", activityLevel = "Low Activity", honeyLevel = 10, latitude = 12.9720, longitude = 77.5950),
                Hive(hiveId = "HIVE-003", location = "East Garden", condition = "Fair", activityLevel = "Medium", honeyLevel = 45, latitude = 12.9710, longitude = 77.5940)
            )
            sampleHives.forEach { hiveRepository.addHive(it) }
            
            // Also seed a default disease report for Admin Dashboard
            diseaseRepository.submitReport(
                com.example.madhumarganewmehafuzzzz.domain.model.DiseaseReport(
                    hiveId = "HIVE-002",
                    diseaseName = "Varroa Mites",
                    description = "Heavy infestation observed in sample HIVE-002. Immediate treatment required.",
                    severity = com.example.madhumarganewmehafuzzzz.domain.model.Severity.CRITICAL
                )
            )
        }
    }
}
