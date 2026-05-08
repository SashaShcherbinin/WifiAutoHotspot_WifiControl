import com.android.build.api.dsl.ApplicationExtension
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
}

val localProperties = Properties().apply {
    FileInputStream(rootProject.file("secret.properties")).use { load(it) }
}

val localStoreFile: String = localProperties.getProperty("storeFile")
val localStorePassword: String = localProperties.getProperty("storePassword")
val localKeyAlias: String = localProperties.getProperty("keyAlias")
val localKeyPassword: String = localProperties.getProperty("keyPassword")

extensions.configure<ApplicationExtension> {
    namespace = "wifi.control"
    //noinspection GradleDependency - we need to support older devices
    compileSdk = 28

    signingConfigs {
        create("release") {
            this.storeFile = file(localStoreFile)
            this.storePassword = localStorePassword
            this.keyAlias = localKeyAlias
            this.keyPassword = localKeyPassword
        }
    }

    defaultConfig {
        applicationId = "wifi.auto.hotspot.wifi.control.provider.content"
        minSdk = 24
        //noinspection ExpiredTargetSdkVersion - we need to support older devices
        targetSdk = 28
        versionCode = 5
        versionName = "1.1.3"
    }

    lint.abortOnError = false

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
}