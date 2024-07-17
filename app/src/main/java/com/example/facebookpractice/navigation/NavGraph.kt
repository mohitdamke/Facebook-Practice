package com.example.facebookpractice.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.facebookpractice.screens.AddThreads
import com.example.facebookpractice.screens.BottomNav
import com.example.facebookpractice.screens.Home
import com.example.facebookpractice.screens.Login
import com.example.facebookpractice.screens.Notification
import com.example.facebookpractice.screens.Profile
import com.example.facebookpractice.screens.Register
import com.example.facebookpractice.screens.Search
import com.example.facebookpractice.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(route = Routes.Splash.routes) {
            Splash(navController = navController)
        }

        composable(route = Routes.Home.routes) {
            Home()
        }

        composable(route = Routes.Login.routes) {
            Login(navController = navController)
        }

        composable(route = Routes.Register.routes) {
            Register(navController = navController)
        }

        composable(route = Routes.Notification.routes) {
            Notification()
        }

        composable(route = Routes.Search.routes) {
            Search()
        }


        composable(route = Routes.Profile.routes) {
            Profile()
        }

        composable(route = Routes.AddThread.routes) {
            AddThreads()
        }

        composable(route = Routes.BottomNav.routes) {
            BottomNav(navController = navController)
        }


    }
}