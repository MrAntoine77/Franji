package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar

enum class KanjiState {
    CategoryLsit,
    Category,
    Info,
    Search
}


@Composable
fun KanjiScreen(
    navController: NavController,
) {
    var state by remember { mutableStateOf(KanjiState.CategoryLsit) }
    var search_text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            HomeTopBar(
                text = search_text,
                onSearchChanged = { text ->
                    state = KanjiState.Search
                    search_text = text
                },
                navController = navController,
                selectedCategoryIndex = 0
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        var categoryPath by remember { mutableStateOf("") }
        var kanjiId by remember { mutableStateOf("") }


        when(state) {
            KanjiState.CategoryLsit -> {
                keyboardController?.hide()
                KanjiCategoryListScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { arg ->
                        state = KanjiState.Category
                        categoryPath = arg
                    },
                )
            }
            KanjiState.Category -> {
                keyboardController?.hide()
                KanjiCategoryScreen(
                    modifier = Modifier.padding(innerPadding),
                    categoryPath = categoryPath,
                    onClick = { arg ->
                        state = KanjiState.Info
                        kanjiId = arg
                    },
                )
            }
            KanjiState.Info -> {
                keyboardController?.hide()
                KanjiInfoScreen(
                    modifier = Modifier.padding(innerPadding),
                    kanjiId = kanjiId
                )
            }
            KanjiState.Search -> {
                KanjiSearchScreen(
                    modifier = Modifier.padding(innerPadding),
                    search_text = search_text,
                    onClick = { arg ->
                        state = KanjiState.Info
                        kanjiId = arg
                    }
                )
            }
        }
    }
}

