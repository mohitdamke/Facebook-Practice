package com.example.threadpractice.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.ThreadModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddThreadViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val threadRef = db.getReference("threads")


    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted
    fun saveImages(
        thread: String,
        userId: String,
        imageUris: List<Uri>
    ) {
        val storyKey = threadRef.push().key ?: return
        val storageRef = Firebase.storage.reference
        val uploadTasks = imageUris.mapIndexed { index, uri ->
            val imageRef = storageRef.child("threads/${storyKey}_$index.jpg")
            imageRef.putFile(uri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }
        }

        // Wait for all uploads to complete
        Tasks.whenAllComplete(uploadTasks).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val imageUris = uploadTasks.map { it.result.toString() }
                saveData(
                    thread = thread,
                    userId = userId,
                    storeKey = storyKey,
                    images = imageUris
                )
            } else {
                _isPosted.value = false
            }
        }
    }

    fun saveData(
        thread: String,
        userId: String,
        images: List<String>,
        storeKey: String
    ) {
        val newThreadRef = threadRef.child(storeKey)
        val threadData = ThreadModel(
            thread = thread,
            images = images,
            userId = userId,
            storeKey = storeKey,
            timeStamp = System.currentTimeMillis().toString(),
            likes = emptyMap(),
            comments = emptyList()
        )

        newThreadRef.setValue(threadData).addOnSuccessListener {
            _isPosted.value = true
        }.addOnFailureListener {
            _isPosted.value = false
        }
    }
}

//    fun savedImage(
//        thread: String, userId: String, imageUri: Uri
//    ) {
//        val storyKey = threadRef.push().key ?: return
//
//        val storageRef = Firebase.storage.reference
//        val imageRef = storageRef.child("threads/${storyKey}.jpg")
//
//        val uploadTask = imageRef.putFile(imageUri)
//        uploadTask.addOnSuccessListener {
//            imageRef.downloadUrl.addOnSuccessListener { uri ->
//                saveData(
//                    thread = thread,
//                    userId = userId,
//                    storeKey = storyKey,
//                    image = uri.toString()
//                )
//            }
//        }
//    }
//
//    fun saveDdata(
//        thread: String, userId: String, image: String, storeKey: String
//    ) {
//        val newThreadRef = threadRef.push()
//        val storyKey = newThreadRef.key ?: return
//        val threadData = ThreadModel(
//            thread = thread,
//            image = image,
//            userId = userId,
//            storeKey = storyKey,
//            timeStamp = System.currentTimeMillis().toString(),
//            likes = emptyMap(),
//            comments = emptyList(),
//        )
//
//
//        newThreadRef.setValue(threadData).addOnSuccessListener {
//            _isPosted.value = true
//        }.addOnFailureListener {
//            _isPosted.value = false
//        }
////        threadRef.child(threadRef.push().key!!).setValue(threadData).addOnSuccessListener {
////            _isPosted.value = true
////        }.addOnFailureListener {
////            _isPosted.value = false
////        }
//
//
//    }













