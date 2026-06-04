package com.example.madhumarganewmehafuzzzz.domain.model

data class InspectionLog(
    val id: String = "",
    val hiveId: String = "",
    val inspectionDate: Long = System.currentTimeMillis(),
    val observations: String = "",
    val pestsFound: List<String> = emptyList(),
    val diseasesFound: List<String> = emptyList(),
    val notes: String = "",
    val imageUrl: String? = null,
    val photoUrls: List<String> = emptyList()
)
