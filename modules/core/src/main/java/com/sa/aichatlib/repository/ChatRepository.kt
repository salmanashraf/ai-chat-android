package com.sa.aichatlib.repository

import com.sa.aichatlib.ChatSdkConfig
import com.sa.aichatlib.dao.MessageDao
import com.sa.aichatlib.model.Message
import com.sa.aichatlib.provider.ChatRequest
import com.sa.aichatlib.provider.ChatResult
import com.sa.aichatlib.provider.ProviderId
import com.sa.aichatlib.provider.ProviderRegistry
import com.sa.aichatlib.utils.toChatPayload
import com.sa.aichatlib.utils.toDomain
import com.sa.aichatlib.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatRepository(
	private val dao: MessageDao,
	private val providerRegistry: ProviderRegistry,
	private var config: ChatSdkConfig
) {
	val messages: Flow<List<Message>> = dao.getAllMessages().map { list ->
		list.map { it.toDomain() }
	}

	suspend fun insert(message: Message) = dao.insert(message.toEntity())
	suspend fun clear() = dao.clear()

	fun updateConfig(newConfig: ChatSdkConfig) {
		config = newConfig
	}

	suspend fun getAIResponse(): String = withContext(Dispatchers.IO) {
		val resolvedProvider: ProviderId = when {
			providerRegistry.hasProvider(config.defaultProvider) -> config.defaultProvider
			else -> providerRegistry.all().firstOrNull()?.providerId
				?: return@withContext "No provider configured. Please apply a provider key."
		}

		val engine = providerRegistry.get(resolvedProvider)
			?: return@withContext "No provider configured for ${resolvedProvider.name}"

		val history = dao.getMessagesOnce().map { it.toDomain().toChatPayload() }
		val request = ChatRequest(
			providerId = resolvedProvider,
			messages = history,
			model = config.providerModels[resolvedProvider]
		)

		return@withContext when (val result = engine.complete(request)) {
			is ChatResult.Success -> result.content
			is ChatResult.Error -> result.throwable.message ?: "Provider error"
		}
	}
}
