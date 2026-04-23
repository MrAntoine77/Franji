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
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.VocabGrid4Fr
import fr.mrantoine.franji.ui.components.VocabGrid4Jp
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun QuizzVocabJp4Fr(
    vocabId: String,
    categoryPath: String,
    onNext: () -> Unit = {},
    passed: MutableState<Boolean>
) {
    var guess_list by remember { mutableStateOf<List<Vocab>>(emptyList()) }
    var vocab by remember { mutableStateOf(Vocab()) }
    var selectedIds by remember { mutableStateOf<List<String>>(emptyList()) }
    val isRevealed = selectedIds.contains(vocabId)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        guess_list = VocabStorage.get4LinkedVocab(context, vocabId, categoryPath)
        vocab = VocabStorage.getVocabById(context, vocabId)
    }

    Column(
        modifier = Modifier
            .padding(Dimens.m)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Dimens.xxxl))

        Text(
            text = vocab.jp,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if(isRevealed) {
            val reveal_kana = if(SettingsStorage.isRomaji()) vocab.lecture.kana.replaceFirstChar { it.uppercase() } else vocab.lecture.kana.replaceFirstChar { it.uppercase() }

            ClickableAnimatedText(
                text = reveal_kana,
                fontSize = 24.sp,
                onClick = { TtsStorage.speak(vocab.lecture.kana) },
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = vocab.fr,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            ThemedButton(
                text = "Suivant",
                onClick = {
                    onNext()
                    selectedIds = emptyList()
                }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        else {
            Spacer(modifier = Modifier.weight(1f))
            VocabGrid4Fr(
                items = guess_list,
                selectedIds = selectedIds,
                vocabId = vocabId,
                onClick = { vocab ->
                    selectedIds = selectedIds + vocab.id
                    if(vocab.id == vocabId) {
                        TtsStorage.speak(vocab.lecture.kana)
                    } else {
                        passed.value = false
                    }
                }
            )
        }
    }
}