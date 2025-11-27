plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.sa.aichatlib.providers.openai"
	compileSdk = 36

	defaultConfig {
		minSdk = 24
	}

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
	api(project(":modules:core"))
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.okhttp)
}
