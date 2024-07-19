package com.example.threadpractice.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadpractice.screens.AddThreads
import com.example.threadpractice.screens.BottomNav
import com.example.threadpractice.screens.Home
import com.example.threadpractice.screens.Login
import com.example.threadpractice.screens.Notification
import com.example.threadpractice.screens.Profile
import com.example.threadpractice.screens.Register
import com.example.threadpractice.screens.Search
import com.example.threadpractice.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(route = Routes.Splash.routes) {
            Splash(navController = navController)
        }

        composable(route = Routes.Home.routes) {
            Home(navController = navController)
        }

        composable(route = Routes.Login.routes) {
            Login(navController = navController)
        }

        composable(route = Routes.Register.routes) {
            Register(navController = navController)
        }

        composable(route = Routes.Notification.routes) {
            Notification(navController = navController)
        }

        composable(route = Routes.Search.routes) {
            Search(navController = navController)
        }


        composable(route = Routes.Profile.routes) {
            Profile(navController = navController)
        }

        composable(route = Routes.AddThread.routes) {
            AddThreads(navController = navController)
        }

        composable(route = Routes.BottomNav.routes) {
            BottomNav(navController = navController)
        }


    }
}