plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(property("COMPILE_SDK_VERSION").toString().toInt())

    defaultConfig {
        minSdkVersion(property("MIN_SDK_VERSION").toString().toInt())
        targetSdkVersion(property("TARGET_SDK_VERSION").toString().toInt())
        versionCode = property("VERSION_CODE").toString().toInt()
        versionName = property("VERSION_NAME").toString()

        consumerProguardFiles("proguard-rules.pro")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
//    api("androidx.annotation:annotation:${property("ANDROIDX_ANNOTATION")}")
//    api("androidx.recyclerview:recyclerview:${property("ANDROIDX_RECYCLERVIEW")}")

    api("com.android.support:support-annotations:${property("SUPPORT_VERSION")}")
    api("com.android.support:recyclerview-v7:${property("SUPPORT_VERSION")}")
}

/**
 * publish config, The following properties are generally configured in the ~/.gradle/gradle.properties file
 */
if (hasProperty("signing.keyId")
    && hasProperty("signing.password")
    && hasProperty("signing.secretKeyRingFile")
    && hasProperty("mavenCentralUsername")
    && hasProperty("mavenCentralPassword")
) {
    apply { plugin("com.vanniktech.maven.publish") }

    configure<com.vanniktech.maven.publish.MavenPublishPluginExtension> {
        sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
    }
}
