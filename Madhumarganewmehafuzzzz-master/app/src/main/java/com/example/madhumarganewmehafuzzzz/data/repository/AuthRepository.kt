package com.example.madhumarganewmehafuzzzz.data.repository

import com.example.madhumarganewmehafuzzzz.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.let {
            User(it.uid, it.email, it.displayName, it.phoneNumber)
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    // Additional methods for Google Sign-In, OTP, etc. would go here
}
