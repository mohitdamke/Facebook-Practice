package com.example.threadpractice.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threadpractice.model.StoryModel
import com.example.threadpractice.model.ThreadModel
import com.example.threadpractice.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class UserViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val threadRef = db.getReference("threads")
    val storyRef = db.getReference("story")
    val userRef = db.getReference("users")

    private val _threads = MutableLiveData(listOf<ThreadModel>())
    val threads: LiveData<List<ThreadModel>> get() = _threads

    private val _story = MutableLiveData(listOf<StoryModel>())
    val story: LiveData<List<StoryModel>> get() = _story

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList: LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList: LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())
    val users: LiveData<UserModel> get() = _users

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess


    private var job: Job? = null

    init {
        startStoryCleanupTask()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()  // Cancel the job when the ViewModel is cleared
    }

    private fun startStoryCleanupTask() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                deleteExpiredStories()
                delay(TimeUnit.HOURS.toMillis(1))  // Check every hour
            }
        }
    }

    private fun deleteExpiredStories() {
        val currentTime = System.currentTimeMillis()
        val cutoffTime = currentTime - TimeUnit.HOURS.toMillis(24)

        storyRef.orderByChild("timeStamp").endAt(cutoffTime.toDouble()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { storySnapshot ->
                    val story = storySnapshot.getValue(StoryModel::class.java)
                    val storyKey = storySnapshot.key

                    if (storyKey != null && story != null) {
                        // Remove the story from the database
                        storyRef.child(storyKey).removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                story.imageStory?.let { imageUrl ->
                                    if (imageUrl.isNotEmpty()) {
                                        Firebase.storage.getReferenceFromUrl(imageUrl)
                                            .delete()
                                            .addOnSuccessListener {
                                                Log.d("DeleteStory", "Image deleted successfully from storage")
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.d("DeleteStory", "Failed to delete image from storage: ${exception.message}")
                                            }
                                    }
                                }
                                Log.d("DeleteStory", "Story deleted successfully from database")
                            } else {
                                Log.d("DeleteStory", "Failed to delete story from database: ${task.exception?.message}")
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DeleteStory", "Failed to delete story: ${error.message}")
            }
        })
    }


    fun fetchUsers(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(UserModel::class.java)
                _users.value = user!!
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })
    }


    fun fetchThreads(uid: String) {
        threadRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val threadList = snapshot.children.mapNotNull {
                    it.getValue(ThreadModel::class.java)
                }
                _threads.postValue(threadList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun fetchStory(uid: String) {
        storyRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val storyList = snapshot.children.mapNotNull {
                    val story = it.getValue(StoryModel::class.java)
                    story?.apply {
                        storyKey = it.key ?: "" // Set the key for each story
                    }
                }
                _story.postValue(storyList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FetchStory", "Failed to fetch stories: ${error.message}")
            }
        })
    }

    fun deleteStory(storyKey: String) {
        storyRef.child(storyKey).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val story = snapshot.getValue(StoryModel::class.java)

                if (story != null) {
                    storyRef.child(storyKey).removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            story.imageStory?.let { imageUrl ->
                                if (imageUrl.isNotEmpty()) {
                                    Firebase.storage.getReferenceFromUrl(imageUrl)
                                        .delete()
                                        .addOnSuccessListener {
                                            Log.d("DeleteStory", "Image deleted successfully from storage")
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.d("DeleteStory", "Failed to delete image from storage: ${exception.message}")
                                        }
                                }
                            }
                            Log.d("DeleteStory", "Story deleted successfully from database")
                            val currentStoryList = _story.value?.toMutableList()
                            currentStoryList?.remove(story)
                            _story.postValue(currentStoryList)
                            _deleteSuccess.postValue(true)
                        } else {
                            Log.d("DeleteStory", "Failed to delete story from database: ${task.exception?.message}")
                            _deleteSuccess.postValue(false)
                        }
                    }
                } else {
                    Log.d("DeleteStory", "Story with key $storyKey not found")
                    _deleteSuccess.postValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DeleteStory", "Failed to delete story: ${error.message}")
            }
        })
    }

    private val firestoreDb = Firebase.firestore
    fun followOrUnfollowUser(userId: String, currentUserId: String, isFollowing: Boolean) {
        val followingRef = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        if (isFollowing) {
            followingRef.update("followingIds", FieldValue.arrayRemove(userId))
            followerRef.update("followerIds", FieldValue.arrayRemove(currentUserId))
        } else {
            followingRef.update("followingIds", FieldValue.arrayUnion(userId))
            followerRef.update("followerIds", FieldValue.arrayUnion(currentUserId))
        }
    }

    fun getFollowers(userId: String) {
        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener { snapshot, error ->

                val followerIds = snapshot?.get("followerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerIds)
            }
    }

        fun getFollowing(userId: String) {
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener { snapshot, error ->

                val followerIds = snapshot?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followerIds)
            }
    }


}

