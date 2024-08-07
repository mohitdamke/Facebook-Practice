package com.example.threadpractice.model

data class ChatModel(
    val senderId: String = "",
    val receiverId: String = "",
    val messageText: String = "",
    val storeKey : String = "",
    val timestamp: Long = System.currentTimeMillis()
)