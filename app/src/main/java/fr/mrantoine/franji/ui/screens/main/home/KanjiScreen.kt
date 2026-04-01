package fr.mrantoine.franji.ui.screens.main.home

import Kanji
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.Tree
import getCategories
import getCategoryKanjiChar
import getCategoryKanjiId

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


    var categories by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }
    var kanji_list by remember { mutableStateOf(emptyArray<String>()) }


    LaunchedEffect(Unit) {
        categories = getCategories()
    }

    when (state) {
        KanjiScreenState.CATEGORY ->  {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Tree(
                    treeData = categories,//temp_data,
                    onClick = {arg ->
                        state = KanjiScreenState.LIST
                        path = arg
                    }
                )
            }
        }
        KanjiScreenState.LIST -> {

            LaunchedEffect(Unit) {
                kanji_list = getCategoryKanjiChar(path)
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

            BackHandler() {
                state = KanjiScreenState.LIST
            }
            KanjiInfoScreen(selected_kanji)

        }
    }

}