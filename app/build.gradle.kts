plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // إزالة التكرار
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.mealapp"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.mealapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
        dataBinding = true
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase dependencies (auth and BOM)
    implementation ("com.google.firebase:firebase-auth:21.1.0")  // استخدام أحدث نسخة
//    implementation ("com.google.firebase:firebase-bom:31.0.1")  // باستخدام BOM للتحكم بالإصدارات

    // Play Services dependencies
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)  // تتوافق مع الإصدارات الأخرى

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
