package com.example.madhumarganewmehafuzzzz.data.repository

import com.example.madhumarganewmehafuzzzz.domain.model.DiseaseReport
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiseaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val reportsCollection = firestore.collection("disease_reports")

    suspend fun submitReport(report: DiseaseReport): Result<Unit> = try {
        val docRef = reportsCollection.document()
        reportsCollection.document(docRef.id).set(report.copy(id = docRef.id)).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getAllReports(): Flow<List<DiseaseReport>> = callbackFlow {
        val subscription = reportsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val reports = snapshot?.toObjects(DiseaseReport::class.java) ?: emptyList()
            trySend(reports)
        }
        awaitClose { subscription.remove() }
    }

    suspend fun updateReportStatus(reportId: String, status: String): Result<Unit> = try {
        reportsCollection.document(reportId).update("status", status).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
