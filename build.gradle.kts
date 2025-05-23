// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
allprojects {
    configurations.all {
        resolutionStrategy {
            force ("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
        }
    }
}

buildscript {
    repositories {
        google()
        mavenCentral() 
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.9")

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}
