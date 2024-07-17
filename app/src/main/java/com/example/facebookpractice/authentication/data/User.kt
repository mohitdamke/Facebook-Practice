package com.example.facebookpractice.authentication.data

data class User(
    val name : String = "",
    val username : String = "",
    val userId : String = "",
    val email : String = "",
    val password : String = "",
    val surname : String = "",
    val imageUrl : String = "",
    val following :List<String> = emptyList(),
    val follower : List<String> = emptyList(),
    val bio : String = "",
    val url : String = "",
    val totalPosts : String = "",
)