package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A lazy list/collection of person cards.
 *
 * @param persons List of persons to display.
 * @param onPersonClick Callback invoked when a person card is clicked.
 * @param modifier Modifier applied to the list.
 */
@Composable
fun UIKitPersonsGrid(
    persons: List<UIKitPerson>,
    onPersonClick: (UIKitPerson) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.small),
        contentPadding = PaddingValues(SpacingTokens.medium),
    ) {
        items(persons, key = { it.id }) { person ->
            UIKitPersonCard(
                person = person,
                onClick = { onPersonClick(person) },
            )
        }
    }
}
