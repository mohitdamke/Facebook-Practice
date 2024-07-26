package com.example.threadpractice.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadpractice.common.DetailStoryItem
import com.example.threadpractice.common.StoryItem
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
        storyAndUsers?.size ?: 0
    })
//    LaunchedEffect(key1 = uid) {
    userViewModel.fetchStory(uid)
//    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Text(text = "All Story")
        if (storyAndUsers == null) {
            // Show loading indicator while fetching stories
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Loading stories...", modifier = Modifier.align(Alignment.Center))
            }
        } else if (storyAndUsers!!.isEmpty()) {
            // Show message if there are no stories
            Text(text = "No stories found.", modifier = Modifier.fillMaxSize())
        } else {

//            if (threads != null && users != null) {
//                item {
//                    this@LazyColumn.items(
//                        threads ?: emptyList()
//                    ) { pair ->
//                        ThreadItem(
//                            thread = pair,
//                            users = users!!,
//                            navController = navController,
//                            userId = SharedPref.getUserName(context),
//                        )
//                    }
//                }
//            }


//            HorizontalPager(
//                state = pagerState,
//                modifier = Modifier.weight(1f)
//            ) {
//                story ?: emptyList()

//                val storyAndUsers = storyAndUsers!![it]
//                val story = story!![it]

            LazyColumn(modifier = modifier) {
                items(storyAndUsers ?: emptyList()) { pairs ->
                    DetailStoryItem(
                        story = pairs.first,
                        users = pairs.second,
                        addStoryViewModel = addStoryViewModel
                    )
                }
            }
        }
    }
}