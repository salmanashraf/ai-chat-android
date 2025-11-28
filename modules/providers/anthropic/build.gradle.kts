import org.gradle.jvm.tasks.Jar

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.serialization)
	id("maven-publish")
	id("signing")
}

android {
	namespace = "com.sa.aichatlib.providers.anthropic"
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
	publishing {
		singleVariant("release") {
			withSourcesJar()
		}
	}
}

dependencies {
	api(project(":modules:core"))
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.okhttp)
}

val anthropicJavadocDir = layout.buildDirectory.dir("emptyJavadoc")

val generateAnthropicJavadoc by tasks.registering {
	doLast {
		anthropicJavadocDir.get().asFile.mkdirs()
	}
}

val anthropicJavadocJar by tasks.registering(Jar::class) {
	dependsOn(generateAnthropicJavadoc)
	archiveClassifier.set("javadoc")
	from(anthropicJavadocDir)
}

afterEvaluate {
	publishing {
		publications {
			create<MavenPublication>("release") {
				groupId = project.group.toString()
				artifactId = "anthropic"
				version = project.version.toString()

				from(components["release"])
				artifact(anthropicJavadocJar.get())

				pom {
					name.set("AI Chat Android SDK - Anthropic Provider")
					description.set("Anthropic/Claude provider implementation for the AI Chat Android SDK.")
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
						developerConnection.set("scm:git:ssh://git@github.com/salmanashraf/ai-chat-android.git")
					}
				}
			}
		}

		repositories {
			maven {
				name = "OSSRH"
				val isSnapshot = project.version.toString().endsWith("-SNAPSHOT")
				url = uri(
					if (isSnapshot) {
						"https://ossrh-staging-api.central.sonatype.com/content/repositories/snapshots/"
					} else {
						"https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
					}
				)
				credentials {
					username = project.findProperty("ossrhUsername") as String?
						?: throw GradleException("Missing ossrhUsername in gradle.properties")
					password = project.findProperty("ossrhPassword") as String?
						?: throw GradleException("Missing ossrhPassword in gradle.properties")
				}
			}
		}
	}

	signing {
		useGpgCmd()
		sign(publishing.publications["release"])
	}
}
