import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.apolloGraphQl)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-alpha06")
            implementation("com.airbnb.android:lottie-compose:6.6.7")
            
            // Apollo GraphQL for Android
            implementation("com.apollographql.apollo:apollo-runtime:4.3.3")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            
            // ViewModel dependencies for KMP
            api(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha06")
            
            // Apollo GraphQL
            api(libs.apollo.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("io.coil-kt.coil3:coil-network-ktor:3.0.0-alpha06")
            
            // Apollo GraphQL for JVM
            implementation("com.apollographql.apollo:apollo-runtime-jvm:4.3.3")
        }
        val iosArm64Main by getting {
            dependencies {
                // Apollo GraphQL for iOS ARM64
                implementation("com.apollographql.apollo:apollo-runtime-iosarm64:4.3.3")
            }
        }
        val iosSimulatorArm64Main by getting {
            dependencies {
                // Apollo GraphQL for iOS Simulator ARM64
                implementation("com.apollographql.apollo:apollo-runtime-iossimulatorarm64:4.3.3")
            }
        }
    }
}

android {
    namespace = "com.llego.multiplatform"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.llego.multiplatform"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.llego.multiplatform.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.llego.multiplatform"
            packageVersion = "1.0.0"
        }
    }
}

apollo {
    service("service") {
        packageName.set("com.llego.multiplatform.graphql")
    }
}
