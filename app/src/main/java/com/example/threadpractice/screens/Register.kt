package com.example.threadpractice.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.R
import com.example.threadpractice.common.OutlineText
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.viewmodel.AuthViewModel

@Composable
fun Register(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context = LocalContext.current
) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)


    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }


    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(Routes.Register.routes) {
                    inclusive = true
                }
            }
        }
    }







    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Register Screen",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.padding(top = 30.dp))


                    Image(
                        painter = if (imageUri == null) {
                            painterResource(id = R.drawable.man)
                        } else {
                            rememberAsyncImagePainter(
                                model = imageUri,
                            )
                        },
                        contentDescription = null, modifier = modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable {
                                val isGranted = ContextCompat.checkSelfPermission(
                                    context,
                                    permissionToRequest
                                ) == PackageManager.PERMISSION_GRANTED

                                if (isGranted) {
                                    launcher.launch("image/*")
                                } else {
                                    permissionLauncher.launch(permissionToRequest)
                                }

                            }, contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.padding(top = 30.dp))
                OutlineText(
                    value = name, onValueChange = { name = it },
                    label = "Name",
                    icons = Icons.Default.Person
                )

                Spacer(modifier = Modifier.padding(top = 30.dp))

                OutlineText(
                    value = username, onValueChange = { username = it },
                    label = "Username",
                    icons = Icons.Default.Person2
                )

                Spacer(modifier = Modifier.padding(top = 30.dp))

                OutlineText(
                    value = bio, onValueChange = { bio = it },
                    label = "Bio",
                    icons = Icons.Default.AddComment
                )

                Spacer(modifier = Modifier.padding(top = 30.dp))

                OutlineText(
                    value = email, onValueChange = { email = it },
                    label = "Email",
                    icons = Icons.Default.Email
                )

                Spacer(modifier = Modifier.padding(top = 30.dp))

                OutlineText(
                    value = password, onValueChange = { password = it },
                    label = "Password",
                    icons = Icons.Default.Lock
                )

                Spacer(modifier = Modifier.padding(top = 30.dp))
                Button(onClick = {


                    if (name.isEmpty() || username.isEmpty() || bio.isEmpty() || email.isEmpty() || password.isEmpty() || imageUri == null) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    } else {
                        authViewModel.register(
                            email,
                            password,
                            name,
                            username,
                            bio,
                            imageUri!!,
                            context
                        )
                        Toast.makeText(
                            context,
                            "You have successfully registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Register")
                }
                Spacer(modifier = Modifier.padding(top = 30.dp))
                Button(onClick = {
                    navController.navigate(Routes.Login.routes) {
                        popUpTo(Routes.Register.routes) {
                            inclusive = true
                        }
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Login")
                }
            }
        }
    }
}