package com.sa.aichatlib.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ChannelList(
	val channels: List<Channel>,
)
