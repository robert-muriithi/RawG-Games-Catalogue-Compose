import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id ("com.google.devtools.ksp") version "1.9.20-1.0.14"
    kotlin("plugin.serialization") version "1.9.0"
    id("com.google.dagger.hilt.android")
}
apply {
    from("$rootDir/core-dependencies.gradle")
}
apply {
    from("$rootDir/test-dependencies.gradle")
}
apply {
    from("$rootDir/compose-dependencies.gradle")
}
val properties = gradleLocalProperties(rootDir)
android {
    namespace = "dev.robert.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            buildConfigField("String", "BASE_URL", "\"https://api.rawg.io/api/\"")
            buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    kotlin {
        jvmToolchain(11)
    }
    buildFeatures {
        compose = true
    }
    buildFeatures {
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:network"))
}