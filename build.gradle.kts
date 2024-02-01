buildscript {
    extra.apply{
        set("compose_ui_version", "1.4.0-beta01")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("com.android.library") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}