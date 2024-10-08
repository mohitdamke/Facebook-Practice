package com.example.threadpractice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.threadpractice.R
import com.example.threadpractice.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun Splash(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.thread), contentDescription = null)

    }
    LaunchedEffect(key1 = true) {
        delay(1000)

        if (FirebaseAuth.getInstance().currentUser == null) {
            navController.navigate(Routes.Login.routes) {
                popUpTo(Routes.Splash.routes) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(Routes.Splash.routes) {
                    inclusive = true
                }

            }
        }
    }
}
