package fr.mrantoine.franji.ui.screens.main.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import fr.mrantoine.franji.ui.components.CategoryBar


@Composable
fun HomeScreen(

) {
    val selectedCategory = remember { mutableStateOf(0) }

    BackHandler(enabled = selectedCategory.value != 0) {
        selectedCategory.value = 0
    }

    val items = listOf(
        "Tout",
        "Kanji & Kana",
        "Vocabulaire",
        "Grammaire"
    )
    Column(
    ) {
        CategoryBar(
            selectedIndex = selectedCategory.value,
            onItemSelected = { index ->
                selectedCategory.value = index
            },
            items = items
        )
        when (selectedCategory.value) {
            0 -> AllScreen(
                selectedCategory = selectedCategory.value,
                onCategorySelected = { index -> selectedCategory.value = index }
            )
            1 -> KanjiScreen()
        }

    }
}