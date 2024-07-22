package com.example.threadpractice.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.common.ThreadItem
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.util.SharedPref
import com.example.threadpractice.viewmodel.AuthViewModel
import com.example.threadpractice.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUsers(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uid: String,
    context: Context = LocalContext.current
) {

    var currentUserId = ""

    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userViewModel: UserViewModel = viewModel()

    val threads by userViewModel.threads.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)
    val isFollowing = followerList?.contains(currentUserId) == true

    userViewModel.fetchThreads(uid)
    userViewModel.fetchUsers(uid)
    userViewModel.getFollowers(uid)
    userViewModel.getFollowing(uid)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Text(text = "User Profile", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = modifier.padding(10.dp))
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
                            Text(text = users!!.name, fontSize = 26.sp)
                            Spacer(modifier = modifier.padding(4.dp))
                            Text(text = "@${users!!.userName}", fontSize = 16.sp)
                            Spacer(modifier = modifier.padding(4.dp))
                            Text(
                                text = users!!.bio,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = modifier.padding(4.dp))

                            Text(text = "${followerList?.size} Followers", fontSize = 18.sp)
                            Spacer(modifier = modifier.padding(4.dp))

                            Text(text = "${followingList?.size} Following", fontSize = 18.sp)
                            Spacer(modifier = modifier.padding(top = 8.dp))
                        }
                        Button(
                            onClick = {
                                if (currentUserId != "") {
                                    userViewModel.followOrUnfollowUser(
                                        uid,
                                        currentUserId,
                                        isFollowing
                                    )
                                }
                            }, modifier = Modifier.weight(0.3f)
                        ) {
                            Text(
                                text = if (isFollowing) {
                                    "Following"
                                } else {
                                    "Follow"
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = users!!.imageUrl
                        ),
                        contentDescription = null,
                        modifier = modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.padding(16.dp))

            }
            if (threads != null && users != null) {
                item {
                    this@LazyColumn.items(
                        threads ?: emptyList()
                    ) { pair ->
                        ThreadItem(
                            thread = pair,
                            users = users!!,
                            navHostController = navController,
                            userId = SharedPref.getUserName(context),
                        )
                    }
                }
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
