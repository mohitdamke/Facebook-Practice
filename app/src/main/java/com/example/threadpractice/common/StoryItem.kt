package com.example.threadpractice.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.model.StoryModel
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.viewmodel.AddStoryViewModel

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    story: StoryModel,
    users: UserModel,
    addStoryViewModel: AddStoryViewModel,
) {
    Column(modifier = modifier) {
        if (story.imageStory != "") {

            Image(
                painter = rememberAsyncImagePainter(model = story.imageStory),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
    IconButton(onClick = {
    }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
    }


}

















