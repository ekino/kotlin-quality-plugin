
plugins {
    id("com.ekino.oss.plugin.kotlin-quality")
}

repositories {
    mavenCentral()
    jcenter()
}

kotlinQuality {
    customDetektConfig = "my-detekt.yml"
}
