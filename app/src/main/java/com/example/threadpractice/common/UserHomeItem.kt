package com.example.threadpractice.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.model.StoryModel
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.navigation.Routes

@Composable
fun UserHomeItem(
    modifier: Modifier = Modifier,
    users: UserModel,
    story: StoryModel,
    navController: NavHostController,
) {

    Column(
        modifier = modifier
            .clickable {
                val routes = Routes.StoryDetail.routes.replace("{story}", users.uid)
                navController.navigate(routes)
            }
            .padding(4.dp), verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = users.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
                .border(shape = CircleShape, color = Red, width = 4.dp)
        )
    }
}























