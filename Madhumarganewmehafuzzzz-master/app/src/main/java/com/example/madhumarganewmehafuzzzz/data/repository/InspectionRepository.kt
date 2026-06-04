package com.example.madhumarganewmehafuzzzz.data.repository

import com.example.madhumarganewmehafuzzzz.domain.model.InspectionLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InspectionRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val inspectionsCollection = firestore.collection("inspections")

    fun getInspectionsForHive(hiveId: String): Flow<List<InspectionLog>> = callbackFlow {
        val subscription = inspectionsCollection
            .whereEqualTo("hiveId", hiveId)
            .orderBy("inspectionDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val logs = snapshot.toObjects(InspectionLog::class.java)
                    trySend(logs)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun addInspectionLog(log: InspectionLog) {
        val docRef = inspectionsCollection.document()
        val logWithId = log.copy(id = docRef.id)
        docRef.set(logWithId).await()
    }

    suspend fun deleteInspectionLog(logId: String) {
        inspectionsCollection.document(logId).delete().await()
    }
}
