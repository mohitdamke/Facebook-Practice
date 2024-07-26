package com.example.threadpractice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.StoryModel
import com.example.threadpractice.model.ThreadModel
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.util.SharedPref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
        threadRef.orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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
        storyRef.orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val storyList = snapshot.children.mapNotNull {
                        it.getValue(StoryModel::class.java)
                    }
                    _story.postValue(storyList)
                }

                override fun onCancelled(error: DatabaseError) {
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

