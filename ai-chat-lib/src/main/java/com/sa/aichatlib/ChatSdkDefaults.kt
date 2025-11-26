package com.sa.aichatlib

import android.content.Context
import com.sa.aichat.providers.anthropic.AnthropicEngine
import com.sa.aichat.providers.gemini.GeminiEngine
import com.sa.aichat.providers.openai.OpenAiEngine
import com.sa.aichat.providers.xai.GrokEngine
import com.sa.aichatlib.provider.ProviderCredential
import com.sa.aichatlib.provider.ProviderId
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.json.JSONObject

/**
 * Convenience helpers to bootstrap ChatSdk with the default provider engines.
 */
fun ChatSdk.initializeWithDefaults(
	context: Context,
	config: ChatSdkConfig = ChatSdkConfig()
) {
	initialize(context, config)
	installDefaultEngines(config = config)
}

fun ChatSdk.applyConfig(
	config: ChatSdkConfig,
	httpClient: OkHttpClient = OkHttpClient(),
	json: Json = Json { ignoreUnknownKeys = true }
) {
	updateConfig(config)
	installDefaultEngines(config = config, httpClient = httpClient, json = json)
}

fun ChatSdk.installDefaultEngines(
	config: ChatSdkConfig = config(),
	httpClient: OkHttpClient = OkHttpClient(),
	json: Json = Json { ignoreUnknownKeys = true }
) {
	val cfg = config
	(cfg.credentials[ProviderId.OPEN_AI] as? ProviderCredential.ApiKey)?.key?.let { openAiKey ->
		registerEngine(
			OpenAiEngine(
				apiKeyProvider = { openAiKey },
				defaultModel = cfg.providerModels[ProviderId.OPEN_AI] ?: "gpt-3.5-turbo",
				httpClient = httpClient,
				json = json
			)
		)
	}

	val geminiKey = when (val credential = cfg.credentials[ProviderId.GEMINI]) {
		is ProviderCredential.ApiKey -> credential.key
		is ProviderCredential.GoogleServiceJson -> extractGeminiKeyFromJson(credential.json)
		else -> null
	}
	geminiKey?.let {
		registerEngine(
			GeminiEngine(
				apiKeyProvider = { it },
				model = cfg.providerModels[ProviderId.GEMINI] ?: "models/gemini-pro",
				httpClient = httpClient,
				json = json
			)
		)
	}

	(cfg.credentials[ProviderId.ANTHROPIC] as? ProviderCredential.ApiKey)?.key?.let { anthropicKey ->
		registerEngine(
			AnthropicEngine(
				apiKeyProvider = { anthropicKey },
				model = cfg.providerModels[ProviderId.ANTHROPIC] ?: "claude-3-haiku-20240307",
				httpClient = httpClient,
				json = json
			)
		)
	}

	(cfg.credentials[ProviderId.XAI] as? ProviderCredential.ApiKey)?.key?.let { grokKey ->
		registerEngine(
			GrokEngine(
				apiKeyProvider = { grokKey },
				model = cfg.providerModels[ProviderId.XAI] ?: "grok-beta",
				httpClient = httpClient,
				json = json
			)
		)
	}
}

private fun extractGeminiKeyFromJson(jsonString: String): String? =
	runCatching {
		val jsonObject = JSONObject(jsonString)
		jsonObject.optString("api_key")
			.takeIf { it.isNotBlank() }
			?: jsonObject.optString("apiKey").takeIf { it.isNotBlank() }
	}.getOrNull()
