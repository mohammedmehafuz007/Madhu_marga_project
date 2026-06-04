package com.example.madhumarganewmehafuzzzz.domain.model

data class FloraEntry(
    val flowerName: String,
    val bloomingMonths: List<String>,
    val honeyType: String,
    val nectarLevel: String // High, Medium, Low
)

val IndianFloraCalendar = listOf(
    FloraEntry("Mustard", listOf("December", "January", "February"), "Creamy White", "High"),
    FloraEntry("Eucalyptus", listOf("March", "April", "May"), "Strong Aroma", "Medium"),
    FloraEntry("Litchi", listOf("February", "March"), "Light Amber", "High"),
    FloraEntry("Sunflower", listOf("July", "August"), "Bright Yellow", "Medium"),
    FloraEntry("Karanja", listOf("April", "May"), "Dark Amber", "Medium")
)
