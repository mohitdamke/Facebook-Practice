package com.example.threadpractice.model

data class StoryModel(
    val imageStory: String = "",
    var userId: String = "",
    val timeStamp: Long = 0L,
    var storyKey: String = "" // Add this field
    )
