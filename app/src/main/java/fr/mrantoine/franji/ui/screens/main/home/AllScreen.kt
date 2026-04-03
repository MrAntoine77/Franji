package fr.mrantoine.franji.ui.screens.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HeaderRow
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.navigation.HorizontalScrollableList
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun AllScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                navController=navController,
                selectedCategoryIndex = 0
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.navigationBarsPadding(),
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        val scrollState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(Dimens.m)
        ) {
            item {
                HeaderRow(
                    text = "Kanji",
                    onClick = { navController.navigate(Screen.KanjiCategoryList.route) }
                )
            }
            item {
                HorizontalScrollableList(
                    items = listOf("JLPT5", "JLPT4", "JLPT3", "JLPT2", "JLPT1"),
                    onItemClick = { navController.navigate(Screen.KanjiCategory.route("Kanji/JLPT5/Tout")) }
                )
            }
        }
    }















}