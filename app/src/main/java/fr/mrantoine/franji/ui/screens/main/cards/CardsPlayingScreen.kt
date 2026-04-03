package fr.mrantoine.franji.ui.screens.main.cards


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.navigation.EaseBar
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.theme.Dimens


@Composable
fun CardsPlayingScreen(
    navController: NavController,
    cardsArray: MutableList<String>,
    title: String
) {
    if(cardsArray.size > 0) {
        val isRevealed = remember { mutableStateOf(false) }
        var lottie by remember { mutableStateOf<String>("{}") }
        var kanji by remember { mutableStateOf(Kanji()) }
        var total by remember { mutableStateOf( cardsArray.size) }


        var passed by remember { mutableStateOf(0) }
        var failed by remember { mutableStateOf(0) }

        LaunchedEffect(isRevealed.value == false) {
            lottie = KanjiStorage.getLottieByKanjiId(cardsArray[0])
        }
        LaunchedEffect(isRevealed.value == false) {
            kanji = KanjiStorage.getKanjiById(cardsArray[0])
        }

        fun nextCard(ok: Boolean) {
            isRevealed.value = false
            if (ok && cardsArray.size == 1) {
                navController.navigate(Screen.CardsList.route)
            } else {

                val first = cardsArray.removeAt(0)
                if(ok) {
                    passed+=1
                }
                else {
                    if((passed + failed) < total) {
                        failed += 1
                    }
                    cardsArray.add(first)
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
                TopBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = title,
                    showBack = true
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
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
                    Text(cardsArray.size.toString(),
                        modifier = Modifier.padding(end = Dimens.s),
                        color = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.s))

                val hint = kanji.lectures.firstOrNull()?.let { lecture ->
                    lecture.fr.firstOrNull()?.takeIf { it.isNotEmpty() } ?: ""
                } ?: ""

                val scrollState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = scrollState,
                ) {
                    item {
                        Text(
                            text = hint,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        HorizontalDivider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if(isRevealed.value) {
                        item {
                            val translation = kanji.lectures.firstOrNull()?.let { lecture ->
                                lecture.kun.firstOrNull()?.takeIf { it.isNotEmpty() }
                                    ?: lecture.ON.firstOrNull()?.takeIf { it.isNotEmpty() }
                                    ?: ""
                            } ?: ""

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = translation,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = Dimens.l),
                                    textAlign = TextAlign.Center
                                )
                                Lottie(
                                    data = lottie
                                )
                            }
                        }
                        items(kanji.lectures.size) { index ->
                            Lecture(
                                french = kanji.lectures[index].fr,
                                ON = kanji.lectures[index].ON,
                                kun = kanji.lectures[index].kun
                            )
                        }
                    }
                }
            }
        }
    }
}