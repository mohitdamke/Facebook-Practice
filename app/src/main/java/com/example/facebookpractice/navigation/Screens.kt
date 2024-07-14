package com.example.facebookpractice.navigation

sealed class Screens(val route: String) {

    data object SignInScreen : Screens(route = "SignIn_Screen")
    data object HomePage : Screens(route = "HomePage")
    data object SignUpScreen : Screens(route = "SignUp_Screen")
    data object LoadingPage : Screens(route = "Loading_Screen")
    data object MainPage : Screens(route = "Main_Screen")
}