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
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.common.ThreadItem
import com.example.threadpractice.common.UsersStoryHomeItem
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.util.SharedPref
import com.example.threadpractice.viewmodel.AddStoryViewModel
import com.example.threadpractice.viewmodel.HomeViewModel
import com.example.threadpractice.viewmodel.SearchViewModel
import com.example.threadpractice.viewmodel.StoryViewModel
import com.example.threadpractice.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userModel: UserModel = UserModel()
) {
    val homeViewModel: HomeViewModel = viewModel()
    val storyViewModel: StoryViewModel = viewModel()
    val addStoryViewModel: AddStoryViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val usersList by searchViewModel.userList.observeAsState(null)
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    val userViewModel: UserViewModel = viewModel()

    val story by userViewModel.story.observeAsState(null)
    val context = LocalContext.current

    val userId = FirebaseAuth.getInstance().currentUser!!.uid

    val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)
    val storyAndUsers by storyViewModel.storyAndUsers.observeAsState(null)

    val savedThreads by homeViewModel.savedThreads.observeAsState(emptyList())

    LaunchedEffect(Unit) {

        searchViewModel.fetchUsersExcludingCurrentUser(currentUserId)
        userViewModel.fetchStory(currentUserId)
        userViewModel.fetchThreads(uid = userId)
        userViewModel.fetchThreads(uid = currentUserId)
        homeViewModel.fetchSavedThreads(currentUserId)
    }

    Scaffold(modifier = modifier, topBar = {
        SmallTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text("Home")
            },
            actions = {
                Icon(
                    imageVector = Icons.Rounded.ChatBubble,
                    contentDescription = null,
                    modifier = modifier.clickable { navController.navigate(Routes.AllChat.routes) })
            },
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
    }) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(6.dp)
        ) {

            item {


                Row(
                    modifier = modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LazyRow(modifier = Modifier.padding(start = 4.dp)) {
                        item {
                            Box {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = SharedPref.getImageUrl(
                                            context
                                        )
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clickable {
                                            if (storyAndUsers != null && storyAndUsers!!.isNotEmpty()) {
                                                val routes =
                                                    Routes.AllStory.routes.replace(
                                                        oldValue = "{all_story}",
                                                        newValue = currentUserId
                                                    )
                                                navController.navigate(routes)
                                            } else {
                                                navController.navigate(Routes.AddStory.routes)
                                            }
                                        }
                                        .clip(CircleShape)
                                        .border(
                                            width = 2.dp,
                                            color = Color.Red,
                                            shape = CircleShape
                                        ),
                                    contentScale = ContentScale.Crop
                                )
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = null,
                                    modifier = modifier
                                        .clickable {
                                            navController.navigate(Routes.AddStory.routes) {
                                                popUpTo(Routes.Home.routes) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        .align(Alignment.BottomEnd)
                                )
                            }
                        }
                        item {
                            Spacer(modifier = modifier.padding(start = 4.dp))

                        }
                        item {
                            if (usersList != null && usersList!!.isNotEmpty()) {
                                val filterItems =
                                    usersList!!.filter {
                                        it.uid != FirebaseAuth.getInstance().currentUser!!.uid
                                    }
                                this@LazyRow.items(filterItems) { pairs ->
                                    UsersStoryHomeItem(
                                        users = pairs,
                                        navHostController = navController,
                                    )
                                }
                            }
                        }
                    }
                }


                this@LazyColumn.items(threadAndUsers ?: emptyList()) { pairs ->
                    ThreadItem(
                        thread = pairs.first,
                        users = pairs.second,
                        navController = navController,
                        userId = userId,
                    )


                }
            }
        }
    }

}
