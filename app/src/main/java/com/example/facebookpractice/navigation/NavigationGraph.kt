package com.example.facebookpractice.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.facebookpractice.authentication.presentation.screens.SignIn
import com.example.facebookpractice.authentication.presentation.screens.SignUp
import com.example.facebookpractice.screens.HomePage
import com.example.facebookpractice.screens.LoadingScreen
import com.example.facebookpractice.screens.MainScreen

@Composable
fun NavigationGraph(
   navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screens.LoadingPage.route
    ) {
        composable(route = Screens.SignInScreen.route) {
            SignIn(navController = navController)
        }
        composable(route = Screens.SignUpScreen.route) {
            SignUp(navController = navController)
        }
        composable(route = Screens.HomePage.route) {
            HomePage(navController = navController)
        }
        composable(route = Screens.LoadingPage.route) {
            LoadingScreen(navController = navController)
        }
        composable(route = Screens.MainPage.route) {
            MainScreen(navController = navController)
        }
    }
}