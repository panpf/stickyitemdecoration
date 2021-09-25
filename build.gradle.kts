// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/") }
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${property("ANDROID_GRADLE")}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("KOTLIN")}")
        classpath("io.github.panpf.maven-publish:maven-publish-gradle-plugin:${property("MAVEN_PUBLISH")}")
    }
}

allprojects {
    repositories {
        maven { setUrl("https://mirrors.huaweicloud.com/repository/maven/") }
        mavenCentral()
        google()
        jcenter()
    }
}