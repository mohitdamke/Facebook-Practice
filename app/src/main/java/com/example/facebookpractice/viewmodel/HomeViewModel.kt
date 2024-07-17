package com.example.facebookpractice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facebookpractice.HomeScreenState
import com.example.facebookpractice.Post
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {


    private fun observePosts(): Flow<List<Post>> {
        return callbackFlow {
            val listener = Firebase.firestore
                .collection("posts").addSnapshotListener { value, error ->
                    if (error != null) {
                        close(error)
                    } else if (value != null) {
                        val posts = value.map { doc ->
                            Post(
                                text = doc.getString("text").orEmpty(),
                                timestamp = doc.getDate("date_posted") ?: Date(),
                                authorName = doc.getString("author_name").orEmpty(),
                                authorAvatarUrl = doc.getString("author_avatar_url").orEmpty()
                            )
                        }.sortedByDescending { it.timestamp }
                        trySend(posts)
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

























}