package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen

@Composable
fun HomeTopBar(
    navController: NavController,
    selectedCategoryIndex: Int = 0
) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.statusBarsPadding()) {
        SearchBar(
            value = searchText,
            onValueChange = { searchText = it },
            onSettingsClick = { navController.navigate(Screen.Settings.route) }
        )
        CategoryBar(navController = navController, selectedCategoryIndex = selectedCategoryIndex)
    }
}