package com.example.threadpractice.navigation

sealed class Routes(val routes: String) {
    object Home : Routes("home")
    object Profile : Routes("profile")
    object Search : Routes("search")
    object Splash : Routes("splash")
    object AddThread : Routes("add_thread")
    object BottomNav : Routes("bottom_nav")
    object Login : Routes("login")
    object Register : Routes("register")
    object AddStory : Routes("add_story")
    object StoryDetail : Routes("story_detail")
    object OtherUsers : Routes("other_users/{data}")
    object AllStory : Routes("all_story/{all_story}")
    object Setting : Routes("setting")
    object Comments : Routes("comments/{data}")
    object Saved : Routes("saved")
    object EditProfile : Routes("edit_profile")
    object ChatGemini : Routes("chat_gemini")
    object AllChat : Routes("all_chat")
    object ChatPeople : Routes("chat_people/{data}")




}