package fr.mrantoine.franji.ui.screens.main.quizz

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.TopBar
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
fun QuizzListScreen(
    navController: NavController
) {
    var paths by remember { mutableStateOf<Array<String>>(emptyArray()) }
    var maps by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        paths = CategoryStorage.getKeys(context, "Quizz")
        maps = buildMap(paths)
    }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding())
            {
                TopBar(
                    title = "Révision",
                    showBack = true,
                    onBackClick =  { backDispatcher?.onBackPressed() }
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
                onClick = { path ->
                    navController.navigate(Screen.QuizzPlaying.route(path))
                }
            )
        }
    }
}