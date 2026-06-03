import dev.whysoezzy.meetings.gradle.kmplibbase.applyIfNeeded
import dev.whysoezzy.meetings.gradle.kmplibbase.detektConfig
import dev.whysoezzy.meetings.gradle.kmplibbase.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins.applyIfNeeded(libs.plugins.detekt.get().pluginId)

detektConfig {
    toolVersion = libs.versions.detekt.get()

    config.setFrom(
        File(rootProject.rootDir, "config/detekt/detekt.yml"),
        File(rootProject.rootDir, "config/detekt/detekt-compose.yml"),
    )

    buildUponDefaultConfig = false
    allRules = false
    baseline = file("detekt-baseline.xml")
    disableDefaultRuleSets = false
    debug = false
    ignoreFailures = false
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    setSource(projectDir)
    include("**/src/*/kotlin/**/*.kt")

    exclude(
        "gradle/conventions-plugins",
        "**/build/generated/",
    )

    with(this.project) {
        reports {
            xml.apply {
                isEnabled = true
                outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt.xml"))
            }

            txt.apply {
                isEnabled = true
                outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt.txt"))
            }

            html.apply {
                isEnabled = true
                outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt.html"))
            }

            sarif.apply {
                isEnabled = false
                outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt.sarif"))
            }

            md.apply {
                isEnabled = true
                outputLocation.set(layout.buildDirectory.file("reports/detekt/detekt.md"))
            }
        }
    }
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    setSource(projectDir)
    include("**/src/*/kotlin/**/*.kt")

    exclude(
        "gradle/conventions-plugins",
        "**/build/generated/",
    )
}

dependencies.add("detektPlugins", libs.detektPlugin.nlopez.composeRules)
