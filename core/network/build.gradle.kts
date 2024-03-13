import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
    namespace = "dev.robert.network"
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
        buildConfig = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {

    // Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp client
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // HttpLoggingInterceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")

    // ktor
    implementation ("io.ktor:ktor-client-android:1.5.0")
    implementation ("io.ktor:ktor-client-serialization:1.5.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation ("io.ktor:ktor-client-logging-jvm:1.5.0")
}