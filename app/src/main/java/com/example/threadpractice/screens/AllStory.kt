package com.example.threadpractice.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.common.DetailStoryItem
import com.example.threadpractice.viewmodel.AddStoryViewModel
import com.example.threadpractice.viewmodel.StoryViewModel
import com.example.threadpractice.viewmodel.UserViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllStory(modifier: Modifier = Modifier, navController: NavHostController, uid: String) {

    val userViewModel: UserViewModel = viewModel()

    val users by userViewModel.users.observeAsState(null)
    val story by userViewModel.story.observeAsState(null)

    val storyViewModel: StoryViewModel = viewModel()
    val addStoryViewModel: AddStoryViewModel = viewModel()

    val storyAndUsers by storyViewModel.storyAndUsers.observeAsState(null)
    val pagerState = rememberPagerState(pageCount = {
        story?.size ?: 0
    })

    LaunchedEffect(key1 = uid) {
        userViewModel.fetchStory(uid)
        userViewModel.fetchUsers(uid)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {

        Text(text = "All Story", fontSize = 24.sp)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                contentDescription = null, modifier = modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Text(text = users?.name ?: "", fontSize = 22.sp)
        }
        if (story == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading stories...", modifier = Modifier.align(Alignment.Center))
            }
        } else if (story!!.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No stories found.", modifier = Modifier.fillMaxSize())
            }
        } else {


//            LazyColumn(modifier = modifier) {
//                items(story ?: emptyList()) { pairs ->
//                    DetailStoryItem(
//                        story = pairs, users = users!!, addStoryViewModel = addStoryViewModel
//                    )
//          }      }

            Spacer(modifier = modifier.height(20.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                story ?: emptyList()
                val story = story!![it]
                DetailStoryItem(
                    story = story, users = users!!, addStoryViewModel = addStoryViewModel
                )

            }
        }
    }
}
