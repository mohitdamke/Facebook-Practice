package com.example.threadpractice.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadpractice.R
import com.example.threadpractice.common.OutlineText
import com.example.threadpractice.model.ChatModel
import com.example.threadpractice.navigation.Routes
import com.example.threadpractice.viewmodel.AuthViewModel
import com.example.threadpractice.viewmodel.PeopleChatViewModel
import com.example.threadpractice.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPeople(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    uid: String,
    chatViewModel: PeopleChatViewModel = viewModel()
) {
    val chatState by chatViewModel.chatState.collectAsState()
    var currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userViewModel: UserViewModel = viewModel()
    val users by userViewModel.users.observeAsState(null)
    val chatMessages by userViewModel.chatMessages.observeAsState(listOf())


    val chatId = if (currentUserId < uid) "$currentUserId-$uid" else "$uid-$currentUserId"

    LaunchedEffect(key1 = uid) {
        userViewModel.fetchUsers(uid)
        chatViewModel.fetchMessages(chatId) // Fetch messages using chatId
        userViewModel.fetchChatMessages(chatId) // Fetch chat messages
    }


    Column(
        modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Section
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "User Chat", fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = modifier.padding(10.dp))

            Column(
                modifier = modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Raees")
                Text(text = users!!.name ?: "Loading...", fontSize = 26.sp)
                Spacer(modifier = modifier.padding(4.dp))
                Text(text = "@${users!!.userName ?: "unknown"}", fontSize = 16.sp)
                Spacer(modifier = modifier.padding(4.dp))

                Image(
                    painter = rememberAsyncImagePainter(
                        model = users!!.imageUrl
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp) // Adjust size as needed
                        .clip(CircleShape) // If you want a circular image
                )
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }

        // Chat Messages Section
//        val messageList = chatState.messages.values.toList()
        val messageList = chatMessages

        LazyColumn(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom // Scroll to the bottom
        ) {
            items(messageList) { message ->
                if (message.senderId == currentUserId) {
                    UserMessageItem(
                        message = message,
                        onDelete = {
                            userViewModel.deleteMessage(
                                chatId = chatId,
                                storeKey = message.storeKey
                            )
                        }
                    )
                } else {
                    OtherMessageItem(
                        message = message,
                        onDelete = {
                            userViewModel.deleteMessage(
                                chatId = chatId,
                                storeKey = message.storeKey
                            )
                        }
                    )
                }
            }
        }

        // Input Field Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = chatViewModel.currentMessage,
                onValueChange = { chatViewModel.currentMessage = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable { }, tint = Color.Gray
                    )
                },
                label = {
                    Text(
                        text = "Type your Text", fontSize = 16.sp,
                        fontWeight = FontWeight.W600, color = Color.Gray,
                        fontFamily = FontFamily.SansSerif, maxLines = 1
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Gray,
                    unfocusedTextColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                modifier = modifier
                    .padding(10.dp)
                    .weight(1f), minLines = 1
            )

            IconButton(onClick = {
                val newMessageRef = userViewModel.chatRef.child(chatId).push()
                val storeKey = newMessageRef.key ?: return@IconButton
                val message = ChatModel(
                    senderId = currentUserId,
                    receiverId = uid,
                    messageText = chatViewModel.currentMessage,
                    storeKey = storeKey,
                    timestamp = System.currentTimeMillis()
                )
                userViewModel.sendMessage(chatId, message)
                chatViewModel.currentMessage = ""
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }

    }
}

private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Adjust format as needed
    return dateFormat.format(timestamp)
}
@Composable
fun UserMessageItem(message: ChatModel, onDelete: (String) -> Unit) {
    val formattedTimestamp = formatTimestamp(message.timestamp)
    Column(
        modifier = Modifier
            .padding(start = 100.dp, bottom = 2.dp)
            .clickable { onDelete(message.storeKey) }
    ) {
        Text(
            text = message.messageText,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(text = formattedTimestamp,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 2.dp, start = 230.dp),
            textAlign = TextAlign.End)
    }
}

@Composable
fun OtherMessageItem(message: ChatModel, onDelete: (String) -> Unit) {
    val formattedTimestamp = formatTimestamp(message.timestamp)

    Column(
        modifier = Modifier
            .padding(end = 100.dp, bottom = 2.dp)
            .clickable { onDelete(message.storeKey) }
    ) {
        Text(
            text = message.messageText,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(text = formattedTimestamp,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 2.dp, start = 230.dp))
    }
}
