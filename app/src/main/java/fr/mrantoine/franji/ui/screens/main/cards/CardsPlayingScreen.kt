package fr.mrantoine.franji.ui.screens.main.cards


import android.speech.tts.TextToSpeech
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.ui.components.navigation.EaseBar
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.screens.main.cards.types.grammar.CardGrammarThemeScreen
import fr.mrantoine.franji.ui.screens.main.cards.types.grammar.CardGrammarVersionScreen
import fr.mrantoine.franji.ui.screens.main.cards.types.kanji.CardKanjiThemeScreen
import fr.mrantoine.franji.ui.screens.main.cards.types.kanji.CardKanjiVersionScreen
import fr.mrantoine.franji.ui.screens.main.cards.types.vocab.CardVocabThemeScreen
import fr.mrantoine.franji.ui.screens.main.cards.types.vocab.CardVocabVersionScreen
import fr.mrantoine.franji.ui.theme.Dimens
import java.util.Locale
import kotlin.io.print


enum class Mode {
    THEME,
    VERSION,
    BOTH
}

fun buildcardList(idList: List<String>, mode: Mode): MutableList<Pair<String, Mode>> {
    val cardList = mutableListOf<Pair<String, Mode>>()
    idList.forEach { id ->
        when(mode) {
            Mode.THEME -> {
                cardList.add(id to Mode.THEME)
            }
            Mode.VERSION -> {
                cardList.add(id to Mode.VERSION)
            }
            Mode.BOTH -> {
                cardList.add(id to Mode.THEME)
                cardList.add(id to Mode.VERSION)
            }
        }
    }
    cardList.shuffle()
    return cardList
}

@Composable
fun CardsPlayingScreen(
    navController: NavController,
    idList: MutableList<String>,
    title: String,
    mode: Mode
) {
    if(idList.size > 0) {
        val isRevealed = remember { mutableStateOf(false) }
        var passed by remember { mutableStateOf(0) }
        var failed by remember { mutableStateOf(0) }
        val cardslist = remember { buildcardList(idList, mode) }
        var total by remember { mutableStateOf( cardslist.size) }


        fun nextCard(ok: Boolean) {
            if (ok && cardslist.size == 1) {
                navController.navigate(Screen.CardsList.route)
            } else {
                isRevealed.value = false
                val first = cardslist.removeAt(0)
                if(ok) {
                    passed+=1
                }
                else {
                    if((passed + failed) < total) {
                        failed += 1
                    }
                    cardslist.add(first)
                }
            }
        }

        val difficulties: Map<String, Pair<Color, () -> Unit>> = if (isRevealed.value) mapOf(
            "Revoir" to (Color(0xFFF44336) to { nextCard(false) }),
            "OK" to (Color(0xFF4CAF50) to { nextCard(true) })

        ) else mapOf(
            "Révéler" to (MaterialTheme.colorScheme.primary to { isRevealed.value = true })
        )

        Scaffold(
            topBar = {
                val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                TopBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = title,
                    showBack = true,
                    onBackClick = {backDispatcher?.onBackPressed()}
                )
            },
            bottomBar = {
                EaseBar(
                    modifier = Modifier.navigationBarsPadding(),
                    items = difficulties
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimens.s),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(passed.toString(),
                        modifier = Modifier.padding(end = Dimens.s),
                        color = Color.Green
                    )
                    Text(failed.toString(),
                        modifier = Modifier.padding(end = Dimens.s),
                        color = Color.Red
                    )
                    Text(cardslist.size.toString(),
                        modifier = Modifier.padding(end = Dimens.s),
                        color = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.s))


                if(cardslist[0].first.startsWith("kanji")) {
                    if(cardslist[0].second == Mode.THEME) {
                        CardKanjiThemeScreen(
                            cardId = cardslist[0].first,
                            isRevealed = isRevealed
                        )
                    }
                    else {
                        CardKanjiVersionScreen(
                            cardId = cardslist[0].first,
                            isRevealed = isRevealed
                        )
                    }

                } else if(cardslist[0].first.startsWith("vocab")) {
                    if(cardslist[0].second == Mode.THEME) {
                        CardVocabThemeScreen(
                            cardId = cardslist[0].first,
                            isRevealed = isRevealed
                        )
                    }
                    else {
                        CardVocabVersionScreen(
                            cardId = cardslist[0].first,
                            isRevealed = isRevealed
                        )
                    }
                } else if(cardslist[0].first.startsWith("grammar")) {
                    if(cardslist[0].second == Mode.THEME) {
                        CardGrammarThemeScreen(
                            cardId = cardslist[0].first,
                            isRevealed = isRevealed
                        )
                    }
                    else {
                        CardGrammarVersionScreen(
                            cardId = cardslist[0].first,
                            isRevealed = isRevealed
                        )
                    }
                }
            }
        }
    }
}