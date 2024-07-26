package com.example.threadpractice.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.model.StoryModel
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.viewmodel.AddStoryViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DetailStoryItem(
    modifier: Modifier = Modifier,
    story: StoryModel,
    users: UserModel,
    addStoryViewModel: AddStoryViewModel,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    if (story.imageStory != "") {
        Spacer(modifier = modifier.padding(10.dp))
        Image(
            painter = rememberAsyncImagePainter(model = story.imageStory),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
                .size(350.dp)
            ,
            contentScale = ContentScale.Crop
        )
    }
}






















