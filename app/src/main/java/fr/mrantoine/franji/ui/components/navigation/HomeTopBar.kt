package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen

@Composable
fun HomeTopBar(
    navController: NavController,
    selectedCategoryIndex: Int = 0,
    autoFocusSearch: Boolean = false,
    onSearchChanged: (String) -> Unit = {},
    redirectRoute: String,
    text: String = ""
) {
    var searchText by remember { mutableStateOf(text) }
    var isFirstFocus by remember { mutableStateOf(true) }

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
                onSearchChanged(it)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && isFirstFocus) {
                        val currentRoute = navController.currentBackStackEntry?.destination?.route
                        if (currentRoute != redirectRoute) {
                            navController.navigate(redirectRoute)
                        }
                        isFirstFocus = false
                    }
                },
            onSettingsClick = {
                navController.navigate(Screen.Settings.route)
            }
        )

        CategoryBar(
            navController = navController,
            selectedCategoryIndex = selectedCategoryIndex
        )
    }

    LaunchedEffect(autoFocusSearch) {
        if (autoFocusSearch) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
}