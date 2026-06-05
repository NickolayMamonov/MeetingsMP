package dev.whysoezzy.meetings.compose.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.components.UIKitButton
import dev.whysoezzy.meetings.compose.components.UIKitInput
import dev.whysoezzy.meetings.compose.components.UIKitTag
import dev.whysoezzy.meetings.compose.models.UIKitTagState
import dev.whysoezzy.meetings.compose.components.UIKitToggle
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.viewmodel.ProfileEditEvent
import dev.whysoezzy.meetings.compose.viewmodel.ProfileEditUiState
import dev.whysoezzy.meetings.compose.viewmodel.ProfileEditViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Profile edit screen for updating user profile information.
 */
@Composable
fun ProfileEditScreen(
    navController: MeetNavController,
    viewModel: ProfileEditViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is dev.whysoezzy.meetings.compose.viewmodel.ProfileEditNavEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = MeetingsTheme.colors.primary)
            }
        }
        else -> {
            ProfileEditContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
            )
        }
    }
}

@Composable
private fun ProfileEditContent(
    uiState: ProfileEditUiState,
    onEvent: (ProfileEditEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background)
            .verticalScroll(rememberScrollState())
            .padding(SpacingTokens.medium),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.medium),
    ) {
        Text(
            text = "Редактировать профиль",
            style = MeetingsTheme.typography.headlineMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        UIKitInput(
            value = uiState.name,
            onValueChange = { onEvent(ProfileEditEvent.NameChanged(it)) },
            label = "Имя",
            placeholder = "Введите имя",
            modifier = Modifier.fillMaxWidth(),
        )

        UIKitInput(
            value = uiState.surname,
            onValueChange = { onEvent(ProfileEditEvent.SurnameChanged(it)) },
            label = "Фамилия",
            placeholder = "Введите фамилию",
            modifier = Modifier.fillMaxWidth(),
        )

        UIKitInput(
            value = uiState.city,
            onValueChange = { onEvent(ProfileEditEvent.CityChanged(it)) },
            label = "Город",
            placeholder = "Введите город",
            modifier = Modifier.fillMaxWidth(),
        )

        UIKitInput(
            value = uiState.bio,
            onValueChange = { onEvent(ProfileEditEvent.BioChanged(it)) },
            label = "О себе",
            placeholder = "Расскажите о себе",
            modifier = Modifier.fillMaxWidth(),
        )

        // Interests / Tags
        if (uiState.availableTags.isNotEmpty()) {
            Text(
                text = "Интересы",
                style = MeetingsTheme.typography.titleMedium,
                color = MeetingsTheme.colors.onSurface,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.small),
            ) {
                uiState.availableTags.forEach { tag ->
                    val isSelected = tag.id in uiState.selectedTagIds
                    UIKitTag(
                        text = tag.name,
                        state = if (isSelected) UIKitTagState.SELECTED else UIKitTagState.ACTIVE,
                        onClick = { onEvent(ProfileEditEvent.TagToggled(tag.id)) },
                    )
                }
            }
        }

        // Toggles
        UIKitToggle(
            checked = uiState.showCommunities,
            onCheckedChange = { onEvent(ProfileEditEvent.ShowCommunitiesToggled(it)) },
        )
        Text(
            text = "Показывать сообщества",
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        UIKitToggle(
            checked = uiState.showMeetings,
            onCheckedChange = { onEvent(ProfileEditEvent.ShowMeetingsToggled(it)) },
        )
        Text(
            text = "Показывать встречи",
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        UIKitToggle(
            checked = uiState.notificationsEnabled,
            onCheckedChange = { onEvent(ProfileEditEvent.NotificationsToggled(it)) },
        )
        Text(
            text = "Уведомления",
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.onSurface,
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "Error",
                style = MeetingsTheme.typography.bodySmall,
                color = MeetingsTheme.colors.error,
            )
        }

        UIKitButton(
            text = "Сохранить",
            onClick = { onEvent(ProfileEditEvent.Save) },
            enabled = !uiState.isSaving,
            loading = uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
