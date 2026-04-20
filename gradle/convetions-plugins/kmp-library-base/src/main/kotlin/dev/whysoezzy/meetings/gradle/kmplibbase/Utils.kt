package dev.whysoezzy.meetings.gradle.kmplibbase

import org.gradle.api.plugins.PluginContainer

/**
 * Apply plugin if it is not applied yet
 */
internal fun PluginContainer.applyIfNeeded(id: String): Boolean {
    if (hasPlugin(id)) return false

    apply(id)
    return true
}