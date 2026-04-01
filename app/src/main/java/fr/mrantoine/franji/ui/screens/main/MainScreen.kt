package fr.mrantoine.franji.ui.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import fr.mrantoine.franji.ui.components.BottomBar
import fr.mrantoine.franji.ui.components.SearchBar
import fr.mrantoine.franji.ui.screens.main.cards.CardsScreen
import fr.mrantoine.franji.ui.screens.main.home.HomeScreen
import fr.mrantoine.franji.ui.theme.Dimens


@Composable
fun MainScreen(
    onCardClick: (() -> Unit) = {}
) {
    val text = remember { mutableStateOf("") }
    val selectedMode = remember { mutableStateOf(0) }

    BackHandler(enabled = selectedMode.value != 0) {
        selectedMode.value = 0
    }


    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()

            ) {
                Box( modifier=Modifier.padding(all=Dimens.m)) {
                    SearchBar(
                        value = text.value,
                        onValueChange = { text.value = it }
                    )
                }
            }

        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.navigationBarsPadding(),
                selectedIndex = selectedMode.value,
                onItemSelected = { index ->
                    selectedMode.value = index
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedMode.value) {
                0 -> HomeScreen()
                1 -> CardsScreen(onCardClick = onCardClick)
            }
        }
    }
}
