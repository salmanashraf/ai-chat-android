package com.sa.aichatlib.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import java.util.UUID

@Immutable
@Serializable
data class Channel(
    val id: String = UUID.randomUUID().toString(),
    val messages: List<Message>
)