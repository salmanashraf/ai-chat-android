plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

android {
	namespace = "com.sa.aichatlib"
	compileSdk = 36

	defaultConfig {
		minSdk = 24
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
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
	api(project(":modules:ui"))
	implementation(project(":modules:providers:openai"))
	implementation(project(":modules:providers:gemini"))
	implementation(project(":modules:providers:anthropic"))
	implementation(project(":modules:providers:xai"))
	implementation(libs.okhttp)
	implementation(libs.kotlinx.serialization.json)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
}
