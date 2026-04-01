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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.Tree
import fr.mrantoine.franji.ui.components.TreeLeaf
import fr.mrantoine.franji.ui.components.TreeNode
import fr.mrantoine.franji.ui.screens.main.cards.CardsScreen
import fr.mrantoine.franji.ui.theme.Dimens

enum class KanjiScreenState {
    CATEGORY,
    LIST,
    KANJI
}
@Composable
fun KanjiScreen() {
    val state = remember { mutableStateOf(KanjiScreenState.CATEGORY) }

    when (state.value) {
        KanjiScreenState.CATEGORY ->  {
            val treeData = listOf(
                "Niveaux" to listOf("JLPT5", "JLPT4", "JLPT3"),
                "Catégories" to listOf("Nature", "Famille", "Travail", "Véhicules", "Compteurs"),
                "Kana" to listOf("Hiragana", "Katakana"),

                )
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Tree(
                    nodes = treeData,
                    onClick = {state.value = KanjiScreenState.LIST}
                )
            }
        }
        KanjiScreenState.LIST -> {
            BackHandler() {
                state.value = KanjiScreenState.CATEGORY
            }
            KanjiListScreen(
                onKanjiClick = {state.value = KanjiScreenState.KANJI}
            )
        }
        KanjiScreenState.KANJI -> {
            BackHandler() {
                state.value = KanjiScreenState.LIST
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