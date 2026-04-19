package fr.mrantoine.franji.ui.screens.main.home.vocab

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
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabCategoryListScreen
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabInfoScreen
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabSearchScreen


enum class VocabState {
    CategoryLsit,
    Category,
    Info,
    Search
}


@Composable
fun VocabScreen(
    navController: NavController,
) {
    var state by remember { mutableStateOf(VocabState.CategoryLsit) }
    var search_text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            HomeTopBar(
                text = search_text,
                onSearchChanged = { text ->
                    state = VocabState.Search
                    search_text = text
                },
                navController = navController,
                selectedCategoryIndex = 2
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
        var vocabId by remember { mutableStateOf("") }


        when(state) {
            VocabState.CategoryLsit -> {
                keyboardController?.hide()
                VocabCategoryListScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { arg ->
                        state = VocabState.Category
                        categoryPath = arg
                    },
                )
            }
            VocabState.Category -> {
                keyboardController?.hide()
                VocabCategoryScreen(
                    modifier = Modifier.padding(innerPadding),
                    categoryPath = categoryPath,
                    onClick = { arg ->
                        state = VocabState.Info
                        vocabId = arg
                    },
                )
            }
            VocabState.Info -> {
                keyboardController?.hide()
                VocabInfoScreen(
                    modifier = Modifier.padding(innerPadding),
                    vocabId = vocabId
                )
            }
            VocabState.Search -> {
                VocabSearchScreen(
                    modifier = Modifier.padding(innerPadding),
                    search_text = search_text,
                    onClick = { arg ->
                        state = VocabState.Info
                        vocabId = arg
                    }
                )
            }
        }
    }
}

