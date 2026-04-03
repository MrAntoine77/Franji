package fr.mrantoine.franji.ui.screens.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fr.mrantoine.franji.network.Kanji
import fr.mrantoine.franji.network.getKanjiByChar
import fr.mrantoine.franji.network.getLottie
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun KanjiInfoScreen(
    navController: NavController,
    kanjiChar: String
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 1
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
        var kanji by remember { mutableStateOf(Kanji()) }
        var lottie by remember { mutableStateOf("{}") }


        LaunchedEffect(kanjiChar) {
            kanji = getKanjiByChar(kanjiChar)
            kanji.let {
                lottie = getLottie(it.id)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(Dimens.m)
        ) {

            item {
                LaunchedEffect(Unit) {
                    lottie = getLottie(kanji.id)
                }
                Lottie(
                    data = lottie
                )
            }

            items(kanji.lectures.size) { index ->
                Lecture(
                    french = kanji.lectures[index].fr,
                    ON = kanji.lectures[index].ON,
                    kun = kanji.lectures[index].kun
                )
            }
        }
    }
}