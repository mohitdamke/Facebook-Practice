package com.example.facebookpractice.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.facebookpractice.common.OutlineText
import com.example.facebookpractice.navigation.Routes

@Composable
fun Login(modifier: Modifier = Modifier, navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Screen", fontSize = 30.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.padding(top = 30.dp))

        OutlineText(
            value = email, onValueChange = { email = it },
            label = "Email",
            icons = Icons.Default.Email
        )

        Spacer(modifier = Modifier.padding(top = 30.dp))

        OutlineText(
            value = password, onValueChange = { password = it },
            label = "Password",
            icons = Icons.Default.Lock
        )

        Spacer(modifier = Modifier.padding(top = 30.dp))

        Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.padding(top = 30.dp))

        Button(onClick = {
            navController.navigate(Routes.Register.routes)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Register")
        }

    }
}

