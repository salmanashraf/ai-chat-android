package com.sa.aichatlib.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class MessageList(
  val messages: List<Message>,
)
