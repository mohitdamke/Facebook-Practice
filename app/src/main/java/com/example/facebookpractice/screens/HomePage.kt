package com.example.facebookpractice.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PhotoAlbum
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material.icons.rounded.VideoCall
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.facebookpractice.R
import com.example.facebookpractice.authentication.presentation.viewmodel.SignOutViewModel
import com.example.facebookpractice.navigation.Screens
import com.example.facebookpractice.ui.theme.BackgroundBlack
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SignOutViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
//    val state = viewModel.signOutState.collectAsState(initial = null)
    val firebase = FirebaseAuth.getInstance()

    LaunchedEffect(key1 = firebase.currentUser) {
        scope.launch {
            if (firebase.currentUser == null) {
                navController.navigate(Screens.SignInScreen.route) {
                    popUpTo(Screens.HomePage.route) { inclusive = true }
                }
            }
        }
    }


    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 20.dp)
            .padding(vertical = 8.dp, horizontal = 8.dp)

    ) {
        LazyColumn() {
            item {
                TopAppBar(modifier, navController)
            }
            stickyHeader {
                TabBar()
            }
            item {
                StatusUpdateBar(
                    modifier,
                    avatarUrl = "https://media.licdn.com/dms/image/D4D03AQFbADYqm_i37g/profile-displayphoto-shrink_200_200/0/1711030857195?e=1726704000&v=beta&t=tJlH_dNe2U3B-rOCeBb5M-Dkpmg3gF9kN7XbRZjOQlw"
                )

            }
        }
    }
}

@Composable
fun TopAppBar(modifier: Modifier, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.W800,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { }, modifier = modifier
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        }
        Spacer(modifier = modifier.padding(8.dp))
        IconButton(
            onClick = {
                navController.navigate(Screens.MainPage.route)

            }, modifier = modifier
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountBox,
                contentDescription = stringResource(id = R.string.search)
            )
        }


    }
}

data class TabItem(
    val icon: ImageVector,
    val contentDescription: String,
)

@Composable
fun TabBar() {

    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem(Icons.Rounded.Home, stringResource(R.string.home)),
        TabItem(Icons.Rounded.Tv, stringResource(R.string.reels)),
        TabItem(Icons.Rounded.Store, stringResource(R.string.marketplace)),
        TabItem(Icons.Rounded.Newspaper, stringResource(R.string.news)),
        TabItem(Icons.Rounded.Notifications, stringResource(R.string.notifications)),
        TabItem(Icons.Rounded.Menu, stringResource(R.string.menu)),
    )

    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary
    ) {

        tabs.forEachIndexed { index, item ->
            Tab(
                selected = tabIndex == index,
                onClick = { tabIndex = index },
                modifier = Modifier.heightIn(48.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription,
                    tint = if (index == tabIndex) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.44f)
                    }
                )
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusUpdateBar(
    modifier: Modifier,
    avatarUrl: String,
    onTextChange: (String) -> Unit = {},
    onSendClick: () -> Unit = {}
) {
    Surface {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.baseline_account_circle_24).build(),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            var text by remember {
                mutableStateOf("")
            }
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    disabledPlaceholderColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = {
                    text = it
                    onTextChange(it)
                },
                placeholder = {
                    Text(stringResource(R.string.whats_on_your_mind))
                },
                keyboardActions = KeyboardActions(onSend = {
                    onSendClick()
                    text = ""
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
            )
        }
    }
    Divider(thickness = 1.dp)

    Row(Modifier.fillMaxWidth()) {
        StatusAction(
            Icons.Rounded.VideoCall,
            stringResource(R.string.live),
            modifier = Modifier.weight(1f)
        )
        VerticalDivider(Modifier.height(48.dp), thickness = 1.dp)
        StatusAction(
            Icons.Rounded.PhotoAlbum,
            stringResource(R.string.photo),
            modifier = Modifier.weight(1f)
        )
        VerticalDivider(Modifier.height(48.dp), thickness = 1.dp)
        StatusAction(
            Icons.Rounded.ChatBubble,
            stringResource(R.string.discuss),
            modifier = Modifier.weight(1f)
        )
    }


}

@Composable
private fun StatusAction(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    TextButton(
        modifier = modifier,
        onClick = { },
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = text, tint = BackgroundBlack)
            Spacer(Modifier.width(8.dp))
            Text(text)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


//    fun LogoutButton() {
//        Button(onClick = {
//            scope.launch {
//                viewModel.SignOutUser()
//                Log.d("TAG", "HomePage: ${firebase.signOut()}")
//                Log.d("TAG", "Logout : ${firebase.currentUser}")
//
//            }
//        }) {
//            Text(text = "Logout", fontSize = 20.sp)
//        }
//
//
//    LaunchedEffect(key1 = state.value?.isSuccess) {
//        if (state.value?.isSuccess == "You have successfully logged out") {
//            navController.navigate(Screens.SignInScreen.route) {
//                popUpTo(Screens.HomePage.route) { inclusive = true }
//            }
//        }
//    }
//}