package com.example.facebookpractice

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    data class Loaded(
        val avatarUrl: String,
        val posts: List<Post>,
    ) : HomeScreenState()

    object SignInRequired : HomeScreenState()
}