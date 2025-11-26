# AI Chat Android SDK
Multi-provider AI chat framework for Android (Jetpack Compose + Kotlin).  
Supports OpenAI (GPT), Claude, Gemini, Grok, and custom LLMs.

## ✨ Features
- Modular architecture (core, providers, UI, tools)
- Plug-and-play Compose Chat UI
- Multi-provider switching (GPT, Claude, Gemini, Grok)
- Plugin system for tools & agents
- Local encrypted message storage (AES-256)
- Offline embeddings (ONNX / TFLite)
- RAG document chat
- Demo app included

## 📚 Documentation
Full docs available in `/docs`.

Start here → **[docs/intro/project-philosophy.md](docs/intro/project-philosophy.md)**

## ⚡ Quick Start
```kotlin
val chat = AiChatSDK {
    provider(OpenAIProvider(apiKey = "sk-..."))
}
AiChatScreen(sdk = chat, sessionId = "default")
