package com.example.threadpractice.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.navigation.Routes

@Composable
fun UsersStoryHomeItem(
    modifier: Modifier = Modifier,
    users: UserModel,
    navHostController: NavHostController,
) {
    Column(
        modifier = modifier
            .padding(2.dp)

    ) {
        Image(
            painter = rememberAsyncImagePainter(model = users.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    val routes = Routes.AllStory.routes.replace("{all_story}", users.uid)
                    navHostController.navigate(routes)
                }
        )
    }
}




















