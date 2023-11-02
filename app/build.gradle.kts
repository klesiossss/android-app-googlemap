

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.secrets_gradle_plugin") version("0.5")
}


android {
    namespace = "com.consultancyti.gmaps"
    compileSdk = 33


    defaultConfig {
        applicationId = "com.consultancyti.gmaps"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            viewBinding = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:19.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.okhttp3:okhttp:3.8.1")
    //implementation 'com.google.code.gson:gson:2.9.0' // Gradle
    implementation("com.google.code.gson:gson:2.9.0") // Gradle KTS
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.7.1")
    implementation("com.google.code.gson:gson:2.9.0")



    implementation ("com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.4.0") // for cache
    implementation ("javax.annotation:javax.annotation-api:1.3.2")
    implementation ("com.google.maps.android:android-maps-utils:2.3.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-alpha02")
    // ViewModel and LiveData
    //implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
   // implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
   // implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    implementation ("com.google.android.libraries.places:places:2.5.0")

}