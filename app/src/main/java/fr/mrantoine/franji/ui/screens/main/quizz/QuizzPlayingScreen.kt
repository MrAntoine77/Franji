package fr.mrantoine.franji.ui.screens.main.quizz

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
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
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.CardTypeTag
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.screens.main.quizz.grammar.QuizzGrammarFr3Jp
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzGrammarJp4Fr
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiFr4Kana
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiFr9Jp
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiJp4Fr
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzKanjiJp4Kana
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzVocabFr4Jp
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzVocabFr4Kana
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzVocabJp4Fr
import fr.mrantoine.franji.ui.screens.main.quizz.kanji.QuizzVocabJp4Kana
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections.emptyMap


enum class QuizzMode {
    KANJI_Fr9Jp,
    KANJI_Fr4Kana,
    KANJI_Jp4Fr,
    KANJI_Jp4Kana,
    VOCAB_Fr4Jp,
    VOCAB_Fr4Kana,
    VOCAB_Jp4Fr,
    VOCAB_Jp4Kana,
    GRAMMAR_Fr3Jp,
    GRAMMAR_Jp4Fr
}

data class QuizzElement(
    val id: String,
    val mode: QuizzMode,
    val failed: Boolean = false
)


fun buildQuizz(
    context: Context,
    listId: List<String>
): List<QuizzElement> {
    val result = mutableListOf<QuizzElement>()

    listId.forEach { id ->
        if (id.startsWith("kanji")) {
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Fr9Jp))
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Jp4Fr))
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Fr4Kana))
            result.add(QuizzElement(id = id, mode = QuizzMode.KANJI_Jp4Kana))
        }
        if (id.startsWith("vocab")) {
            val vocab = VocabStorage.getVocabById(context, id)
            result.add(QuizzElement(id = id, mode = QuizzMode.VOCAB_Fr4Jp))
            result.add(QuizzElement(id = id, mode = QuizzMode.VOCAB_Jp4Fr))
            if(vocab.lecture.kana != vocab.jp) {
                result.add(QuizzElement(id = id, mode = QuizzMode.VOCAB_Fr4Kana))
                result.add(QuizzElement(id = id, mode = QuizzMode.VOCAB_Jp4Kana))
            }
        }
        if (id.startsWith("grammar")) {
            result.add(QuizzElement(id = id, mode = QuizzMode.GRAMMAR_Fr3Jp))
            result.add(QuizzElement(id = id, mode = QuizzMode.GRAMMAR_Jp4Fr))
        }
    }
    return result.shuffled()
}


@Composable
fun QuizzPlayingScreen(
    navController: NavController,
    categoryPath: String
) {
    val context = LocalContext.current

    var backPressedOnce by remember { mutableStateOf(false) }

    if (backPressedOnce) {
        LaunchedEffect(Unit) {
            delay(2000)
            backPressedOnce = false
        }
    }

    BackHandler {
        if (backPressedOnce) {
            navController.navigate(Screen.QuizzWorldList.route)
        } else {
            backPressedOnce = true
            Toast.makeText(
                context,
                "Appuyez encore pour quitter. Les données non sauvegardées seront perdues.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = categoryPath.uppercase().replace("/", " "),
                showBack = true,
                showMenu = false,
                onBackClick = { backDispatcher?.onBackPressed() },
                navController = navController
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


        val checkList = remember { mutableMapOf<String, Boolean>() }

        fun onNext() {
            loading = true
            index += 1

            val current = quizzList.first()

            quizzList = if (passed.value) {
                if(!current.failed) {
                    if(!checkList.containsKey(itemId)) {
                        checkList[itemId] = true
                    }
                }
                quizzList.drop(1)
            } else {
                checkList[itemId] = false
                if (current.failed) {
                    quizzList.drop(1)
                } else {
                    val updated = current.copy(failed = true)
                    quizzList.drop(1) + updated
                }
            }

            passed.value = true
        }

        LaunchedEffect(index) {
            if(index == 0) {
                itemIdList = CategoryStorage.getCategoryByPath(context, categoryPath).toList().shuffled()
                quizzList = buildQuizz(context= context, listId = itemIdList)
            }
            if(quizzList.isNotEmpty()) {
                itemId = quizzList[0].id
                mode = quizzList[0].mode
                loading = false
            }
            else {
                checkList.forEach { (id, done) ->
                    if(done) {
                        CategoryStorage.updateCard(context, categoryPath, id)
                    }
                    else {
                        CategoryStorage.resetCard(context, categoryPath, id)
                    }
                }
                navController.navigate(Screen.QuizzWorldList.route)
            }
        }
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = Dimens.s)
        ) {
            Text(text=quizzList.size.toString())
            CardTypeTag(
                text = if(itemId.startsWith("kanji")) "Kanji" else if(itemId.startsWith("vocab")) "Vocabulaire" else "Grammaire",
                color = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
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
                        QuizzMode.VOCAB_Fr4Jp -> {
                            QuizzVocabFr4Jp(
                                vocabId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                        QuizzMode.VOCAB_Fr4Kana -> {
                            QuizzVocabFr4Kana(
                                vocabId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                        QuizzMode.VOCAB_Jp4Fr -> {
                            QuizzVocabJp4Fr(
                                vocabId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                        QuizzMode.VOCAB_Jp4Kana -> {
                            QuizzVocabJp4Kana(
                                vocabId = itemId,
                                categoryPath = categoryPath,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                        QuizzMode.GRAMMAR_Fr3Jp -> {
                            QuizzGrammarFr3Jp(
                                grammarId = itemId,
                                onNext = { onNext() },
                                passed = passed
                            )
                        }
                        QuizzMode.GRAMMAR_Jp4Fr -> {
                            QuizzGrammarJp4Fr(
                                grammarId = itemId,
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