plugins {
    id("java-library")
    id("kotlin")
    id("appyx-publish-java")
    id("appyx-detekt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}