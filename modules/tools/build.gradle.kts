plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

android {
	namespace = "com.sa.aichatlib.tools"
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
}
