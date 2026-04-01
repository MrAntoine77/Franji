package fr.mrantoine.franji.ui.screens.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.HeaderRow
import fr.mrantoine.franji.ui.components.HorizontalScrollableList
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun AllScreen(
    selectedCategory: Int = 0,
    onCategorySelected: (Int) -> Unit = {}
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(Dimens.m)
    ) {
        item {
            HeaderRow(
                text = "Kanji",
                onClick = { onCategorySelected(1) }
            )
        }
        item {
            HorizontalScrollableList(
                items = listOf("JLPT5", "JLPT4", "JLPT3", "JLPT2", "JLPT1"),
                onItemClick = { onCategorySelected(1) }
            )
        }

        item {
            HeaderRow(
                text = "Kana",
                onClick = { onCategorySelected(1) }
            )
        }
        item {
            HorizontalScrollableList(
                items = listOf("Hiragana", "Katakana"),
                onItemClick = { onCategorySelected(1) }
            )
        }

        item {
            HeaderRow(
                text = "Vocabulaire",
                onClick = { onCategorySelected(2) }
            )
        }
        item {
            HorizontalScrollableList(
                items = listOf("Nature", "Famille", "Véhicules", "Travail", "Compteurs"),
                onItemClick = { onCategorySelected(2) }
            )
        }

        item {
            HeaderRow(
                text = "Grammaire",
                onClick = { onCategorySelected(3) }
            )
        }
        item {
            HorizontalScrollableList(
                items = listOf("Particule TO", "Particule NO", "Particule WA"),
                onItemClick = { onCategorySelected(3) }
            )
        }
    }
}