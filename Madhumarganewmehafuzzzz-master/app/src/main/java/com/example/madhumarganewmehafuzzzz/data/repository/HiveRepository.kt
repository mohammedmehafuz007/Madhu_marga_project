package com.example.madhumarganewmehafuzzzz.data.repository

import com.example.madhumarganewmehafuzzzz.domain.model.Hive
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HiveRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val hivesCollection = firestore.collection("hives")

    fun getHives(): Flow<List<Hive>> = callbackFlow {
        val subscription = hivesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val hives = snapshot.toObjects(Hive::class.java)
                trySend(hives)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun addHive(hive: Hive) {
        val docRef = hivesCollection.document()
        val hiveWithId = hive.copy(id = docRef.id)
        docRef.set(hiveWithId).await()
    }

    suspend fun updateHive(hive: Hive) {
        hivesCollection.document(hive.id).set(hive).await()
    }

    suspend fun deleteHive(hiveId: String) {
        hivesCollection.document(hiveId).delete().await()
    }

    suspend fun getHiveById(id: String): Hive? {
        return hivesCollection.document(id).get().await().toObject(Hive::class.java)
    }
}
