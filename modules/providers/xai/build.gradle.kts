plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.sa.aichatlib.providers.xai"
	compileSdk = 36

	defaultConfig {
		minSdk = 24
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	api(project(":modules:core"))
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.okhttp)
}
