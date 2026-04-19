package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen

@Composable
fun HomeTopBar(
    navController: NavController,
    selectedCategoryIndex: Int = 0,
    onSearchChanged: (String) -> Unit = {},
    text: String = ""
) {
    var searchText by remember { mutableStateOf(text) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        SearchBar(
            value = searchText,
            onValueChange = {
                searchText = it

            },

            modifier = Modifier
                .focusRequester(focusRequester),
            onSettingsClick = {
                navController.navigate(Screen.Settings.route)
            },

            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchChanged(searchText)
                    keyboardController?.hide()
                }
            )
        )

        CategoryBar(
            navController = navController,
            selectedCategoryIndex = selectedCategoryIndex
        )
    }

}