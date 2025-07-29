# 🤖 AI Chat SDK for Android (OpenAI)

This is a lightweight and production-ready Android SDK that lets you integrate conversational AI (Gemini or OpenAI) into any Android app using modern development practices.

---

## 🚀 Features

- ✨ Jetpack Compose UI for chat
- 🤖 OpenAI GPT-3.5
- 💾 Local message persistence using Room
- 🔁 Real-time chat updates using Kotlin Flow
- 🧱 MVVM + Clean Architecture
- 📱 Sample app included

---

## 📁 Project Structure

```
md-android-clean/
├── aichatlib/                # Reusable SDK module
│   ├── data/                 # Room DB setup
│   ├── model/                # Message & API Models
│   ├── repository/           # Handles AI API and local persistence
│   ├── ui/                   # Chat UI Composables
│   └── viewmodel/            # ChatViewModel
├── sampleapp/                # Demo app using the SDK
│   └── MainActivity.kt
```

---

## 🔧 Setup

### 1. Add to `libs.versions.toml`:
```toml
okhttp = "com.squareup.okhttp3:okhttp:4.12.0"
json = "org.json:json:20231013"
```

### 2. Add Internet Permission in `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### 3. Application Class (init Room):
```kotlin
class AIChatApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}
```

Update your `AndroidManifest.xml` of sample app:
```xml
<application android:name=".AIChatApp" ... />
```

---

## 🤖 Using OpenAI (GPT-3.5)

In `ChatRepository.kt`:
```kotlin
suspend fun getAIResponse(message: String): String = withContext(Dispatchers.IO) {
    val json = JSONObject().apply {
        put("model", "gpt-3.5-turbo")
        put("messages", JSONArray().apply {
            put(JSONObject().apply {
                put("role", "user")
                put("content", message)
            })
        })
    }

    val requestBody = json.toString()
        .toRequestBody("application/json".toMediaType())

    val request = Request.Builder()
        .url("https://api.openai.com/v1/chat/completions")
        .header("Authorization", "Bearer YOUR_API_KEY")
        .post(requestBody)
        .build()

    val response = client.newCall(request).execute()
    val body = response.body?.string()

    return@withContext JSONObject(body ?: "")
        .getJSONArray("choices")
        .getJSONObject(0)
        .getJSONObject("message")
        .getString("content").trim()
}
```

> 🔐 Replace `YOUR_API_KEY` with your [OpenAI API key](https://platform.openai.com/account/api-keys)

---

## 💬 Start Chat Screen

In your `MainActivity.kt`:
```kotlin
setContent {
    MaterialTheme {
        ChatScreen()
    }
}
```

---

## ✅ Tech Highlights

| Layer     | Technology               |
|-----------|--------------------------|
| UI        | Jetpack Compose          |
| ViewModel | Kotlin Coroutines + Flow |
| DB        | Room                     |
| Network   | OpenAI via OkHttp        |

---

## 📸 Screenshot

> ✅ Typing UI, persistent history, AI replies with loading indicator.

---

## 💡 Why this matters (Tech Nation):

- ✅ Modular SDK with Clean Architecture
- ✅ Real product integration (Gemini or OpenAI)
- ✅ Reusable by other devs and scalable
- ✅ MVVM + Compose + Room = Best Practices
- ✅ Demonstrates innovation + technical leadership


### Demo

https://user-images.githubusercontent.com/2988051/154840275-941842f9-6aa7-4bb4-ab90-5ca0e617ba91.mp4

---

## 📝 License

MIT – Free to use, modify, and extend.

