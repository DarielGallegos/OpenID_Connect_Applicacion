plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "pm1.iiipac.openidconnectapplicacion"
    compileSdk = 34

    defaultConfig {
        applicationId = "pm1.iiipac.openidconnectapplicacion"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.11.0")

    val versionRetrofit = "2.9.0"
    val versionOkHttp = "4.9.0"

    //Implementaciones para Retrofit y OkHttp
    implementation("com.squareup.retrofit2:retrofit:$versionRetrofit")
    implementation("com.squareup.retrofit2:converter-gson:$versionRetrofit")
    implementation("com.squareup.okhttp3:logging-interceptor:$versionOkHttp")
    implementation("com.squareup.okhttp3:okhttp:$versionOkHttp")
    implementation("com.google.code.gson:gson:2.11.0")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}