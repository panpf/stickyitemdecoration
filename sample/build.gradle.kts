plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = property("COMPILE_SDK_VERSION").toString().toInt()

    defaultConfig {
        applicationId = "com.github.panpf.recycler.sticky.sample"

        minSdk = property("MIN_SDK_VERSION").toString().toInt()
        targetSdk = property("TARGET_SDK_VERSION").toString().toInt()
        versionCode = property("VERSION_CODE").toString().toInt()
        versionName = property("VERSION_NAME").toString()

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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":stickyitemdecoration"))
    implementation(project(":stickyitemdecoration-assemblyadapter4"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("KOTLIN")}")

    implementation("androidx.appcompat:appcompat:${property("ANDROIDX_APPCOMPAT")}")
    implementation("androidx.fragment:fragment-ktx:${property("ANDROIDX_FRAGMENT")}")
    implementation("androidx.recyclerview:recyclerview:${property("ANDROIDX_RECYCLERVIEW")}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${property("ANDROIDX_LIFECYCLE")}")
    implementation("androidx.constraintlayout:constraintlayout:${property("ANDROIDX_CONSTRAINTLAYOUT")}")
    implementation("androidx.navigation:navigation-fragment-ktx:${property("ANDROIDX_NAVIGATION")}")
    implementation("androidx.navigation:navigation-ui-ktx:${property("ANDROIDX_NAVIGATION")}")

    implementation("com.google.android.material:material:${property("GOOGLE_MATERIAL")}")
    implementation("me.panpf:sketch:${property("SKETCH_VERSION")}")
    implementation("com.github.promeg:tinypinyin:${property("TINYPINYIN")}")
    implementation("io.github.panpf.liveevent:liveevent:${property("LIVEEVENT")}")
    implementation("io.github.panpf.tools4a:tools4a-dimen-ktx:${property("TOOLS4A")}")
}