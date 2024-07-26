package com.example.threadpractice.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.StoryModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddStoryViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val storyRef = db.getReference("story")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("story/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted


    fun saveImage(
        userId: String, uidStory: String, imageUri: Uri
    ) {

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveData(userId = userId, uidStory = uidStory, image = uri.toString())
            }
        }


    }

    fun saveData(
        userId: String, uidStory: String, image: String
    ) {

        val storyData = StoryModel(
            imageStory = image,
            userId = userId,
            timeStamp = System.currentTimeMillis().toString(),
            uidStory = uidStory
        )

        storyRef.child(storyRef.push().key!!).setValue(storyData).addOnSuccessListener {
            _isPosted.value = true
        }.addOnFailureListener {
            _isPosted.value = false
        }


    }


}










