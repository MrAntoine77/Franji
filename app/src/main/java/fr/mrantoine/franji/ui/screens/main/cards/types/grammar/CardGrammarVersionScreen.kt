package fr.mrantoine.franji.ui.screens.main.cards.types.grammar

import android.speech.tts.TextToSpeech
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarItem
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.CardTypeTag
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.theme.Dimens
import org.w3c.dom.Text
import java.util.Locale

@Composable
fun CardGrammarVersionScreen(
    cardId: String,
    isRevealed: MutableState<Boolean>
) {
    var grammar by remember { mutableStateOf(Grammar()) }
    var grammarExample by remember { mutableStateOf(GrammarItem()) }
    val scrollState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(cardId) {
        grammar = GrammarStorage.getGrammarById(context, cardId)
        grammarExample = grammar.examples.random()
        scrollState.scrollToItem(0)

    }

    LaunchedEffect( isRevealed.value) {
        if (isRevealed.value) {
            TtsStorage.speak(grammarExample.lecture.kana)
        }
    }

    val hint = grammarExample.jp

    CardTypeTag(
        text = "Grammaire",
        color = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.height(Dimens.s))
    ClickableAnimatedText(
        text = hint,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        onClick = { TtsStorage.speak(grammarExample.lecture.kana) }
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.secondary,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth(),
    )


    if(isRevealed.value) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollState,
        ) {

            item {
                val translation = grammarExample.fr
                val lecture = grammarExample.lecture
                val desc = grammar.desc
                val title = grammar.title
                val subtitle = grammar.subtitle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = Dimens.m)
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
                    ClickableAnimatedText(
                        text = if(SettingsStorage.isRomaji()) lecture.romaji else lecture.kana,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.m),
                        textAlign = TextAlign.Center,
                        onClick = { TtsStorage.speak(grammarExample.lecture.kana) }
                    )
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = subtitle,
                        fontSize = 24.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = desc,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

