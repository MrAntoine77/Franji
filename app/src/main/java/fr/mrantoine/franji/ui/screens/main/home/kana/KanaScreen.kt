package fr.mrantoine.franji.ui.screens.main.home.kana

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

enum class KanaState {
    CategoryLsit,
    Category,
    Info,
    Search
}


@Composable
fun KanaScreen(
    navController: NavController,
) {
    var state by remember { mutableStateOf(KanaState.CategoryLsit) }
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
        var categoryPath by remember { mutableStateOf("") }
        var kanaId by remember { mutableStateOf("") }


        when(state) {
            KanaState.CategoryLsit -> {
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
                keyboardController?.hide()
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
                KanaInfoScreen(
                    modifier = Modifier.padding(innerPadding),
                    kanaId = kanaId
                )
            }
            KanaState.Search -> {
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
