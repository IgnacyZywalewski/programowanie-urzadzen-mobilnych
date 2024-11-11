buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.3")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
}