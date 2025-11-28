import org.gradle.api.tasks.bundling.Zip
import org.gradle.jvm.tasks.Jar

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	id("maven-publish")
	id("signing")
}

val releaseGroupId = project.group.toString()
val releaseVersion = project.version.toString()
val centralPortalRepoDir = layout.buildDirectory.dir("central-component/repository")
val centralPortalRepoDirFile = centralPortalRepoDir.map { it.asFile }

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

/* ---------------------------------------------------------
   2. EMPTY JAVADOC JAR (Maven Central requirement)
--------------------------------------------------------- */
val emptyJavadocDir = layout.buildDirectory.dir("emptyJavadoc")

val createEmptyJavadoc by tasks.registering {
	doLast {
		emptyJavadocDir.get().asFile.mkdirs()
	}
}

val javadocJar by tasks.registering(Jar::class) {
	dependsOn(createEmptyJavadoc)
	archiveClassifier.set("javadoc")
	from(emptyJavadocDir)
}

/* ---------------------------------------------------------
   3. PUBLISHING — MUST BE inside afterEvaluate()
--------------------------------------------------------- */

afterEvaluate {

	publishing {
		publications {
			create<MavenPublication>("release") {

				groupId = releaseGroupId
				artifactId = "aichatlib"
				version = releaseVersion

				// Use the release component (includes AAR + dependencies)
				from(components["release"])

				// Only add javadoc jar (sources is already included by component)
				artifact(javadocJar.get())

				pom {
					name.set("AI Chat Android SDK")
					description.set("A multi-provider AI chat SDK.")
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
				name = "CentralPortal"
				url = uri(centralPortalRepoDirFile.get())
			}
			maven {
				name = "OSSRH"
				val isSnapshot = releaseVersion.endsWith("-SNAPSHOT")
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

	val publishCentralPortal = tasks.named("publishReleasePublicationToCentralPortalRepository")

	tasks.register<Zip>("bundleCentralComponent") {
		group = "publishing"
		description = "Packages the Central Portal component zip with checksums/signatures."
		dependsOn(publishCentralPortal)
		from(centralPortalRepoDirFile)
		destinationDirectory.set(layout.buildDirectory.dir("central-component"))
		archiveFileName.set("aichatlib-${releaseVersion}-component.zip")
	}
}
