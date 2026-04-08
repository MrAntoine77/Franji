package fr.mrantoine.franji.ui.screens.main.cards.types.grammar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarItem
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.ui.components.CardTypeTag
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.theme.Dimens
import org.w3c.dom.Text

@Composable
fun CardGrammarVersionScreen(
    cardId: String,
    isRevealed: MutableState<Boolean>,
) {
    var grammar by remember { mutableStateOf(Grammar()) }
    var grammarExample by remember { mutableStateOf(GrammarItem()) }

    LaunchedEffect(cardId) {
        grammar = GrammarStorage.getGrammarById(cardId)
        grammarExample = grammar.examples.random()
    }

    val hint = grammarExample.jp

    CardTypeTag(
        text = "Grammaire",
        color = MaterialTheme.colorScheme.primary,
        textColor = Color.White
    )
    Spacer(modifier = Modifier.height(Dimens.s))
    HighlightedText(
        text = hint,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    HorizontalDivider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    )

    val scrollState = rememberLazyListState()
    if(isRevealed.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollState,
        ) {

            item {
                val translation = grammarExample.fr
                val lecture = grammarExample.lecture
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = translation,
                        fontSize = 32.sp,
                        lineHeight = 40.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Center
                    )
                    HighlightedText(
                        text = if(SettingsStorage.isRomaji()) lecture.romaji else lecture.kana,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

