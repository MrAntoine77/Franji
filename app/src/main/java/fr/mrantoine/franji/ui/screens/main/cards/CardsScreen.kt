package fr.mrantoine.franji.ui.screens.main.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.mrantoine.franji.ui.components.Tree


@Composable
fun CardsScreen(
    onCardClick: (() -> Unit) = {}
) {
    val data = mapOf(
        "Par types" to mapOf(
            "Kanji" to mapOf(
                "JLPT 5" to mapOf(
                    "JLPT 5 1-20" to listOf("kanji1", "kanji2"),
                    "JLPT 5 21-40" to listOf("kanji3", "kanji4")
                ),
                "JLPT 4" to mapOf(
                    "JLPT 4 41-60" to listOf("kanji5", "kanji6"),
                    "JLPT 4 61-80" to listOf("kanji7", "kanji8")
                )
            ),
            "Kana" to mapOf(
                "Hiragana" to listOf("a", "i" ,"u"),
                "Katakana" to listOf("a", "i" ,"u")
            ),
            "Vocabulaire" to mapOf(
                "JLPT 5" to mapOf(
                    "JLPT 5 1-20" to listOf("kanji1", "kanji2"),
                    "JLPT 5 21-40" to listOf("kanji3", "kanji4")
                ),
                "JLPT 4" to mapOf(
                    "JLPT 4 41-60" to listOf("kanji5", "kanji6"),
                    "JLPT 4 61-80" to listOf("kanji7", "kanji8")
                )
            ),
            "Grammaire" to mapOf(
                "JLPT 5" to mapOf(
                    "JLPT 5 1-20" to listOf("kanji1", "kanji2"),
                    "JLPT 5 21-40" to listOf("kanji3", "kanji4")
                ),
                "JLPT 4" to mapOf(
                    "JLPT 4 41-60" to listOf("kanji5", "kanji6"),
                    "JLPT 4 61-80" to listOf("kanji7", "kanji8")
                )
            ),

        ),
        "Catégories" to mapOf(
            "Nature" to listOf(""),
            "Famille" to listOf(""),
            "Travail" to listOf(""),
            "Véhicules" to listOf(""),
            "Compteurs" to listOf("")
        ),
        "Personnalisées" to mapOf(
            "Perso 1" to listOf("a", "i" ,"u"),
            "Perso 2" to listOf("a", "i" ,"u"),
            "+" to emptyList()
        )
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        HorizontalDivider(
            color = Color.Gray,
            thickness = 2.dp
        )
        Tree(
            treeData = data,
            onClick = { onCardClick() }
        )
    }
}