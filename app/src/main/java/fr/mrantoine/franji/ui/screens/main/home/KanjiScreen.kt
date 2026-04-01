package fr.mrantoine.franji.ui.screens.main.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.Tree
import fr.mrantoine.franji.ui.theme.Dimens
import kotlin.to

enum class KanjiScreenState {
    CATEGORY,
    LIST,
    KANJI
}
@Composable
fun KanjiScreen() {
    var state by remember { mutableStateOf(KanjiScreenState.CATEGORY) }
    var categoryTitle by remember { mutableStateOf("Default") }

    when (state) {
        KanjiScreenState.CATEGORY ->  {
            val temp_data = mapOf(
                "Niveaux" to mapOf(
                    "JLPT 5" to mapOf(
                        "JLPT 5 1-20" to listOf("kanji1", "kanji2"),
                        "JLPT 5 21-40" to listOf("kanji3", "kanji4")
                    ),
                    "JLPT 4" to mapOf(
                        "JLPT 4 41-60" to listOf("kanji5", "kanji6"),
                        "JLPT 4 61-80" to listOf("kanji7", "kanji8")
                    )
                ),
                "Catégories" to mapOf(
                    "Nature" to listOf(""),
                    "Famille" to listOf(""),
                    "Travail" to listOf(""),
                    "Véhicules" to listOf(""),
                    "Compteurs" to listOf("")
                ),
                "Kana" to mapOf(
                    "Hiragana" to listOf("a", "i" ,"u"),
                    "Katakana" to listOf("a", "i" ,"u")
                )
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Tree(
                    treeData = temp_data,
                    onClick = {arg ->
                        state = KanjiScreenState.LIST
                        categoryTitle = arg
                    }
                )
            }
        }
        KanjiScreenState.LIST -> {
            BackHandler() {
                state = KanjiScreenState.CATEGORY
            }
            KanjiListScreen(
                onKanjiClick = {state = KanjiScreenState.KANJI},
                title = categoryTitle
            )
        }
        KanjiScreenState.KANJI -> {
            BackHandler() {
                state = KanjiScreenState.LIST
            }
            val scrollState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(Dimens.m)
            ) {
                item {
                    Lottie()
                }
                item {
                    KanjiInfoScreen()
                }
            }

        }
    }

}