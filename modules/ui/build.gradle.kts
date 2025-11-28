import org.gradle.jvm.tasks.Jar

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
	id("maven-publish")
	id("signing")
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
	implementation(project(":modules:core"))
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.lifecycle.viewmodel.compose)
}

val uiJavadocDir = layout.buildDirectory.dir("emptyJavadoc")

val generateUiJavadoc by tasks.registering {
	doLast {
		uiJavadocDir.get().asFile.mkdirs()
	}
}

val uiJavadocJar by tasks.registering(Jar::class) {
	dependsOn(generateUiJavadoc)
	archiveClassifier.set("javadoc")
	from(uiJavadocDir)
}

afterEvaluate {
	publishing {
		publications {
			create<MavenPublication>("release") {
				groupId = project.group.toString()
				artifactId = "ui"
				version = project.version.toString()

				from(components["release"])
				artifact(uiJavadocJar.get())

				pom {
					name.set("AI Chat Android SDK - UI")
					description.set("Compose UI module for the AI Chat Android SDK.")
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
