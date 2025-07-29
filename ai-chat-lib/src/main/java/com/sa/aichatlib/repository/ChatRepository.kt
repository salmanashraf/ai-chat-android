package com.sa.aichatlib.repository

import com.sa.aichatlib.dao.MessageDao
import com.sa.aichatlib.model.Message
import com.sa.aichatlib.model.OpenAIChatRequest
import com.sa.aichatlib.model.OpenAIChatResponse
import com.sa.aichatlib.model.OpenAIMessage
import com.sa.aichatlib.utils.toDomain
import com.sa.aichatlib.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ChatRepository(private val dao: MessageDao) {
	val messages: Flow<List<Message>> = dao.getAllMessages().map { list ->
		list.map { it.toDomain() }
	}

	private val jsonParser = Json { ignoreUnknownKeys = true }

	private val client = OkHttpClient()


	suspend fun insert(message: Message) = dao.insert(message.toEntity())
	suspend fun clear() = dao.clear()

	suspend fun getAIResponse(message: String): String = withContext(Dispatchers.IO) {
		try {

			val requestBody = jsonParser.encodeToString(
				OpenAIChatRequest(
					model = "gpt-3.5-turbo",
					messages = listOf(OpenAIMessage(role = "user", content = message))
				)
			).toRequestBody("application/json".toMediaType())

			val request = Request.Builder()
				.url("https://api.openai.com/v1/chat/completions")
				.header("Authorization", "Bearer YOUR_API_KEY")
				.post(requestBody)
				.build()

			val response = client.newCall(request).execute()
			val bodyString = response.body?.string() ?: ""

			val parsed = jsonParser.decodeFromString<OpenAIChatResponse>(bodyString)
			return@withContext parsed.choices.firstOrNull()?.message?.content?.trim()
				?: "No response from OpenAI"
		} catch (e: Exception) {
			e.printStackTrace()
			return@withContext "Something went wrong"
		}
	}


}
