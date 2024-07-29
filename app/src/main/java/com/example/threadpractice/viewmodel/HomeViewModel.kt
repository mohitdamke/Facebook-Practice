package com.example.threadpractice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.CommentModel
import com.example.threadpractice.model.ThreadModel
import com.example.threadpractice.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val thread = db.getReference("threads")

    private var _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    val threadsAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadsAndUsers

    private var _savedThreads = MutableLiveData<List<ThreadModel>>()
    val savedThreads: LiveData<List<ThreadModel>> = _savedThreads

    private val _savedThreadIds = MutableLiveData<List<String>>()
    val savedThreadIds: LiveData<List<String>> = _savedThreadIds


    init {
        fetchThreadsAndUsers {
            _threadsAndUsers.value = it
        }
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        fetchSavedThreads(currentUserId)

        toggleSaveThread(currentUserId)
    }

    private fun fetchThreadsAndUsers(onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit) {

        thread.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Pair<ThreadModel, UserModel>>()

                for (threadSnapshot in snapshot.children) {

                    val thread = threadSnapshot.getValue(ThreadModel::class.java)
                    thread.let {
                        fetchUserFromThread(it!!) { user ->
                            result.add(0, it to user)

                            if (result.size == snapshot.childrenCount.toInt()) {
                                onResult(result)
                            }

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun fetchUserFromThread(thread: ThreadModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun toggleLike(threadId: String, userId: String) {
        val threadRef = FirebaseDatabase.getInstance().getReference("threads").child(threadId)
        threadRef.child("likes").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // User has already liked, remove the like
                        threadRef.child("likes").child(userId).removeValue()
                    } else {
                        // User has not liked, add the like
                        threadRef.child("likes").child(userId).setValue(true)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }


    fun addComment(
        threadId: String,
        userId: String,
        username: String,
        name: String,
        commentText: String
    ) {
        val threadRef = db.getReference("threads").child(threadId)
        threadRef.child("comments").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentsList = mutableListOf<CommentModel>()
                snapshot.children.mapNotNullTo(commentsList) { it.getValue(CommentModel::class.java) }

                val newComment = CommentModel(
                    userId = userId, username = username,
                    name = name, text = commentText
                )
                commentsList.add(newComment)

                threadRef.child("comments").setValue(commentsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    fun fetchComments(threadId: String, onResult: (List<CommentModel>) -> Unit) {
        val threadRef =
            FirebaseDatabase.getInstance().getReference("threads").child(threadId).child("comments")
        threadRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments =
                    snapshot.children.mapNotNull { it.getValue(CommentModel::class.java) }
                onResult(comments)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun toggleSaveThread(threadId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef = db.getReference("users").child(currentUserId).child("savedThreads")

        userRef.child(threadId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Thread is already saved, unsave it
                    userRef.child(threadId).removeValue()
                } else {
                    // Thread is not saved, save it
                    userRef.child(threadId).setValue(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun fetchSavedThreads(userId: String): LiveData<List<ThreadModel>> {
        val savedThreadsLiveData = MutableLiveData<List<ThreadModel>>()

        db.getReference("users").child(userId).child("savedThreads")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val threadIds = snapshot.children.mapNotNull { it.key }
                    fetchThreadsByIds(threadIds) { threads ->
                        savedThreadsLiveData.value = threads
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        return savedThreadsLiveData
    }

    private fun fetchThreadsByIds(threadIds: List<String>, onResult: (List<ThreadModel>) -> Unit) {
        db.getReference("threads").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val threads = threadIds.mapNotNull { id ->
                    snapshot.child(id).getValue(ThreadModel::class.java)
                }
                onResult(threads)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun getUserById(userId: String): LiveData<UserModel> {
        val userLiveData = MutableLiveData<UserModel>()
        db.getReference("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    userLiveData.value = user?: UserModel()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        return userLiveData
    }




}

