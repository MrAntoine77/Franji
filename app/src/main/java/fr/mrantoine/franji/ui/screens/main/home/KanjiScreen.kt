package fr.mrantoine.franji.ui.screens.main.home

import Category
import Kanji
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.Tree
import fr.mrantoine.franji.ui.theme.Dimens
import getCategory
import getCategoryArray
import getKanjiByCharId
import getLottie
import kotlinx.coroutines.launch
import kotlin.to

enum class KanjiScreenState {
    CATEGORY,
    LIST,
    KANJI
}
@Composable
fun KanjiScreen() {
    var state by remember { mutableStateOf(KanjiScreenState.CATEGORY) }
    var path by remember { mutableStateOf("Default") }
    var selected_kanji by remember { mutableStateOf<Kanji>(Kanji("", emptyList(), "")) }


    var category by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }
    var kanji_list by remember { mutableStateOf(emptyArray<String>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        category = getCategory("")
    }

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
                    treeData = category,//temp_data,
                    onClick = {arg ->
                        state = KanjiScreenState.LIST
                        path = arg
                    }
                )
            }
        }
        KanjiScreenState.LIST -> {

            LaunchedEffect(Unit) {
                kanji_list = getCategoryArray(path)
                print(kanji_list)
            }

            BackHandler() {
                state = KanjiScreenState.CATEGORY
            }
            KanjiListScreen(
                onKanjiClick = {kanji ->
                    selected_kanji = kanji
                    state = KanjiScreenState.KANJI
                               },
                path = path,
                kanjis = kanji_list
            )
        }
        KanjiScreenState.KANJI -> {
            LaunchedEffect(Unit) {
                kanji_list = getCategoryArray(path)
                print(kanji_list)
            }

            BackHandler() {
                state = KanjiScreenState.LIST
            }
            KanjiInfoScreen(selected_kanji)

        }
    }

}