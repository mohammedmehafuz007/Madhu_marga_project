package com.example.madhumarganewmehafuzzzz.domain.model

data class HarvestLog(
    val id: String = "",
    val hiveId: String = "",
    val harvestDate: Long = System.currentTimeMillis(),
    val quantityKg: Double = 0.0,
    val quality: String = "",
    val notes: String = ""
)
