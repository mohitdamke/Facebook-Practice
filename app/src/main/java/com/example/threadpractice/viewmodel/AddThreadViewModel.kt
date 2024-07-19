package com.example.threadpractice.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.ThreadModel
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.util.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddThreadViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("threads")

    private val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted


     fun saveImage(
      thread : String,
      userId : String,
      imageUri: Uri
    ) {

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveData(thread, userId, uri.toString())
            }
        }


    }

     fun saveData(
        thread : String,
        userId : String,
        image: String
    ) {

        val threadData = ThreadModel(
            thread,
            userId,
            image,
            System.currentTimeMillis().toString()
        )

        userRef.child(userRef.push().key!!).setValue(threadData)
            .addOnSuccessListener {
                _isPosted.value = true
            }.addOnFailureListener {
                _isPosted.value = false
            }


    }


}










