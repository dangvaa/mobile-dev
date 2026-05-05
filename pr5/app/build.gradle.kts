import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.recipesapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.recipesapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val propertiesFile = rootProject.file("local.properties")

        if (propertiesFile.exists()) {
            properties.load(propertiesFile.inputStream())
        }

        val yandexKey = properties.getProperty("YANDEX_MAPS_KEY") ?: ""
        val weatherKey = properties.getProperty("WEATHER_API_KEY") ?: ""

        buildConfigField("String", "YANDEX_KEY", "\"$yandexKey\"")
        buildConfigField("String", "WEATHER_KEY", "\"$weatherKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room3.runtime)
    implementation(libs.androidx.room.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.yandex.maps)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
}