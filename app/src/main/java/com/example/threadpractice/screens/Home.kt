package com.example.threadpractice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadpractice.common.ThreadItem
import com.example.threadpractice.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(modifier: Modifier = Modifier, navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)
    Column {
        Text(text = "raees")

        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "raees")
                Text(text = "raees")
                Text(text = "raees")
            }}
            items(threadAndUsers ?: emptyList()) { pairs ->
                ThreadItem(
                    thread = pairs.first,
                    users = pairs.second,
                    navHostController = navController,
                    userId = userId
                )

            }
        }
    }
}