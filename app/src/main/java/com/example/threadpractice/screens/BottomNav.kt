package com.example.threadpractice.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.threadpractice.model.BottomNavItem
import com.example.threadpractice.navigation.Routes

@Composable
fun BottomNav(navController: NavHostController) {
    val navController1 = rememberNavController()

    Scaffold(bottomBar = { MyBottomBar(navController1) }) { padding ->
        NavHost(
            navController = navController1,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(padding)
        )
        {
            composable(route = Routes.Home.routes) {
                Home(navController = navController1)
            }

            composable(route = Routes.Notification.routes) {
                Notification(navController = navController1)
            }

            composable(route = Routes.Search.routes) {
                Search(navController = navController1)
            }

            composable(route = Routes.Profile.routes) {
                Profile(navController = navController1)
            }

            composable(route = Routes.AddThread.routes) {
                AddThreads(navController = navController1)
            }

        }
    }


}

@Composable
fun MyBottomBar(navController1: NavHostController) {

    val backStackEntry = navController1.currentBackStackEntryAsState()

    val list = listOf(
        BottomNavItem("Home", Routes.Home.routes, Icons.Default.Home),
        BottomNavItem("Search", Routes.Search.routes, Icons.Default.Search),
        BottomNavItem("AddThread", Routes.AddThread.routes, Icons.Default.AddCircle),
        BottomNavItem("Notification", Routes.Notification.routes, Icons.Default.Notifications),
        BottomNavItem("Profile", Routes.Profile.routes, Icons.Default.Person),


        )

    BottomAppBar {
        list.forEach {
            val selected = it.route == backStackEntry.value?.destination?.route

            NavigationBarItem(selected = selected, onClick = {
                navController1.navigate(it.route) {
                    popUpTo(navController1.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            }, icon = {
                Icon(imageVector = it.icon, contentDescription = it.title)
            })
        }
    }
}