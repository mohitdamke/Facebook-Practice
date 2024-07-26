package com.example.threadpractice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.common.StoryItem
import com.example.threadpractice.common.ThreadItem
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.util.SharedPref
import com.example.threadpractice.viewmodel.AddStoryViewModel
import com.example.threadpractice.viewmodel.HomeViewModel
import com.example.threadpractice.viewmodel.StoryViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(modifier: Modifier = Modifier, navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val storyViewModel: StoryViewModel = viewModel()
    val addStoryViewModel: AddStoryViewModel = viewModel()

    val context = LocalContext.current

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)
    val storyAndUsers by storyViewModel.storyAndUsers.observeAsState(null)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        Text(text = "Home Page", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)

        Row(
            modifier = modifier.padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            LazyRow(modifier = Modifier.padding(start = 4.dp)) {
                item {
                    Box() {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = SharedPref.getImageUrl(
                                    context
                                )
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(width = 2.dp, color = Color.Red, shape = CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            modifier = modifier.clickable {
                                navController.navigate(Routes.AddStory.routes){
                                    navController.navigateUp()
                                }
                            }.align(Alignment.BottomEnd)
                        )
                    }
                }
                item {
                    Spacer(modifier = modifier.padding(start = 4.dp))

                }
                items(storyAndUsers ?: emptyList()) { pairs ->
                    StoryItem(
                        story = pairs.first,
                        users = pairs.second,
                        addStoryViewModel = addStoryViewModel
                    )
                }
            }
        }
        LazyColumn(modifier = modifier.weight(1f)) {
            items(threadAndUsers ?: emptyList()) { pairs ->
                ThreadItem(
                    thread = pairs.first,
                    users = pairs.second,
                    navController = navController,
                    userId = userId
                )
            }
        }


    }
}