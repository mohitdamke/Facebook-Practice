package com.example.threadpractice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.threadpractice.R
import com.example.threadpractice.navigation.Routes

@Composable
fun Notification(modifier: Modifier = Modifier, navController: NavHostController) {
    Column {
        Button(onClick = {
            navController.navigate(Routes.ChatGemini.routes)
        }) {
            Text(text = stringResource(R.string.chatgpt))
        }
    }
}
