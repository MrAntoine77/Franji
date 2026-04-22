package fr.mrantoine.franji.ui.screens.main.quizz

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.LottieStorage
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.theme.Dimens


@Composable
fun KanjiGrid3x3(
    items: List<Kanji>,
    onClick: (Kanji) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    kanjiId: String
) {
    val safeItems = (items + List(9 - items.size.coerceAtLeast(0)) { Kanji("") }).take(9)
    val canSelect =  !selectedIds.contains(kanjiId)


    Column {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    val kanji = safeItems[index]


                    val boxColor = if (selectedIds.contains(kanji.id)) MaterialTheme.colorScheme.tertiary else Color.Transparent

                    val textColor = if (selectedIds.contains(kanji.id)) MaterialTheme.colorScheme.secondary else Color.Unspecified


                    Card(
                        modifier = Modifier
                            .size(110.dp)
                            .padding(6.dp)
                            .clickable(enabled = canSelect && !selectedIds.contains(kanji.id)) {
                                if(!selectedIds.contains(kanji.id)) {
                                    onClick(kanji)
                                }
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize().background(boxColor)
                        ) {
                            Text(
                                text = kanji.kanji,
                                fontSize = 32.sp,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizzPlayingScreen(
    navController: NavController,
) {

    var kanjiList by remember { mutableStateOf<List<String>>(emptyList()) }
    var kanjiId by remember { mutableStateOf("") }
    var kanji by remember { mutableStateOf<Kanji>(Kanji()) }
    var index by remember { mutableStateOf(0) }


    var guess_list by remember { mutableStateOf<List<Kanji>>(emptyList()) }
    var lottie by remember { mutableStateOf("{}") }
    var selectedIds by remember { mutableStateOf<List<String>>(emptyList()) }

    var loading by remember { mutableStateOf(true) }

    val categoryPath = "Kanji/Monde5/Niveau3"

    val context = LocalContext.current

    LaunchedEffect(index) {
        if(index == 0) {
            kanjiList = CategoryStorage.getCategoryByPath(context, categoryPath).toList().shuffled()
        }
        kanjiId = kanjiList[index]
        kanji = KanjiStorage.getKanjiById(context, kanjiId)
        guess_list = KanjiStorage.getLinkedKanji(context, kanjiId, categoryPath)
        loading = false
        lottie = LottieStorage.getLottieById(context, kanjiId)
    }

    Scaffold(
        topBar = {
            val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = "Test Quizz",
                showBack = true,
                onBackClick = { backDispatcher?.onBackPressed() }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(Dimens.m)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(!loading) {
                Spacer(modifier = Modifier.height(Dimens.xxxl))

                Text(
                    text = kanji.main_lecture.fr.replaceFirstChar { it.uppercase() },
                    fontSize = 32.sp
                )



                Spacer(modifier = Modifier.weight(1f))
                if(selectedIds.contains(kanjiId)) {
                    val reveal_kana = if(SettingsStorage.isRomaji()) kanji.main_lecture.kana.replaceFirstChar { it.uppercase() } else kanji.main_lecture.kana.replaceFirstChar { it.uppercase() }
                    Lottie(
                        data = lottie,
                        speed = 2f
                    )
                    ClickableAnimatedText(
                        text = reveal_kana,
                        fontSize = 28.sp,
                        onClick = { TtsStorage.speak(kanji.main_lecture.kana) }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ThemedButton(
                        text = "Suivant",
                        onClick = {
                            index = (index + 1) % kanjiList.size
                            selectedIds = emptyList()
                            loading = true
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                else {
                    KanjiGrid3x3(
                        items = guess_list,
                        selectedIds = selectedIds,
                        kanjiId = kanjiId,
                        onClick = { kanji ->
                            selectedIds = selectedIds + kanji.id
                            if(kanji.id == kanjiId) {
                                TtsStorage.speak(kanji.main_lecture.kana)
                            }
                        }
                    )
                }
            }
        }
    }
}