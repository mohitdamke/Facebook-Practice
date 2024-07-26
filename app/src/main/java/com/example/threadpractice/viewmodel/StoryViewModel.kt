package com.example.threadpractice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadpractice.model.StoryModel
import com.example.threadpractice.model.ThreadModel
import com.example.threadpractice.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User

class StoryViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val story = db.getReference("story")

    private var _storyAndUsers = MutableLiveData<List<Pair<StoryModel, UserModel>>>()
    val storyAndUsers: LiveData<List<Pair<StoryModel, UserModel>>> = _storyAndUsers

    init {
        fetchStoryAndUsers {
            _storyAndUsers.value = it
        }
    }

    private fun fetchStoryAndUsers(onResult: (List<Pair<StoryModel, UserModel>>) -> Unit) {

        story.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Pair<StoryModel, UserModel>>()

                for (storySnapshot in snapshot.children) {

                    val story = storySnapshot.getValue(StoryModel::class.java)
                    story.let {
                        fetchUserFromStory(it!!) { user ->
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

    private fun fetchUserFromStory(story: StoryModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(story.userId)
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

}

