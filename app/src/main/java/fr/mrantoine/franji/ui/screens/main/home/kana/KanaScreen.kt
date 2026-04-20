package fr.mrantoine.franji.ui.screens.main.home.kana

import androidx.activity.compose.BackHandler
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
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabState

enum class KanaState {
    CategoryLsit,
    Category,
    Info,
    Search
}


@Composable
fun KanaScreen(
    navController: NavController,
    state: KanaState,
    categoryPath: String,
    kanaId: String

) {
    var state by rememberSaveable { mutableStateOf(state) }
    var search_text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            HomeTopBar(
                text = search_text,
                onSearchChanged = { text ->
                    state = KanaState.Search
                    search_text = text
                },
                navController = navController,
                selectedCategoryIndex = 1
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        var categoryPath by rememberSaveable { mutableStateOf(categoryPath) }
        var kanaId by rememberSaveable { mutableStateOf(kanaId) }


        when(state) {
            KanaState.CategoryLsit -> {
                categoryPath = ""
                kanaId = ""
                keyboardController?.hide()
                KanaCategoryListScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { arg ->
                        state = KanaState.Category
                        categoryPath = arg
                    },
                )
            }
            KanaState.Category -> {
                kanaId = ""
                keyboardController?.hide()
                BackHandler {
                    state = KanaState.CategoryLsit
                }
                KanaCategoryScreen(
                    modifier = Modifier.padding(innerPadding),
                    categoryPath = categoryPath,
                    onClick = { arg ->
                        state = KanaState.Info
                        kanaId = arg
                    },
                )
            }
            KanaState.Info -> {
                keyboardController?.hide()
                BackHandler {
                    if (categoryPath != "") {
                        state = KanaState.Category
                    } else {
                        state = KanaState.CategoryLsit
                    }
                }
                KanaInfoScreen(
                    modifier = Modifier.padding(innerPadding),
                    kanaId = kanaId
                )
            }
            KanaState.Search -> {
                BackHandler {
                    if(kanaId != "") {
                        state = KanaState.Info
                    }else if (categoryPath != "") {
                        state = KanaState.Category
                    } else {
                        state = KanaState.CategoryLsit
                    }
                }
                KanaSearchScreen(
                    modifier = Modifier.padding(innerPadding),
                    search_text = search_text,
                    onClick = { arg ->
                        state = KanaState.Info
                        kanaId = arg
                    }
                )
            }
        }
    }
}
