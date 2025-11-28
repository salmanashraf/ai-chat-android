import org.gradle.jvm.tasks.Jar

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.parcelize)
	id("kotlin-kapt")
	alias(libs.plugins.kotlin.serialization)
	id("maven-publish")
	id("signing")
}

android {
	namespace = "com.sa.aichatlib.core"
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
	implementation(libs.androidx.core.ktx)
	implementation(libs.coroutines.android)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.okhttp)
	implementation(libs.retrofit)
	implementation(libs.gson.converter)
	implementation(libs.gson)
	implementation(libs.json)
	implementation(libs.room.runtime)
	implementation(libs.room.ktx)
	kapt(libs.room.compiler)
	implementation(libs.lifecycle.viewmodel.ktx)
}

val coreJavadocDir = layout.buildDirectory.dir("emptyJavadoc")

val generateCoreJavadoc by tasks.registering {
	doLast {
		coreJavadocDir.get().asFile.mkdirs()
	}
}

val coreJavadocJar by tasks.registering(Jar::class) {
	dependsOn(generateCoreJavadoc)
	archiveClassifier.set("javadoc")
	from(coreJavadocDir)
}

afterEvaluate {
	publishing {
		publications {
			create<MavenPublication>("release") {
				groupId = project.group.toString()
				artifactId = "core"
				version = project.version.toString()

				from(components["release"])
				artifact(coreJavadocJar.get())

				pom {
					name.set("AI Chat Android SDK - Core")
					description.set("Core data, repository, and domain layer for AI Chat Android SDK.")
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
