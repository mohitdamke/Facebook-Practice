package com.example.threadpractice.common

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.R
import com.example.threadpractice.model.ThreadModel
import com.example.threadpractice.model.UserModel
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThreadItem(
    modifier: Modifier = Modifier,
    thread: ThreadModel,
    users: UserModel,
    navController: NavHostController,
    userId: String,
) {

    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    val homeViewModel: HomeViewModel = viewModel()

    val imagePagerState = rememberPagerState(pageCount = {
        thread.images.size ?: 0
    })

    val isLiked = thread.likes.containsKey(currentUserId)
    val isSaved = users.savedThreads.containsKey(thread.storeKey)


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
                    .clickable {
                        if (users!!.uid == currentUserId) {
                            navController.navigate(Routes.Profile.routes) {
                                launchSingleTop = true
                            }
                        } else {
                            val routes = Routes.OtherUsers.routes.replace("{data}", users!!.uid)
                            navController.navigate(routes) {
                                launchSingleTop = true
                            }
                        }
                    },
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = users!!.name, fontSize = 20.sp)
                Text(text = thread.thread, fontSize = 20.sp)
            }
        }

Spacer(modifier = modifier.height(10.dp))
        if (thread.images.isNotEmpty()) {
            HorizontalPager(
                state = imagePagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = modifier.padding(start = 60.dp))

                Image(
                    painter = rememberAsyncImagePainter(model = thread.images[it]),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }


//                Image(
//                    painter = rememberAsyncImagePainter(model = thread.images),
//                    contentDescription = null,
//                    modifier = modifier.size(100.dp),
//                    contentScale = ContentScale.Crop
//                )

        Spacer(modifier = modifier.padding(top = 20.dp))

        Spacer(modifier = modifier.height(10.dp))
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector =
                if (isLiked) Icons.Filled.HeartBroken else Icons.Outlined.Share,
                contentDescription = null,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
                        if (currentUserId.isNotEmpty()) {
                            homeViewModel.toggleLike(
                                threadId = thread.storeKey ?: "",
                                userId = currentUserId
                            )
                        }
                    }
            )
            Spacer(modifier = modifier.padding(start = 2.dp))
            Text(text = "${thread.likes.size} Likes")
            Spacer(modifier = modifier.padding(start = 10.dp))

            Icon(imageVector = Icons.Outlined.ModeComment,
                contentDescription = null,
                modifier = modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate(
                            Routes.Comments.routes.replace(
                                "{data}",
                                thread.storeKey ?: ""
                            )
                        )
                    })
            Spacer(modifier = modifier.padding(start = 2.dp))
            Text(text = "${thread.comments.size} Comments")
            Spacer(modifier = modifier.padding(start = 10.dp))
            Icon(
                imageVector = if (isSaved) {
                    Icons.Outlined.Save
                } else Icons.Filled.SaveAs,
                contentDescription = null,
                modifier = modifier.clickable {
                    homeViewModel.toggleSaveThread(
                        threadId = thread.storeKey ?: "",
                        context = context
                    )


                })
            Spacer(modifier = modifier.padding(start = 2.dp))
            Text(text = stringResource(R.string.save))
        }

        Divider(modifier = modifier.padding(10.dp))
        Spacer(modifier = modifier.height(10.dp))

    }
}




















