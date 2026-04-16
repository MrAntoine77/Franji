package fr.mrantoine.franji.ui.screens.main.home

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.MainPage
import fr.mrantoine.franji.storage.MainPageStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HeaderRow
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.navigation.HorizontalScrollableList
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AllScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var backPressedOnce by remember { mutableStateOf(false) }

    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true

            Toast.makeText(
                context,
                "Appuie encore une fois pour quitter",
                Toast.LENGTH_SHORT
            ).show()

            scope.launch {
                delay(2000)
                backPressedOnce = false
            }
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 0
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        val scrollState = rememberLazyListState()
        var kanjiPages by remember { mutableStateOf(MainPage()) }
        var vocabPages by remember { mutableStateOf(MainPage()) }
        var grammarPages by remember { mutableStateOf(MainPage()) }

        LaunchedEffect(Unit) {
            kanjiPages = MainPageStorage.getMainPage("Kanji")
            vocabPages = MainPageStorage.getMainPage("Vocabulaire")
            grammarPages = MainPageStorage.getMainPage("Grammaire")
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
                    onClick = { navController.navigate(Screen.KanjiCategoryList.route) }
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
                    onClick = { navController.navigate(Screen.VocabCategoryList.route) }
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
                    onClick = { navController.navigate(Screen.GrammarCategoryList.route) }
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













