package fr.mrantoine.franji.ui.screens.main.home.grammar

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
import fr.mrantoine.franji.ui.screens.main.home.grammar.GrammarCategoryListScreen
import fr.mrantoine.franji.ui.screens.main.home.grammar.GrammarCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.grammar.GrammarInfoScreen
import fr.mrantoine.franji.ui.screens.main.home.grammar.GrammarSearchScreen
import fr.mrantoine.franji.ui.screens.main.home.kana.KanaState

enum class GrammarState {
    CategoryLsit,
    Category,
    Info,
    Search
}


@Composable
fun GrammarScreen(
    navController: NavController,
) {
    var state by rememberSaveable { mutableStateOf(GrammarState.CategoryLsit) }
    var search_text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            HomeTopBar(
                text = search_text,
                onSearchChanged = { text ->
                    state = GrammarState.Search
                    search_text = text
                },
                navController = navController,
                selectedCategoryIndex = 3
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        var categoryPath by rememberSaveable { mutableStateOf("") }
        var grammarId by rememberSaveable { mutableStateOf("") }


        when(state) {
            GrammarState.CategoryLsit -> {
                categoryPath = ""
                grammarId = ""
                keyboardController?.hide()
                GrammarCategoryListScreen(
                    modifier = Modifier.padding(innerPadding),
                    onClick = { arg ->
                        state = GrammarState.Category
                        categoryPath = arg
                    },
                )
            }
            GrammarState.Category -> {
                grammarId = ""
                keyboardController?.hide()
                BackHandler {
                    state = GrammarState.CategoryLsit
                }
                GrammarCategoryScreen(
                    modifier = Modifier.padding(innerPadding),
                    categoryPath = categoryPath,
                    onClick = { arg ->
                        state = GrammarState.Info
                        grammarId = arg
                    },
                )
            }
            GrammarState.Info -> {
                keyboardController?.hide()
                BackHandler {
                    if (categoryPath != "") {
                        state = GrammarState.Category
                    } else {
                        state = GrammarState.CategoryLsit
                    }
                }
                GrammarInfoScreen(
                    modifier = Modifier.padding(innerPadding),
                    grammarId = grammarId
                )
            }
            GrammarState.Search -> {
                BackHandler {
                    if(grammarId != "") {
                        state = GrammarState.Info
                    }else if (categoryPath != "") {
                        state = GrammarState.Category
                    } else {
                        state = GrammarState.CategoryLsit
                    }
                }
                GrammarSearchScreen(
                    modifier = Modifier.padding(innerPadding),
                    search_text = search_text,
                    onClick = { arg ->
                        state = GrammarState.Info
                        grammarId = arg
                    }
                )
            }
        }
    }
}
