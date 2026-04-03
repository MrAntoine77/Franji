package fr.mrantoine.franji.ui.components

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import fr.mrantoine.franji.ui.theme.Dimens

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
        CategoryBar(navController=navController, selectedCategoryIndex = selectedCategoryIndex)
    }
}