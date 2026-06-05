package dev.whysoezzy.meetings.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme

@Composable
fun MeetingsApp(modifier: Modifier = Modifier) {
    MeetingsTheme{
        Surface(
            modifier
                .background(MeetingsTheme.colors.background)
                .fillMaxSize()
        ) {
            Text("Hello, Meetings!")
        }
    }
}
