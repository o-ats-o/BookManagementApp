// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    kotlin("jvm") version "1.9.23" apply false
    id ("dagger.hilt.android.plugin") version "2.51.1" apply false
}
