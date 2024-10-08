package com.example.threadpractice.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.threadpractice.geminiAi.domain.ChatState
import com.example.threadpractice.model.Chat
import com.example.threadpractice.model.Message
import com.google.android.play.integrity.internal.c
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PeopleChatViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chats")

    private val _chatState = MutableStateFlow(Chat())
    val chatState: StateFlow<Chat> get() = _chatState

    var currentMessage by mutableStateOf("")

    fun fetchMessages(chatId: String) {
        chatRef.child(chatId).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableMapOf<String, Message>()
                snapshot.children.forEach {
                    val message = it.getValue(Message::class.java)
                    message?.let { msg -> messages[it.key!!] = msg }
                }
                _chatState.value = chatState.value.copy(messages = messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun sendMessage(chatId: String) {
        val messageId = chatRef.child(chatId).child("messages").push().key ?: return
        val message = Message(
            senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            text = currentMessage,
            timestamp = System.currentTimeMillis()
        )
        chatRef.child(chatId).child("messages").child(messageId).setValue(message)
        currentMessage = ""
    }
}

