package com.example.threadpractice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.threadpractice.model.CommentModel
import com.example.threadpractice.util.SharedPref
import com.example.threadpractice.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CommentsScreen(
    navController: NavHostController,
    threadId: String,
    viewModel: HomeViewModel = HomeViewModel()
) {
    val context = LocalContext.current
    var comments by remember { mutableStateOf(listOf<CommentModel>()) }

    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    LaunchedEffect(threadId) {
        viewModel.fetchComments(threadId) {
            comments = it
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {

        Comments(comments = comments) { commentText ->
            val newComment = CommentModel(
                userId = FirebaseAuth.getInstance().currentUser!!.uid,
                text = commentText
            )
            viewModel.addComment(
                userId = currentUserId,
                threadId = threadId,
                commentText = commentText,
                username = SharedPref.getUserName(context),
                name = SharedPref.getName(context)
            )
        }
    }
}
