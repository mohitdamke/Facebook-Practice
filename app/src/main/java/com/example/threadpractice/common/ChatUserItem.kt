package com.example.threadpractice.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.navigation.Routes

@Composable
fun ChatUserItem(
    modifier: Modifier = Modifier,
    users: UserModel,
    navHostController: NavHostController,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    val routes = Routes.ChatPeople.routes.replace("{data}", users.uid)
                    navHostController.navigate(routes)
                }
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = users.name, fontSize = 20.sp)
                Text(text = users.bio, fontSize = 20.sp)
            }
        }


    }
}




















