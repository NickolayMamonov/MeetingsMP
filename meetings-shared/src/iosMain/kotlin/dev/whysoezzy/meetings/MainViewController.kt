package dev.whysoezzy.meetings

import androidx.compose.ui.window.ComposeUIViewController
import dev.whysoezzy.meetings.compose.MeetingsApp

fun MainViewController() = ComposeUIViewController { MeetingsApp() }