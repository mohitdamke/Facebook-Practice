package com.example.threadpractice.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.common.ThreadItem
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.util.SharedPref
import com.example.threadpractice.viewmodel.AuthViewModel
import com.example.threadpractice.viewmodel.HomeViewModel
import com.example.threadpractice.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context = LocalContext.current,
) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val homeViewModel: HomeViewModel = viewModel()

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)


    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)
    val scope = rememberCoroutineScope()

    if (firebaseUser != null)
        userViewModel.fetchThreads(firebaseUser!!.uid)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null)
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid


    if (currentUserId != "") {
        LaunchedEffect(key1 = currentUserId) {
            userViewModel.getFollowers(currentUserId)
            userViewModel.getFollowing(currentUserId)
            userViewModel.fetchThreads(currentUserId)
            homeViewModel.fetchSavedThreads(userId = currentUserId)
        }
    }


    val currentUser = FirebaseAuth.getInstance().currentUser?.uid

    val savedThreadIds by homeViewModel.savedThreadIds.observeAsState(emptyList())
    val threadsToDisplay =
        threads!!.filter { it.userId == currentUserId } // Filter for uploaded threads
    val savedThreadsToDisplay =
        threadsToDisplay.filter { savedThreadIds.contains(it.userId) } // Filter for saved threads
    val unsavedThreadsToDisplay =
        threadsToDisplay.filter { !savedThreadIds.contains(it.userId) } // Filter for unsaved threads




    LaunchedEffect(currentUser) {
        Log.d("TAG currentUser", "Current user Logout: STARTS " + currentUser.toString())
        if (currentUser == null) {
            navController.navigate(Routes.Login.routes) {
                popUpTo(Routes.Profile.routes) {
                    inclusive = true
                }
            }
            Log.d("TAG currentUser", "Current user Logout: ENDS " + " ${currentUser.toString()}")
        }
    }

    val user = UserModel(
        email = SharedPref.getEmail(context),
        name = SharedPref.getName(context),
        userName = SharedPref.getUserName(context),
        bio = SharedPref.getBio(context),
        imageUrl = SharedPref.getImageUrl(context),
    )
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(text = "Profile Screen", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = modifier.padding(10.dp))
                Button(onClick = { navController.navigate(Routes.Setting.routes) }) {
                    Text(text = "Setting")
                }
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = modifier
                                .weight(0.7f)
                        ) {
                            Text(text = "Name ${user.name}", fontSize = 26.sp)
                            Spacer(modifier = modifier.padding(4.dp))
                            Text(text = "@${user.userName}", fontSize = 16.sp)
                            Text(
                                text = "Total Post Count ${threadsToDisplay.size}",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = modifier.padding(4.dp))
                            Text(
                                text = user.bio,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = modifier.padding(4.dp))

                            Text(text = "${followerList?.size ?: "0"} Follower", fontSize = 18.sp)
                            Spacer(modifier = modifier.padding(4.dp))

                            Text(text = "${followingList?.size ?: "0"} Following", fontSize = 18.sp)
                            Spacer(modifier = modifier.padding(top = 8.dp))
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    navController.navigate(Routes.Login.routes) {
                                        popUpTo(Routes.Profile.routes) {
                                            inclusive = true
                                        }
                                    }
                                    authViewModel.logout()
                                    Toast.makeText(
                                        context,
                                        "You have successfully Logout",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }, modifier = Modifier.weight(0.3f)
                        ) {
                            Text(text = "Logout")
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Box(modifier = modifier) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = SharedPref.getImageUrl(
                                    context
                                )
                            ),
                            contentDescription = null,
                            modifier = modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )

                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            modifier = modifier.clickable {
                                navController.navigate(Routes.AddStory.routes)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.padding(16.dp))

                }
            }

            items(savedThreadsToDisplay + unsavedThreadsToDisplay) { thread ->
                ThreadItem(
                    thread = thread,
                    users = user,
                    navController = navController,
                    userId = SharedPref.getUserName(context)
                )
            }
        }


    }

    LaunchedEffect(key1 = firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.routes) {
                popUpTo(Routes.Profile.routes) {
                    inclusive = true
                }
            }
        } else {
            Unit
        }
    }

}
