package com.example.threadpractice.model


data class CommentModel(
    val userId: String = "",
    val text: String = "",
    val username: String = "", // New field for username
    val name: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class ThreadModel(
    val thread : String = "",
    val image: String = "",
    val userId : String = "",
    val storeKey : String = "",
    val timeStamp : String = "",
    val likes: Map<String, Boolean> = emptyMap(),  // User IDs and their like status
    val comments: List<CommentModel> = emptyList()
    )
