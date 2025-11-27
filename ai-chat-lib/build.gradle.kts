import java.util.Properties

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	id("maven-publish")
	id("signing")
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

publishing {
	publications {
		create<MavenPublication>("release") {
			groupId = "io.github.salmanashraf"
			artifactId = "aichatlib"
			version = "1.0.0"

			afterEvaluate {
				from(components["release"])
			}

			pom {
				name.set("AI Chat Android SDK")
				description.set("A multi-provider AI chat SDK for Android apps including GPT, Claude, Gemini, and Grok.")
				url.set("https://github.com/salmanashraf/ai-chat-android")

				licenses {
					license {
						name.set("MIT License")
						url.set("https://opensource.org/licenses/MIT")
					}
				}

				developers {
					developer {
						id.set("salmanashraf")
						name.set("Salman Ashraf")
						email.set("salmanashraf.12@gmail.com")
					}
				}

				scm {
					url.set("https://github.com/salmanashraf/ai-chat-android")
					connection.set("scm:git:git://github.com/salmanashraf/ai-chat-android.git")
					developerConnection.set("scm:git:ssh://github.com:salmanashraf/ai-chat-android.git")
				}
			}
		}
	}

	repositories {
		maven {
			name = "OSSRH"
			url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
			credentials {
				username = project.findProperty("ossrhUsername") as String?
				password = project.findProperty("ossrhPassword") as String?
			}
		}
	}
}

signing {
	useGpgCmd()  // REQUIRED for macOS GPG 2.4+
	sign(publishing.publications["release"])
}
