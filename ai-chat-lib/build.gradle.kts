plugins {
	id("com.android.library")
	id("kotlin-kapt")
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.parcelize)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.compose.compiler)
}

android {
	namespace = "com.sa.aichatlib"
	compileSdk = 35

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

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.lifecycle.viewmodel.compose)
	implementation(libs.retrofit)
	implementation(libs.gson.converter)
	implementation(libs.gson)
	implementation(libs.coroutines.android)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.okhttp)
	implementation(libs.json)
	kapt(libs.room.compiler)
	implementation(libs.room.runtime)
	implementation(libs.room.ktx)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
}