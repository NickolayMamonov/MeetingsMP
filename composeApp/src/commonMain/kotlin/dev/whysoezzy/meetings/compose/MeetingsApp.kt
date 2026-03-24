package dev.whysoezzy.meetings.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uikit.MeetingsTheme

@Composable
@Preview
fun MeetingsApp() {
    MeetingsTheme{
        Surface(
            Modifier.background(MeetingsTheme.colors.background)
                .fillMaxSize()
        ) {
            Text("Hello, Meetings!")
        }
    }
}