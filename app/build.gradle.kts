plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ravi.github"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ravi.github"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit for API calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Room for offline caching
    implementation ("androidx.room:room-runtime:2.5.0")
    annotationProcessor ("androidx.room:room-compiler:2.5.0")

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")
    //firebase Authentication
    implementation("com.google.firebase:firebase-auth")
    // Firebase for FCM
    implementation ("com.google.firebase:firebase-messaging")

    implementation("com.google.android.gms:play-services-auth:21.3.0")
    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.6.0")

    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.3.1")

    // Glide for image loading (optional)
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    // Hilt for Dependency Injection
    implementation ("com.google.dagger:hilt-android:2.45")
    annotationProcessor ("com.google.dagger:hilt-compiler:2.45")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3") // Latest stable version as of now

}