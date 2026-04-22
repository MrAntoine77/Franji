package fr.mrantoine.franji.ui.screens.main.quizz

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiFr4Kana
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiFr9Jp
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiJp4Fr
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiJp4Kana
import fr.mrantoine.franji.ui.theme.Dimens


enum class QuizzMode {
    KANJI_Fr9Jp,
    KANJI_Fr4Kana,
    KANJI_Jp4Fr,
    KANJI_Jp4Kana,
}

data class QuizzElement(
    val id: String,
    val mode: QuizzMode
)


fun buildQuizz(listId: List<String>): List<QuizzElement> {
    val result = mutableListOf<QuizzElement>()

    listId.forEach { id ->
        if (id.startsWith("kanji")) {
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Fr9Jp))
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Fr4Kana))
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Jp4Fr))
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Jp4Kana))
        }
    }
    return result.shuffled()
}


@Composable
fun QuizzPlayingScreen(
    navController: NavController,
    categoryPath: String
) {

    Scaffold(
        topBar = {
            val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = categoryPath.uppercase().replace("/", " "),
                showBack = true,
                onBackClick = { backDispatcher?.onBackPressed() }
            )
        }
    ) { innerPadding ->
        var itemIdList by remember { mutableStateOf<List<String>>(emptyList()) }
        var quizzList by remember { mutableStateOf<List<QuizzElement>>(emptyList()) }

        var itemId by remember { mutableStateOf("") }
        var index by remember { mutableStateOf(0) }
        var loading by remember { mutableStateOf(true) }
        val passed = remember { mutableStateOf(true) }
        var mode by remember { mutableStateOf(QuizzMode.KANJI_Fr9Jp) }
        val context = LocalContext.current

        fun onNext() {
            loading = true
            index += 1
            quizzList = if(passed.value) {
                quizzList.drop(1)
            } else {
                quizzList.drop(1) + quizzList.first()
            }
            passed.value = true
        }

        LaunchedEffect(index) {
            if(index == 0) {
                itemIdList = CategoryStorage.getCategoryByPath(context, categoryPath).toList().shuffled()
                quizzList = buildQuizz(listId = itemIdList)
            }
            if(quizzList.isNotEmpty()) {
                itemId = quizzList[0].id
                mode = quizzList[0].mode
                loading = false
            }
            else {
                navController.navigate(Screen.QuizzList.route)
            }
        }
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = Dimens.s)
        ) {
            Text(text=quizzList.size.toString())
            if(quizzList.isNotEmpty() && !loading && itemId != "") {
                key(itemId) {
                    when (mode) {
                        QuizzMode.KANJI_Fr9Jp -> {
                            QuizzKanjiFr9Jp(
                                kanjiId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }

                        QuizzMode.KANJI_Fr4Kana -> {
                            QuizzKanjiFr4Kana(
                                kanjiId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                        QuizzMode.KANJI_Jp4Fr -> {
                            QuizzKanjiJp4Fr(
                                kanjiId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                        QuizzMode.KANJI_Jp4Kana -> {
                            QuizzKanjiJp4Kana(
                                kanjiId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                    }
                }
            }
        }
    }
}