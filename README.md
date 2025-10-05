# 🤖 AI Chat SDK for Android – Compose + OpenAI + RoomDB

A modular, production-ready **AI Chat SDK** for Android built using **Jetpack Compose**, **OpenAI's LLM API**, **Room Database** for persistence, and **Clean Architecture** principles.

Whether you're building fintech tools, education bots, or customer support assistants — this SDK provides everything you need to integrate intelligent LLM chat into your Android app.

---

## ✨ Features

- 🔌 Plug-and-play AI chat component (Jetpack Compose)
- 💬 Supports LLMs (OpenAI GPT models)
- 🗃️ Local message persistence with Room DB
- 🧠 MVVM & Repository Pattern
- 📦 Easy to integrate as a library module

---

## 🧠 Powered by LLM

This SDK uses **OpenAI’s GPT-3.5 Turbo** LLM to generate real-time AI responses from natural language inputs.

You can easily swap in **Gemini Pro** or any other LLM provider using the existing architecture.

---

## 📐 Architecture

- `ai-chat-lib/`
  - `model/`: Shared message models and OpenAI response structures
  - `repository/`: Handles message I/O and network requests to LLM
  - `dao/`: Room DAO for saving and retrieving messages
  - `viewmodel/`: ViewModel layer using Kotlin Coroutines
  - `ui/`: Composable chat UI
  - `network/`: OpenAI HTTP client integration
  - `factory/`: ViewModel factory injection
  - `AIChatApp.kt`: Central app initialization with DB setup

---

## 📦 Setup

### 1. Add Dependencies

In your `build.gradle.kts`:

```kotlin
implementation("androidx.compose.ui:ui:<version>")
implementation("androidx.room:room-runtime:<version>")
kapt("androidx.room:room-compiler:<version>")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
```

---

### 2. Configure OpenAI Access

Set your OpenAI API key securely:

```kotlin
val request = Request.Builder()
    .url("https://api.openai.com/v1/chat/completions")
    .header("Authorization", "Bearer YOUR_API_KEY")
```

---

### 3. Integrate the SDK

Initialize DB in your `Application` class:

```kotlin
val db = Room.databaseBuilder(
    context,
    AppDatabase::class.java, "chat.db"
).build()
```

Then in your `Activity` or `Compose` function:

```kotlin
ChatScreen(viewModel = defaultChatViewModel())
```

---

### 4. Customization

- Replace OpenAI endpoint with Gemini/Grok/LLama2
- Update `ChatRepository` to modify prompt structure
- Override `MessageDao` for encryption or multi-user storage

---

## 📊 Sample Use Case

```kotlin
viewModel.sendUserMessage("What's the weather today?")
```

Under the hood:

- Saves user message
- Sends to OpenAI LLM
- Parses JSON response
- Stores AI reply
- Emits live UI updates

---

## 📁 Folder Overview

```
ai-chat-lib/
│
├── viewmodel/         # MVVM logic
├── repository/        # ChatRepository with LLM logic
├── dao/               # Room DB setup
├── model/             # Domain & network models
├── network/           # OpenAI client integration
├── ui/                # Composable UI
└── AIChatApp.kt       # Application initializer
```

---

## 🚀 How to Run

1. Clone this repo
2. Add your OpenAI API key
3. Run `:sample-app` module
4. Start chatting with LLM in real time!

---

## 🔒 Security Notes

- Keep your API key in encrypted secrets or gradle properties
- Do not hardcode credentials in source files

#### License

This project is licensed under the [MIT License](https://github.com/salmanashraf/ai-chat-android/blob/master/LICENSE).


---

## 📬 Contact

For help or collaboration, reach out via [GitHub](https://github.com/salmanashraf) or open an issue.
