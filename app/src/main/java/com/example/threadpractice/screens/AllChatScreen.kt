package com.example.threadpractice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadpractice.common.ChatUserItem
import com.example.threadpractice.common.OutlineText
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AllChatScreen(modifier: Modifier = Modifier, navController: NavHostController) {

    val searchViewModel: SearchViewModel = viewModel()
    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    var searchChat by remember { mutableStateOf("") }

    val usersList by searchViewModel.userList.observeAsState(null)
    LaunchedEffect(Unit) {
        searchViewModel.fetchUsersExcludingCurrentUser(currentUserId)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.ChatGemini.routes)
            }, modifier = modifier.padding(10.dp)) {
                Icon(imageVector = Icons.Rounded.AcUnit, contentDescription = null)
            }
        }) {

        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .padding(it)
                ) {
                    Text(text = "Chat Screen")
                    OutlineText(
                        value = searchChat,
                        onValueChange = { searchChat = it },
                        label = "Chat",
                        icons = Icons.Default.Search
                    )
                    if (usersList != null && usersList!!.isNotEmpty()) {
                        val filterItems =
                            usersList!!.filter { it.name.contains(searchChat, ignoreCase = true) }

                        this@LazyColumn.items(filterItems) { pairs ->
                            ChatUserItem(
                                users = pairs,
                                navHostController = navController,
                            )
                        }
                    }
                }

            }
        }
    }
}

