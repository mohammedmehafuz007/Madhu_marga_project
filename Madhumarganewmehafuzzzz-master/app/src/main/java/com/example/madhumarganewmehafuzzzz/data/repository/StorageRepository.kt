package com.example.madhumarganewmehafuzzzz.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor() {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun uploadHiveImage(uri: Uri, hiveId: String): Result<String> = try {
        val imageRef = storageRef.child("hives/$hiveId/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageRef.putFile(uri).await()
        val downloadUrl = imageRef.downloadUrl.await()
        Result.success(downloadUrl.toString())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun uploadInspectionImage(uri: Uri, inspectionId: String): Result<String> = try {
        val imageRef = storageRef.child("inspections/$inspectionId/${System.currentTimeMillis()}.jpg")
        imageRef.putFile(uri).await()
        val downloadUrl = imageRef.downloadUrl.await()
        Result.success(downloadUrl.toString())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
