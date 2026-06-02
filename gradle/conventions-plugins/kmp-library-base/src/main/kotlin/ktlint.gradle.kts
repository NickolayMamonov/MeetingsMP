import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.libs

plugins.applyIfNeeded("java")
private val ktLintConfig = configurations.create("ktlint")

dependencies {
    "ktlint"(libs.ktlint.cli) {
        attributes {
            attribute(
                Bundling.BUNDLING_ATTRIBUTE,
                getObjects().named<Bundling>(Bundling.EXTERNAL),
            )
        }
    }
}

private val sources = listOf("**/src/**/*.kt", "!**/build/generated/**")
private val ktLintCliMainClass = "com.pinterest.ktlint.Main"
private val reportArgs = listOf(
    "--reporter=plain",
    "--reporter=checkstyle,output=build/reports/ktlint/ktlint.xml",
    "--reporter=html,output=build/reports/ktlint/ktlint.html",
)

val ktLintCheckTask = tasks.register<JavaExec>("ktlintCheck") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = ktLintConfig
    mainClass = ktLintCliMainClass
    args = reportArgs + sources
}

tasks.named("check") {
    dependsOn(ktLintCheckTask)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = ktLintConfig
    mainClass = ktLintCliMainClass
    jvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args = sources
}
