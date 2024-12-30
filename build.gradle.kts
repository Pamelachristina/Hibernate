plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Hibernate and H2 dependencies
    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")
    implementation("com.h2database:h2:2.2.224")

    // JUnit dependencies for testing (optional)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application { // Configure the main class
    mainClass.set("org.example.Main")
}




