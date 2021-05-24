plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(property("COMPILE_SDK_VERSION").toString().toInt())

    defaultConfig {
        applicationId("me.panpf.recycler.sticky.sample")

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
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("KOTLIN")}")
//
//    implementation("androidx.appcompat:appcompat:${property("ANDROIDX_APPCOMPAT")}")
//    implementation("androidx.annotation:annotation:${property("ANDROIDX_ANNOTATION")}")
//    implementation("androidx.recyclerview:recyclerview:${property("ANDROIDX_RECYCLERVIEW")}")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${property("LIFECYCLE")}")
//    implementation("androidx.constraintlayout:constraintlayout:${property("ANDROIDX_CONSTRAINTLAYOUT")}")
//
//    implementation("me.panpf:sketch:${property("SKETCH_VERSION")}")
//    implementation("io.github.panpf.assemblyadapter:assemblyadapter:${property("ASSEMBLY_ADAPTER_VERSION")}")
//    implementation("io.github.panpf.assemblyadapter:assemblyadapter-ktx:${property("ASSEMBLY_ADAPTER_VERSION")}")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("KOTLIN")}")

    implementation("com.android.support:appcompat-v7:${property("SUPPORT_VERSION")}")
    implementation("com.android.support:recyclerview-v7:${property("SUPPORT_VERSION")}")
    implementation("com.android.support.constraint:constraint-layout:${property("CONSTRAINT_LAYOUT_VERSION")}")
    implementation("android.arch.lifecycle:extensions:${property("LIFECYCLE_VERSION")}")

    implementation("me.panpf:sketch:${property("SKETCH_VERSION")}")
    implementation("me.panpf:assembly-adapter:${property("ASSEMBLY_ADAPTER_VERSION")}")
    implementation("me.panpf:assembly-adapter-ktx:${property("ASSEMBLY_ADAPTER_VERSION")}")

    implementation(project(":sticky-recycler-item-decoration"))
}
