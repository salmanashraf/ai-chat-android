pluginManagement {
	repositories {
		google {
			content {
				includeGroupByRegex("com\\.android.*")
				includeGroupByRegex("com\\.google.*")
				includeGroupByRegex("androidx.*")
			}
		}
		mavenCentral()
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
	}
}

rootProject.name = "AI Chat Lib"
include(":ai-chat-lib")
include(":sampleapp")
include(":modules:core")
include(":modules:ui")
include(":modules:providers:openai")
include(":modules:providers:gemini")
include(":modules:providers:anthropic")
include(":modules:providers:xai")
include(":modules:tools")
include(":modules:embeddings")
include(":modules:crypto")
