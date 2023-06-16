plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "io.vallfg.valorantlfgmultiplatform.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "io.vallfg.valorantlfgmultiplatform.android"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    val composeBom = platform(libs.androidx.compose.bom)

    implementation(libs.androidx.core.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit)
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation(composeBom)
    debugImplementation(libs.androidx.compose.ui.manifest)
    implementation(libs.lottie.compose)
    implementation(libs.koin.compose)
    implementation(libs.voyager.androidx)
    implementation(libs.voyager.koin)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.navigation)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)
    implementation(libs.coil.compose)
    implementation(libs.coil)
    implementation(libs.coil.svg)
    implementation(libs.kotlinx.datetime)
}