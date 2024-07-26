package com.example.threadpractice.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.R
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.util.SharedPref
import com.example.threadpractice.viewmodel.AddStoryViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStory(modifier: Modifier = Modifier, navController: NavHostController) {
    val addStoryViewModel: AddStoryViewModel = viewModel()
    val isPosted by addStoryViewModel.isPosted.observeAsState(false)

    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    var imageUri by remember { mutableStateOf<Uri?>(null) }


    LaunchedEffect(isPosted) {
        if (isPosted) {
            imageUri = null

            navController.navigate(Routes.Home.routes) {
                popUpTo(Routes.AddStory.routes) {
                    inclusive = true
                }
            }
            Toast.makeText(
                context,
                "story have been Successfully Uploaded",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        )
        { uri: Uri? ->
            imageUri = uri
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            } else {
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                title = {
                    Text("Add Story")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Routes.Home.routes) {
                            popUpTo(Routes.AddStory.routes) {
                                inclusive = true
                            }
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (imageUri == null) {
                                addStoryViewModel.saveData(
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                    uidStory = UUID.randomUUID().toString(),
                                    image = ""
                                )
                            } else {
                                addStoryViewModel.saveImage(
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                    uidStory = UUID.randomUUID().toString(),
                                    imageUri = imageUri!!
                                )

                            }
                            Toast.makeText(
                                context,
                                "Story have been Successfully Uploaded",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Text(text = "Add Story", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                IconButton(onClick = { /* TODO something */ }) {
                    Image(
                        painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.padding(start = 10.dp))
                Text(
                    text = SharedPref.getUserName(context),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp), contentAlignment = Alignment.Center

            ) {
                Image(
                    painter = if (imageUri != null) rememberAsyncImagePainter(
                        model = imageUri
                    )
                    else
                        painterResource(id = R.drawable.add),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .size(300.dp)
                        .clip(RectangleShape)
                        .clickable {
                            val isGranted = ContextCompat.checkSelfPermission(
                                context,
                                permissionToRequest
                            ) == PackageManager.PERMISSION_GRANTED

                            if (isGranted) {
                                launcher.launch("image/*")
                                Log.i("TAG", "image is been loading")
                            } else {
                                permissionLauncher.launch(permissionToRequest)
                                Log.i("TAG", "image is not loading")

                            }
                        }
                )

                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Close Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            imageUri = null
                        }
                        .padding(top = 14.dp, bottom = 10.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}