package com.example.madhumarganewmehafuzzzz.data.repository

import com.example.madhumarganewmehafuzzzz.domain.model.User
import com.example.madhumarganewmehafuzzzz.domain.model.Hive
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    fun getAllFarmers(): Flow<List<User>> = callbackFlow {
        val subscription = usersCollection
            .whereEqualTo("role", "FARMER")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val farmers = snapshot?.toObjects(User::class.java) ?: emptyList()
                trySend(farmers)
            }
        awaitClose { subscription.remove() }
    }

    fun getFarmerHives(userId: String): Flow<List<Hive>> = callbackFlow {
        val subscription = firestore.collection("hives")
            .whereEqualTo("userId", userId) // Assuming hives have a userId field
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val hives = snapshot?.toObjects(Hive::class.java) ?: emptyList()
                trySend(hives)
            }
        awaitClose { subscription.remove() }
    }
}
