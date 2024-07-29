package com.example.threadpractice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadpractice.R
import com.example.threadpractice.common.ThreadItem
import com.example.threadpractice.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Saved(modifier: Modifier = Modifier, navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val savedThreads by homeViewModel.fetchSavedThreads(userId).observeAsState(emptyList())

    LazyColumn {
        item {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(R.string.saved))
                savedThreads.forEach { thread ->
                    val user by homeViewModel.getUserById(thread.userId).observeAsState(null)
                    user?.let {
                        ThreadItem(
                            thread = thread,
                            users = it,
                            navController = navController,
                            userId = userId,

                            )
                    }
                }
            }
        }
    }
}