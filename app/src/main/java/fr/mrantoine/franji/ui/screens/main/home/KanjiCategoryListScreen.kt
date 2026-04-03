package fr.mrantoine.franji.ui.screens.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.navigation.Tree
import fr.mrantoine.franji.ui.screens.main.cards.buildMap

@Composable
fun KanjiCategoryListScreen(
    navController: NavController
) {

    var paths by remember { mutableStateOf<Array<String>>(emptyArray()) }
    var maps by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }


    LaunchedEffect(Unit) {
        paths = CategoryStorage.getCategoriesPaths("Kanji")
        maps = buildMap(paths)
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 1
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.navigationBarsPadding(),
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Tree(
                treeData = maps,
                onClick = {arg ->
                    navController.navigate(Screen.KanjiCategory.route("Kanji/$arg"))
                }
            )
        }
    }
}