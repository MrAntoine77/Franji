package fr.mrantoine.franji.ui.screens.main.home.grammar

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
    var state by remember { mutableStateOf(GrammarState.CategoryLsit) }
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
        var categoryPath by remember { mutableStateOf("") }
        var grammarId by remember { mutableStateOf("") }


        when(state) {
            GrammarState.CategoryLsit -> {
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
                keyboardController?.hide()
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
                GrammarInfoScreen(
                    modifier = Modifier.padding(innerPadding),
                    grammarId = grammarId
                )
            }
            GrammarState.Search -> {
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
