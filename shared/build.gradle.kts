plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.apollographql.apollo3") version "3.8.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
}


@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
                implementation(libs.apollo)
                implementation(libs.voyager.koin)
                implementation(libs.voyager.androidx)
                implementation(libs.voyager.bottomSheetNavigator)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.tabNavigator)
                implementation(libs.voyager.transitions)
                implementation(libs.ktor.core)
                implementation(libs.ktor.cio)
                implementation(libs.ktor.websockets)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlin.serialization)
                implementation(libs.koin.core)
                implementation(libs.apollo)
                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

apollo {
    service("service") {
        packageName.set("io.vallfg")
    }
}

android {
    namespace = "io.vallfg.valorantlfgmultiplatform"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}