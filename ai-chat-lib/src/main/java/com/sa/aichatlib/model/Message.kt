package com.sa.aichatlib.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.UUID

@Immutable
@Serializable
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val sender: String,
    val message: String,
) {
    val isBot: Boolean = sender == "AI"
    companion object {
        fun defaultMessage(): Message = Message(sender = "AI", message = "Hi, I'm your assistant.")
    }
}