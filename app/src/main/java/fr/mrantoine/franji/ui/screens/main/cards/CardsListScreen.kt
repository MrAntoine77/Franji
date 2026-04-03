package fr.mrantoine.franji.ui.screens.main.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import fr.mrantoine.franji.network.getCategories
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.CategoryBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.navigation.SearchBar
import fr.mrantoine.franji.ui.components.navigation.Tree


@Composable
fun CardsListScreen(
    navController: NavController
) {
    var categories by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        categories = getCategories()
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                SearchBar(
                    value = searchText,
                    onValueChange = { searchText = it }
                )
            }
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.navigationBarsPadding(),
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
                treeData = categories,
                onClick = {arg ->
                    navController.navigate(Screen.CardsRecap.route(arg))
                }
            )
        }
    }
}