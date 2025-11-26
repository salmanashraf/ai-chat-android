plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	id("maven-publish")
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
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}
	kotlinOptions {
		jvmTarget = "21"
	}
	publishing {
		singleVariant("release") {
			withSourcesJar()
		}
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

afterEvaluate {
	val libVersion = project.findProperty("LIB_VERSION")?.toString() ?: "1.0.0-SNAPSHOT"
	publishing {
		publications {
			create<MavenPublication>("release") {
				groupId = "com.sa.aichatlib"
				artifactId = "ai-chat-lib"
				version = libVersion
				from(components["release"])
			}
		}
		repositories {
			val githubRepo = System.getenv("GITHUB_REPOSITORY")
			val githubToken = System.getenv("GITHUB_TOKEN")
			if (!githubRepo.isNullOrBlank() && !githubToken.isNullOrBlank()) {
				maven {
					name = "GitHubPackages"
					url = uri("https://maven.pkg.github.com/$githubRepo")
					credentials {
						username = System.getenv("GITHUB_ACTOR")
						password = githubToken
					}
				}
			}
		}
	}
}
