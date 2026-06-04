package com.example.madhumarganewmehafuzzzz.domain.model

data class Hive(
    val id: String = "",
    val userId: String = "",
    val hiveId: String = "",
    val location: String = "",
    val condition: String = "",
    val queenStatus: String = "",
    val honeyLevel: Int = 0, // 0-100
    val activityLevel: String = "",
    val imageUrls: List<String> = emptyList(),
    val qrCodeData: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val lastInspectionDate: Long = System.currentTimeMillis()
)
