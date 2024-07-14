package com.example.facebookpractice.authentication.presentation.screens

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facebookpractice.R
import com.example.facebookpractice.authentication.common.AuthConstants.ServerClient
import com.example.facebookpractice.authentication.presentation.viewmodel.SignInViewModel
import com.example.facebookpractice.navigation.Screens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun SignIn(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    navController: NavController
) {


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                viewModel.googleSignIn(credentials)
            } catch (it: ApiException) {
                print(it)
            }
        }

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val googleState = viewModel.googleState.value


    val state = viewModel.signInState.collectAsState(initial = null)

    val focusManager = LocalFocusManager.current

    Column(
        modifier
            .fillMaxSize()
            .padding(30.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {

        Text(text = "SignIn", modifier = Modifier.padding(bottom = 30.dp), fontSize = 30.sp)
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(value = email, onValueChange = { email = it },
            label = { Text(text = "Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(value = password, onValueChange = { password = it },
            label = { Text(text = "Password") }, singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))



        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "",
            modifier = Modifier
                .clickable {
                    val gso =
                        GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(ServerClient)
                            .requestEmail()
                            .build()

                    val mGoogleSignInClient =
                        GoogleSignIn.getClient(context, gso)

                    launcher.launch(mGoogleSignInClient.signInIntent)
                }
                .size(40.dp)
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))
        if (googleState.isLoading) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            scope.launch {
                viewModel.loginUser(email, password)
            }
        }) {
            Text(text = "SignIn")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "or")
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            navController.navigate(Screens.SignUpScreen.route)
        }) {
            Text(text = "SignUp", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))

        if (state.value?.isLoading == true) {
            CircularProgressIndicator()
        }
    }
    LaunchedEffect(key1 = state.value?.isSuccess) {
        scope.launch {
            if (state.value?.isSuccess?.isNotEmpty() == true) {
                val success = state.value?.isSuccess
                navController.navigate(Screens.HomePage.route){
                    popUpTo(Screens.SignInScreen.route){
                        inclusive = true
                    }
                }
            }
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
    LaunchedEffect(key1 = googleState.isSuccess) {
        scope.launch {
            if (googleState.isSuccess != null) {
                Toast.makeText(
                    context,
                    "SignIn With Google Account is Success ",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate(Screens.HomePage.route) {
                    popUpTo(Screens.SignInScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = googleState.isError) {
        scope.launch {
            if (googleState.isError == null) {
                Toast.makeText(
                    context,
                    "Failed to SignIn With Google Account",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}



