package com.example.madhumarganewmehafuzzzz.domain.model

data class DiseaseReport(
    val id: String = "",
    val hiveId: String = "",
    val userId: String = "",
    val diseaseName: String = "",
    val description: String = "",
    val severity: Severity = Severity.LOW,
    val reportedAt: Long = System.currentTimeMillis(),
    val status: ReportStatus = ReportStatus.PENDING,
    val imageUrl: String? = null
)

enum class Severity {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum class ReportStatus {
    PENDING, INVESTIGATING, RESOLVED
}
