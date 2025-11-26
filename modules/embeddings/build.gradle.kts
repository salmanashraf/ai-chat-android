plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

android {
	namespace = "com.sa.aichatlib.embeddings"
	compileSdk = 36
	defaultConfig { minSdk = 24 }
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions { jvmTarget = "11" }
}

dependencies {
	api(project(":modules:core"))
}
