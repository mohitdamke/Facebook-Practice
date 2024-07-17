package com.example.facebookpractice.screens

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
import com.example.facebookpractice.R
import com.example.facebookpractice.navigation.Routes
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
        navController.navigate(Routes.Login.routes){
            popUpTo(Routes.Splash.routes) {
                inclusive = true
            }
        }
    }
}
