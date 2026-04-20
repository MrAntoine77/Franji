package fr.mrantoine.franji.ui.screens.main.quizz

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.MainPage
import fr.mrantoine.franji.storage.MainPageStorage
import fr.mrantoine.franji.ui.components.ComingSoonBox
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HeaderRow
import fr.mrantoine.franji.ui.components.navigation.HorizontalScrollableList
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.components.navigation.Tree
import fr.mrantoine.franji.ui.screens.main.cards.buildMap
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiState
import fr.mrantoine.franji.ui.theme.Dimens
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun QuizzListScreen(
    navController: NavController
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding())
            {
                TopBar(
                    title = "Quizz",
                    showBack = true,
                    onBackClick =  { backDispatcher?.onBackPressed() }
                )
            }
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 1,
                navController = navController
            )
        }
    ) { innerPadding ->
        val scrollState = rememberLazyListState()
        var kanjiPages by remember { mutableStateOf(MainPage()) }
        var vocabPages by remember { mutableStateOf(MainPage()) }
        var grammarPages by remember { mutableStateOf(MainPage()) }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            kanjiPages = MainPageStorage.getMainPage(context, "Kanji")
            vocabPages = MainPageStorage.getMainPage(context,"Vocabulaire")
            grammarPages = MainPageStorage.getMainPage(context,"Grammaire")
        }

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
                    onClick = { navController.navigate(Screen.KanjiList.route(
                        state = KanjiState.Category,
                        categoryPath = "",
                        kanjiId = ""
                    )) }
                )
            }
            item {
                HorizontalScrollableList(
                    navController = navController,
                    page = kanjiPages,
                )
            }
            item {
                HeaderRow(
                    text = "Vocabulaire",
                    onClick = { navController.navigate(Screen.VocabList.route) }
                )
            }
            item {
                HorizontalScrollableList(
                    navController = navController,
                    page = vocabPages,
                )
            }
            item {
                HeaderRow(
                    text = "Grammaire",
                    onClick = { navController.navigate(Screen.GrammarList.route) }
                )
            }
            item {
                HorizontalScrollableList(
                    navController = navController,
                    page = grammarPages,
                )
            }
        }
    }
}