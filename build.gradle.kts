buildscript {
    extra.apply{
        set("compose_ui_version", "1.4.0-beta01")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("com.android.library") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}