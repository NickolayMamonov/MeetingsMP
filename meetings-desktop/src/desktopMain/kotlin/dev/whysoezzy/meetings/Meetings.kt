@file:JvmName("Meetings")
package dev.whysoezzy.meetings

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.whysoezzy.meetings.compose.MeetingsApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Meetings",
    ) {
        MeetingsApp()
    }
}