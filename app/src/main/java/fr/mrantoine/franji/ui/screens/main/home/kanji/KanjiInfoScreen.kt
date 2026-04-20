package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.LottieStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabState
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun KanjiInfoScreen(
    navController: NavController,
    modifier: Modifier,
    kanjiId: String
) {
    val scrollState = rememberLazyListState()
    var kanji by remember { mutableStateOf(Kanji()) }
    var lottie by remember { mutableStateOf("{}") }
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        kanji = KanjiStorage.getKanjiById(context, kanjiId)
        kanji.let {
            lottie = LottieStorage.getLottieById(context,it.id)
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(Dimens.m)
    ) {

        item {
            Lottie(
                data = lottie,
                speed = 2f
            )
        }
        item {
            Lecture(kanji.lectures)
        }

        if(kanji.vocab.size > 0)
        {
            item {
                Text(
                    text = "Utilisations :",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimens.m),
                    textAlign = TextAlign.Start
                )
            }
            kanji.vocab.forEach { vocabId ->
                item {
                    var vocab by remember { mutableStateOf(Vocab()) }
                    val context = LocalContext.current
                    LaunchedEffect(Unit) {
                        vocab = VocabStorage.getVocabById(context, vocabId)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Screen.VocabList.route(
                                    state = VocabState.Info,
                                    categoryPath = "",
                                    vocabId = vocabId
                                ))
                            }
                            .padding(vertical = Dimens.s, horizontal = Dimens.m),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        HighlightedText(
                            text = vocab.jp.replace(kanji.kanji, "{${kanji.kanji}}"),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = vocab.fr,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}