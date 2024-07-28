package com.example.threadpractice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.ThreadModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ThreadViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("threads")

    // Assuming this is used to observe the thread data
    private val _thread = MutableLiveData<ThreadModel>()
    val thread: LiveData<ThreadModel> = _thread

    private val _likeThread = MutableLiveData<List<String>>()
    val likeThread: LiveData<List<String>> = _likeThread

    private val _dislikeThread = MutableLiveData<List<String>>()
    val dislikeThread: LiveData<List<String>> = _dislikeThread

    // Like a thread
    fun likeThread(threadId: String, userId: String) {
        val threadRef = userRef.child(threadId)
        threadRef.child("likes").child(userId).setValue(true)
        threadRef.child("dislikes").child(userId).removeValue()
    }

    // Dislike a thread
    fun dislikeThread(threadId: String, userId: String) {
        val threadRef = userRef.child(threadId)
        threadRef.child("dislikes").child(userId).setValue(true)
        threadRef.child("likes").child(userId).removeValue()
    }

    // Fetch the thread data to observe changes
    fun fetchThread(threadId: String) {
        userRef.child(threadId).get().addOnSuccessListener { snapshot ->
            _thread.value = snapshot.getValue(ThreadModel::class.java)
        }
    }
}





