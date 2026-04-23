package fr.mrantoine.franji.ui.screens.main.quizz.kanji

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarItem
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.GrammarGrid4Fr
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.components.VocabGrid4Fr
import fr.mrantoine.franji.ui.components.VocabGrid4Jp
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun QuizzGrammarJp4Fr(
    grammarId: String,
    onNext: () -> Unit = {},
    passed: MutableState<Boolean>
) {
    var guess_list by remember { mutableStateOf<List<Grammar>>(emptyList()) }
    var grammar by remember { mutableStateOf(Grammar()) }
    var grammarItem by remember { mutableStateOf(GrammarItem()) }
    var selectedIds by remember { mutableStateOf<List<String>>(emptyList()) }
    val isRevealed = selectedIds.contains(grammarId)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        guess_list = GrammarStorage.get4RandomGrammarElement(grammarId)
        grammar = GrammarStorage.getGrammarById(context, grammarId)
        grammarItem = grammar.examples.random()
    }

    Column(
        modifier = Modifier
            .padding(Dimens.m)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Dimens.xl))

        ClickableAnimatedText(
            text = grammarItem.jp,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                TtsStorage.speak(grammarItem.lecture.kana)
            }
        )
        if(isRevealed) {
            val reveal_kana = if(SettingsStorage.isRomaji()) grammarItem.lecture.kana.replaceFirstChar { it.uppercase() } else grammarItem.lecture.kana.replaceFirstChar { it.uppercase() }

            ClickableAnimatedText(
                text = reveal_kana,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                onClick = { TtsStorage.speak(grammarItem.lecture.kana) },
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = grammarItem.fr,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.xl))
            Text(
                text = grammar.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 28.sp
            )
            Text(
                text = grammar.subtitle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(Dimens.m))
            Text(
                text = grammar.desc,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            ThemedButton(
                text = "Suivant",
                onClick = {
                    onNext()
                    selectedIds = emptyList()
                }
            )
        }
        else {
            Spacer(modifier = Modifier.weight(1f))
            GrammarGrid4Fr(
                items = guess_list,
                selectedIds = selectedIds,
                grammarId = grammarId,
                onClick = { grammar ->
                    selectedIds = selectedIds + grammar.id
                    if(grammar.id == grammarId) {
                        TtsStorage.speak(grammarItem.lecture.kana)
                    } else {
                        passed.value = false
                    }
                }
            )
        }
    }
}