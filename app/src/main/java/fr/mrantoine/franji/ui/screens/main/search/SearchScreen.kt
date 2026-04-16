package fr.mrantoine.franji.ui.screens.main.search

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fr.mrantoine.franji.ui.components.ComingSoonBox
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.TopBar


@Composable
fun SearchScreen(
    navController: NavController
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .background(MaterialTheme.colorScheme.background))
            {
                TopBar(
                    title = "Rechercher",
                    showBack = true,
                    onBackClick =  { backDispatcher?.onBackPressed() }
                )
            }
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 4,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            ComingSoonBox()
        }
    }
}