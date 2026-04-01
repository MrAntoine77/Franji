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
import fr.mrantoine.franji.ui.components.TreeLeaf
import fr.mrantoine.franji.ui.components.TreeNode

@Composable
fun CardsScreen(
    onCardClick: (() -> Unit) = {}
) {
    val treeData = listOf(
        "Par types" to listOf("Kanji", "Kana", "Vocabulaire", "Grammaire"),
        "Catégories" to listOf("Nature", "Famille", "Travail", "Véhicules", "Compteurs"),
        "Personnalisées" to listOf("Personnalisé1", "Personnalisé2"),

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
            nodes = treeData,
            onClick = onCardClick
        )
    }
}