package com.sa.aichatlib.utils

import com.sa.aichatlib.dao.MessageEntity
import com.sa.aichatlib.model.Message

fun MessageEntity.toDomain(): Message =
	Message(
		id = id.toString(), // or generate a UUID if you prefer
		sender = sender,
		message = content
	)

fun Message.toEntity(): MessageEntity =
	MessageEntity(
		sender = sender,
		content = message,
		timestamp = System.currentTimeMillis()
	)
