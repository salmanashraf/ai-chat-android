package com.sa.aichatlib.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sa.aichatlib.MyApp
import com.sa.aichatlib.factory.ChatViewModelFactory
import com.sa.aichatlib.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel = defaultChatViewModel()) {
    val messages by viewModel.messages.collectAsState()
    val input = remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true,
            verticalArrangement = Arrangement.Bottom
        ) {
            items(messages.reversed()) { message ->
                MessageItem(message)
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gemini is thinking...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = input.value,
                onValueChange = { input.value = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Type your message...") },
                singleLine = true
            )

            Button(
                onClick = {
                    val trimmed = input.value.trim()
                    if (trimmed.isNotEmpty()) {
                        viewModel.sendUserMessage(trimmed)
                        input.value = ""
                    }
                },
                enabled = input.value.trim().isNotEmpty()
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun defaultChatViewModel(): ChatViewModel {
    val context = LocalContext.current
    return viewModel(
        factory = ChatViewModelFactory((context.applicationContext as MyApp).repository)
    )
}
