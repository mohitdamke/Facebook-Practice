package com.example.facebookpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.facebookpractice.navigation.NavGraph
import com.example.facebookpractice.ui.theme.FacebookPracticeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FacebookPracticeTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
