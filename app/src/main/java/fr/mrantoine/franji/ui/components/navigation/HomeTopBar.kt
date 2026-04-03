package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun HomeTopBar(
    navController: NavController,
    selectedCategoryIndex: Int = 0
) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.statusBarsPadding()) {
        SearchBar(
            value = searchText,
            onValueChange = { searchText = it }
        )
        CategoryBar(navController = navController, selectedCategoryIndex = selectedCategoryIndex)
    }
}