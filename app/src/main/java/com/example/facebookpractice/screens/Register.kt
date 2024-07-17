package com.example.facebookpractice.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facebookpractice.common.OutlineText

@Composable
fun Register(modifier: Modifier = Modifier) {

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            item {
                Text(text = "Register Screen", fontSize = 30.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.padding(top = 30.dp))

                OutlineText(
                    value = name, onValueChange = { name = it },
                    label = "Name",
                    icons = Icons.Default.Person
                )

                Spacer(modifier = Modifier.padding(top = 30.dp))

                OutlineText(
                    value = username, onValueChange = { username = it },
                    label = "Username",
                    icons = Icons.Default.Person2
                )

                Spacer(modifier = Modifier.padding(top = 30.dp))

                OutlineText(
                    value = bio, onValueChange = { bio = it },
                    label = "Bio",
                    icons = Icons.Default.AddComment
                )

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
                Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Register")
                }
            }
        }
    }
}