package fr.mrantoine.franji.ui.screens.main.home

import Kanji
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.Tree
import fr.mrantoine.franji.ui.theme.Dimens
import getLottie

@Composable
fun KanjiInfoScreen(
    kanji: Kanji
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(Dimens.m)
    ) {
        // header Lottie
        item {
            var lottie by remember { mutableStateOf("{}") }
            LaunchedEffect(Unit) {
                lottie = getLottie(kanji.id)
            }
            Lottie(
                data = lottie
            )
        }

        // items Lecture
        items(kanji.lectures.size) { index ->
            Lecture(
                french = kanji.lectures[index].fr,
                on = kanji.lectures[index].ON,
                kun = kanji.lectures[index].kun
            )
        }
    }
}