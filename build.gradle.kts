import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.DefaultGenerator
import org.jooq.codegen.example.JPrefixGeneratorStrategy
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.SchemaMappingType
import org.jooq.meta.mariadb.MariaDBDatabase

val commandLineRunnerClass = "org.springframework.batch.core.launch.support.CommandLineJobRunner"
val devDatabaseDriverClass = "org.mariadb.jdbc.Driver"
val devDatabaseSchema = "aplus_point"
val devDatabaseUrl = "jdbc:mariadb://dev-aplus-db01.altools.co.kr:3307/$devDatabaseSchema"
val devDatabaseUser = "apluspoint"
val devDatabasePassword = "eoqkr1!"

val jooqGeneratorName = "main"
val jooqPackageName = "jooq.dsl"
val jooqDirectory = "src/generated/jooq"
val jooqEncoding = "UTF-8"
val jooqIncludeTables = listOf(
    "point_accounting_history",
    "point_accounting_record",
    "point_history"
).joinToString("|")

plugins {
    val ktlintVersion = "11.0.0"
    val stringBootVersion = "3.2.4"
    val springDependencyManagementVersion = "1.1.4"
    val kotlinVersion = "1.9.23"
    val jooqVersion = "7.1.1"

    id("org.springframework.boot") version stringBootVersion
    id("io.spring.dependency-management") version springDependencyManagementVersion
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("nu.studer.jooq") version jooqVersion
    id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
}

group = "com.aplus"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val kotestVersion = "5.3.2"
val fixtureMonkeyVersion = "1.0.15"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    jooqGenerator("org.mariadb.jdbc:mariadb-java-client")
    testImplementation("org.testcontainers:mariadb")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:$fixtureMonkeyVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    configurations {
        create(jooqGeneratorName) {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = devDatabaseDriverClass
                    url = devDatabaseUrl
                    user = devDatabaseUser
                    password = devDatabasePassword
                }
                generator.apply {
                    name = DefaultGenerator::class.java.name
                    database.apply {
                        name = MariaDBDatabase::class.java.name
                        includes = jooqIncludeTables
                        schemata = listOf(
                            SchemaMappingType().apply { inputSchema = devDatabaseSchema },
                            SchemaMappingType().apply { inputSchema = "${devDatabaseSchema}_meta" }
                        )
                        forcedTypes.addAll(
                            listOf(
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "JSONB?"
                                },
                                ForcedType().apply {
                                    name = "varchar"
                                    includeExpression = ".*"
                                    includeTypes = "INET"
                                },
                                ForcedType().apply {
                                    name = "BOOLEAN"
                                    includeTypes = "(?i:TINYINT\\(1\\))"
                                }
                            )
                        )
                    }
                    generate.apply {
                        withDeprecated(false)
                        withRecords(true)
                        withImmutablePojos(true)
                        withFluentSetters(true)
                        withJavaTimeTypes(true)
                    }
                    target.apply {
                        withPackageName(jooqPackageName)
                        withDirectory(jooqDirectory)
                        withEncoding(jooqEncoding)
                    }
                    strategy.name = JPrefixGeneratorStrategy::class.java.name
                }
            }
        }
    }
}

tasks.test {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    useJUnitPlatform()
}
