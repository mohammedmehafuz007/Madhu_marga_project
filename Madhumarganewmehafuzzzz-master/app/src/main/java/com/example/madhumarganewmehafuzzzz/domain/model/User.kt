package com.example.madhumarganewmehafuzzzz.domain.model

data class User(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val phoneNumber: String?,
    val role: UserRole = UserRole.FARMER
)

enum class UserRole {
    ADMIN, FARMER
}
