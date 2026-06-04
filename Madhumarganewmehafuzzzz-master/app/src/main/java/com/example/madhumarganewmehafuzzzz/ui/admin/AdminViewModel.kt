package com.example.madhumarganewmehafuzzzz.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madhumarganewmehafuzzzz.data.repository.AdminRepository
import com.example.madhumarganewmehafuzzzz.data.repository.DiseaseRepository
import com.example.madhumarganewmehafuzzzz.domain.model.DiseaseReport
import com.example.madhumarganewmehafuzzzz.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val diseaseRepository: DiseaseRepository
) : ViewModel() {

    val farmers: StateFlow<List<User>> = adminRepository.getAllFarmers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<DiseaseReport>> = diseaseRepository.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        checkAndSeed()
    }

    private fun checkAndSeed() {
        viewModelScope.launch {
            // If reports are empty, we seed some default content
            reports.collect {
                if (it.isEmpty()) {
                    seedDefaultReports()
                }
            }
        }
    }

    private suspend fun seedDefaultReports() {
        val defaultReports = listOf(
            DiseaseReport(diseaseName = "American Foulbrood", description = "Reported in Southern Region hives. Requires immediate quarantine.", severity = com.example.madhumarganewmehafuzzzz.domain.model.Severity.CRITICAL),
            DiseaseReport(diseaseName = "Chalkbrood", description = "Common fungal infection detected in moisture-heavy areas.", severity = com.example.madhumarganewmehafuzzzz.domain.model.Severity.MEDIUM),
            DiseaseReport(diseaseName = "Small Hive Beetle", description = "Observed during peak monsoon. Advised to use beetle traps.", severity = com.example.madhumarganewmehafuzzzz.domain.model.Severity.HIGH)
        )
        defaultReports.forEach { diseaseRepository.submitReport(it) }
    }

    fun updateReportStatus(reportId: String, status: String) {
        viewModelScope.launch {
            diseaseRepository.updateReportStatus(reportId, status)
        }
    }
}
