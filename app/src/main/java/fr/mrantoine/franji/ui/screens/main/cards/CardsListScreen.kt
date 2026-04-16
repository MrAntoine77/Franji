package fr.mrantoine.franji.ui.screens.main.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.SearchBar
import fr.mrantoine.franji.ui.components.navigation.Tree



fun buildMap(paths: Array<String>): Map<String, Any> {
    print(paths)

    val result = mutableMapOf<String, Any>()

    paths.forEach { path ->
        val parts = path.split("/")
        var currentLevel = result
        parts.forEachIndexed { index, part ->
            if (index == parts.lastIndex) {
                currentLevel[part] = 0
            } else {
                if (currentLevel[part] !is MutableMap<*, *>) {
                    currentLevel[part] = mutableMapOf<String, Any>()
                }
                currentLevel = currentLevel[part] as MutableMap<String, Any>
            }
        }
    }
    return result
}

@Composable
fun CardsListScreen(
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }

    var paths by remember { mutableStateOf<Array<String>>(emptyArray()) }
    var maps by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }


    LaunchedEffect(Unit) {
        paths = CategoryStorage.getKeys()
        maps = buildMap(paths)
    }


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
            )
            {
                SearchBar(
                    value = searchText,
                    onValueChange = { searchText = it },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.secondary,
                    thickness = 2.dp
                )
            }
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 1,
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
                    navController.navigate(Screen.CardsRecap.route(arg))
                }
            )
        }
    }
}