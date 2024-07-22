package com.example.threadpractice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadpractice.common.OutlineText
import com.example.threadpractice.common.ThreadItem
import com.example.threadpractice.common.UserItem
import com.example.threadpractice.viewmodel.HomeViewModel
import com.example.threadpractice.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Search(modifier: Modifier = Modifier, navController: NavHostController) {

    val searchViewModel: SearchViewModel = viewModel()
    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    var search by remember { mutableStateOf("") }

    val usersList by searchViewModel.userList.observeAsState(null)

    LaunchedEffect(Unit) {
        searchViewModel.fetchUsersExcludingCurrentUser(currentUserId)
    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(6.dp)) {
        Text(text = "Search Page", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
        OutlineText(
            value = search, onValueChange = { search = it },
            label = "Search",
            icons = Icons.Default.Search
        )
        LazyColumn(modifier = modifier.fillMaxSize()) {
            if (usersList != null && usersList!!.isNotEmpty()) {
                val filterItems = usersList!!.filter { it.name.contains(search, ignoreCase = true) }

                items(filterItems) { pairs ->
                    UserItem(
                        users = pairs,
                        navHostController = navController,
                    )
                }
            }
        }
    }
}