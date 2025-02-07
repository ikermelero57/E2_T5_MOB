import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.e2_t5_mob"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.e2_t5_mob"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {


        implementation ("androidx.cardview:cardview:1.0.0")


    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

//    implementation ("org.hibernate:hibernate-core:5.4.30.Final")  // Versión de Hibernate, usa la última compatible con tu servidor
//    implementation ("javax.persistence:javax.persistence-api:2.2")  // Para la parte de JPA, si usas alguna
//    implementation ("org.hibernate:hibernate-entitymanager:5.4.30.Final") // Si necesitas la parte de EntityManage

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}