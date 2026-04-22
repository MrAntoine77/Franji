package fr.mrantoine.franji.ui.screens.main.quizz.kanji

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.LottieStorage
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.KanjiGrid4Fr
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun QuizzKanjiJp4Kana(
    kanjiId: String,
    categoryPath: String,
    onNext: () -> Unit = {},
    passed: MutableState<Boolean>

) {
    var lottie by remember { mutableStateOf("{}") }
    var guess_list by remember { mutableStateOf<List<Kanji>>(emptyList()) }
    var kanji by remember { mutableStateOf(Kanji()) }
    var selectedIds by remember { mutableStateOf<List<String>>(emptyList()) }
    val isRevealed = selectedIds.contains(kanjiId)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        guess_list = KanjiStorage.get4LinkedKanji(context, kanjiId, categoryPath)
        lottie = LottieStorage.getLottieById(context, kanjiId)
        kanji = KanjiStorage.getKanjiById(context, kanjiId)
    }

    Column(
        modifier = Modifier
            .padding(Dimens.m)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Dimens.xxxl))
        Lottie(
            data = lottie,
            speed = 2f
        )
        Spacer(modifier = Modifier.weight(1f))
        if(isRevealed) {
            val reveal_kana = if(SettingsStorage.isRomaji()) kanji.main_lecture.kana.replaceFirstChar { it.uppercase() } else kanji.main_lecture.kana.replaceFirstChar { it.uppercase() }

            ClickableAnimatedText(
                text = reveal_kana,
                fontSize = 28.sp,
                onClick = { TtsStorage.speak(kanji.main_lecture.kana) }
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
            KanjiGrid4Fr(
                items = guess_list,
                selectedIds = selectedIds,
                kanjiId = kanjiId,
                onClick = { kanji ->
                    selectedIds = selectedIds + kanji.id
                    if(kanji.id == kanjiId) {
                        TtsStorage.speak(kanji.main_lecture.kana)
                    } else {
                        passed.value = false
                    }
                }
            )
        }
    }
}