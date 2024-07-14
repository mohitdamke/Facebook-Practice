package com.example.facebookpractice.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facebookpractice.R
import com.example.facebookpractice.authentication.presentation.viewmodel.SignOutViewModel
import com.example.facebookpractice.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: SignOutViewModel = hiltViewModel(),
    navController: NavController,
) {


    val scope = rememberCoroutineScope()
    val state = viewModel.signOutState.collectAsState(initial = null)
    val firebase = FirebaseAuth.getInstance()

    LaunchedEffect(key1 = firebase.currentUser) {
        scope.launch {
            if (firebase.currentUser == null) {
                navController.navigate(Screens.SignInScreen.route) {
                    popUpTo(Screens.MainPage.route) { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.main_screen),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraBold
        )
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val state = viewModel.signOutState.collectAsState(initial = null)
        val firebase = FirebaseAuth.getInstance()
        Button(onClick = {
            scope.launch {
                viewModel.SignOutUser()
                Log.d("TAG", "HomePage: ${firebase.signOut()}")
                Log.d("TAG", "Logout : ${firebase.currentUser}")

            }
        }) {
            Text(text = "Logout", fontSize = 20.sp)
        }

        LaunchedEffect(key1 = state.value?.isSuccess) {
            if (state.value?.isSuccess == "You have successfully logged out") {
                navController.navigate(Screens.SignInScreen.route)
                val success = state.value?.isSuccess
                Toast.makeText(context, success, Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotEmpty() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}