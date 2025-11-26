plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
}

android {
	namespace = "com.sa.aichatlib.ui"
	compileSdk = 36

	defaultConfig {
		minSdk = 24
	}

	buildFeatures {
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.composeUi.get()
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
	implementation(project(":modules:core"))
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.lifecycle.viewmodel.compose)
}
