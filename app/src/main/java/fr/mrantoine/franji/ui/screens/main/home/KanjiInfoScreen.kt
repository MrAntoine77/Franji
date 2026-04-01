package fr.mrantoine.franji.ui.screens.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Tree

@Composable
fun KanjiInfoScreen() {
    Column() {
        Lecture(
            french = listOf("lumière", "clarté"),
            on = listOf("MEI"),
            kun = listOf("a(kari)",)
        )
        Lecture(
            french = listOf("lumière", "clarté"),
            on = listOf("MEI"),
            kun = listOf("a(kari)")
        )
        Lecture(
            french = listOf("lumière", "clarté"),
            on = listOf("MEI"),
            kun = listOf("a(kari)")
        )
        Lecture(
            french = listOf("lumière", "clarté"),
            on = listOf("MEI"),
            kun = listOf("a(kari)")
        )
        Lecture(
            french = listOf("lumière", "clarté"),
            on = listOf("MEI"),
            kun = listOf("a(kari)")
        )
    }
}