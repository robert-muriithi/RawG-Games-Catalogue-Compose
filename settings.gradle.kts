pluginManagement {
    repositories {
        google()
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

rootProject.name = "NavigationDemo"
include(":app")
include(":games")
include(":bookmarks")
include(":settings")
include(":core")
include(":core:database")
include(":core:network")
include(":tags")
include(":core:shared")
include(":core:datastore")
include(":core:designsystem")
include(":search")
include(":developers")
