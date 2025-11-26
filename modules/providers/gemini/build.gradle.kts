plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.sa.aichatlib.providers.gemini"
	compileSdk = 36

	defaultConfig {
		minSdk = 24
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}
	kotlinOptions {
		jvmTarget = "21"
	}
}

dependencies {
	api(project(":modules:core"))
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.okhttp)
}
