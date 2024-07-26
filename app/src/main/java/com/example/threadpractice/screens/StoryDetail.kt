package com.example.threadpractice.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun StoryDetail(modifier: Modifier = Modifier
                , navController: NavHostController) {
    Text(text = "Add Story")
}