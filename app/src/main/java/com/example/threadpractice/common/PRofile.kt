//package com.example.threadpractice.common
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.threadpractice.R
//
//@Preview(showBackground = true)
//@Composable
//fun PRofile(modifier: Modifier = Modifier) {
//
//    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Spacer(modifier = modifier.padding(10.dp))
//        Row(
//            modifier = modifier
//                .fillMaxSize()
//                .weight(0.4f)
//                .padding(start = 10.dp, end = 10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column(
//                modifier = modifier
//                    .padding(4.dp)
//                    .weight(0.6f)
//            ) {
//                Text(text = "Mohit Damke", fontSize = 26.sp)
//                Spacer(modifier = modifier.padding(4.dp))
//                Text(text = "mohitdamke", fontSize = 20.sp)
//                Spacer(modifier = modifier.padding(4.dp))
//                Text(
//                    text = "I am a android developer working on the side project and " + "i love to create new project with writing code in kotlin",
//                    fontSize = 18.sp
//                )
//                Spacer(modifier = modifier.padding(4.dp))
//
//                Text(text = "0 Follower", fontSize = 18.sp)
//                Spacer(modifier = modifier.padding(4.dp))
//
//                Text(text = "0 Following", fontSize = 18.sp)
//                Spacer(modifier = modifier.padding(top = 8.dp))
//
//                Button(onClick = {
//                    authViewModel.logout()
//                }) {
//                    Text(text = "Logout")
//                }
//            }
//            Image(
//                painter = painterResource(id = R.drawable.man),
//                contentDescription = null,
//                modifier = modifier.size(100.dp)
//            )
//        }
//    }
//}