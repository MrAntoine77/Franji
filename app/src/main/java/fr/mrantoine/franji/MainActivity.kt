package fr.mrantoine.franji

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import fr.mrantoine.franji.ui.screens.auth.SplashScreen
import fr.mrantoine.franji.ui.theme.FranjiTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.mrantoine.franji.ui.screens.main.cards.CardsListScreen
import fr.mrantoine.franji.ui.screens.main.cards.CardsPlayingScreen
import fr.mrantoine.franji.ui.screens.main.cards.CardsRecapScreen
import fr.mrantoine.franji.ui.screens.main.cards.Mode
import fr.mrantoine.franji.ui.screens.main.home.grammar.GrammarScreen
import fr.mrantoine.franji.ui.screens.main.home.kana.KanaScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiState
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabScreen
import fr.mrantoine.franji.ui.screens.main.home.vocab.VocabState
import fr.mrantoine.franji.ui.screens.main.quizz.QuizzListScreen
import fr.mrantoine.franji.ui.screens.main.settings.SettingsScreen


sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Settings : Screen("settings")



    data object KanjiList : Screen("kanji_list/{state}/{categoryPath}/{kanjiId}") {
        const val ARG_STATE = "state"
        const val ARG_CATEGORY_PATH = "categoryPath"
        const val ARG_KANJI_ID = "kanjiId"

        fun route(state: KanjiState, categoryPath: String, kanjiId: String): String {
            val encodedState = Uri.encode(state.toString())
            val encodedCategoryPath = Uri.encode(categoryPath)
            val encodedKanjiId = Uri.encode(kanjiId)
            return "kanji_list/$encodedState/$encodedCategoryPath/$encodedKanjiId"
        }
    }




    data object KanaList : Screen("kana_list")
    data object VocabList : Screen("vocab_list/{state}/{categoryPath}/{vocabId}") {
        const val ARG_STATE = "state"
        const val ARG_CATEGORY_PATH = "categoryPath"
        const val ARG_VOCAB_ID = "vocabId"

        fun route(state: VocabState, categoryPath: String, vocabId: String): String {
            val encodedState = Uri.encode(state.toString())
            val encodedCategoryPath = Uri.encode(categoryPath)
            val encodedVocabId = Uri.encode(vocabId)
            return "vocab_list/$encodedState/$encodedCategoryPath/$encodedVocabId"
        }
    }
    data object GrammarList : Screen("grammar_list")



    data object CardsList : Screen("cards_list")
    data object CardsRecap : Screen("cards_recap/{cardsPath}") {
        const val ARG = "cardsPath"
        fun route(cardsPath: String) = "cards_recap/${Uri.encode(cardsPath)}"
    }
    data object CardsPlaying : Screen("cards_playing/{idArray}/{title}/{mode}") {
        const val ARG_CARDS = "idArray"
        const val ARG_TITLE = "title"
        const val ARG_MODE = "mode"

        fun route(idArray: Array<String>, title: String, mode: Mode): String {
            val encodedCards = Uri.encode(idArray.joinToString(","))
            val encodedTitle = Uri.encode(title)
            val encodedMode = Uri.encode(mode.toString())
            return "cards_playing/$encodedCards/$encodedTitle/$encodedMode"
        }
    }


    data object QuizzList : Screen("quizz_list")

}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkTheme = isSystemInDarkTheme()
            val systemUiController = rememberSystemUiController()

            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = !darkTheme
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = !darkTheme
                )
            }




            FranjiTheme(dynamicColor = false) {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    enterTransition = { fadeIn(animationSpec = tween(0)) },
                    exitTransition = { fadeOut(animationSpec = tween(0)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(0)) },
                    popExitTransition = { fadeOut(animationSpec = tween(0)) }
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navController)
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(navController)
                    }

                    composable(
                        route = Screen.KanjiList.route,
                        arguments = listOf(
                            navArgument(Screen.KanjiList.ARG_CATEGORY_PATH) { type = NavType.StringType },
                            navArgument(Screen.KanjiList.ARG_KANJI_ID) { type = NavType.StringType },
                            navArgument(Screen.KanjiList.ARG_STATE) { type = NavType.StringType }
                        )
                    ) { backStackEntry ->

                        val categoryPath = backStackEntry.arguments
                            ?.getString(Screen.KanjiList.ARG_CATEGORY_PATH)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val kanjiId = backStackEntry.arguments
                            ?.getString(Screen.KanjiList.ARG_KANJI_ID)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val state: KanjiState = backStackEntry.arguments
                            ?.getString(Screen.KanjiList.ARG_STATE)
                            ?.let { Uri.decode(it) }
                            ?.let { KanjiState.valueOf(it) }
                            ?: KanjiState.CategoryLsit

                        KanjiScreen(
                            navController = navController,
                            categoryPath = categoryPath,
                            state = state,
                            kanjiId = kanjiId
                        )
                    }

                    composable(Screen.KanaList.route) {
                        KanaScreen(navController)
                    }
                    composable(
                        route = Screen.VocabList.route,
                        arguments = listOf(
                            navArgument(Screen.VocabList.ARG_CATEGORY_PATH) { type = NavType.StringType },
                            navArgument(Screen.VocabList.ARG_VOCAB_ID) { type = NavType.StringType },
                            navArgument(Screen.VocabList.ARG_STATE) { type = NavType.StringType }
                        )
                    ) { backStackEntry ->

                        val categoryPath = backStackEntry.arguments
                            ?.getString(Screen.VocabList.ARG_CATEGORY_PATH)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val vocabId = backStackEntry.arguments
                            ?.getString(Screen.VocabList.ARG_VOCAB_ID)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val state: VocabState = backStackEntry.arguments
                            ?.getString(Screen.VocabList.ARG_STATE)
                            ?.let { Uri.decode(it) }
                            ?.let { VocabState.valueOf(it) }
                            ?: VocabState.CategoryLsit

                        VocabScreen(
                            navController = navController,
                            categoryPath = categoryPath,
                            state = state,
                            vocabId = vocabId
                        )
                    }
                    composable(Screen.GrammarList.route) {
                        GrammarScreen(navController)
                    }

                    composable(Screen.CardsList.route) {
                        CardsListScreen(navController)
                    }

                    composable(
                        route = Screen.CardsRecap.route,
                        arguments = listOf(navArgument(Screen.CardsRecap.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val cardsPath = backStackEntry.arguments
                            ?.getString(Screen.CardsRecap.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        CardsRecapScreen(navController, cardsPath)
                    }

                    composable(
                        route = Screen.CardsPlaying.route,
                        arguments = listOf(
                            navArgument(Screen.CardsPlaying.ARG_CARDS) { type = NavType.StringType },
                            navArgument(Screen.CardsPlaying.ARG_TITLE) { type = NavType.StringType },
                            navArgument(Screen.CardsPlaying.ARG_MODE) { type = NavType.StringType }
                        )
                    ) { backStackEntry ->

                        val cardsPlayingString = backStackEntry.arguments
                            ?.getString(Screen.CardsPlaying.ARG_CARDS)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val title = backStackEntry.arguments
                            ?.getString(Screen.CardsPlaying.ARG_TITLE)
                            ?.let { Uri.decode(it) }
                            ?: ""

                        val mode: Mode = backStackEntry.arguments
                            ?.getString(Screen.CardsPlaying.ARG_MODE)
                            ?.let { Uri.decode(it) }
                            ?.let { Mode.valueOf(it) }
                            ?: Mode.VERSION

                        val idList: MutableList<String> =
                            if (cardsPlayingString.isEmpty()) mutableListOf()
                            else cardsPlayingString.split(",").toMutableList()

                        CardsPlayingScreen(
                            navController = navController,
                            idList = idList,
                            title = title,
                            mode = mode
                        )
                    }
                    composable(Screen.QuizzList.route) {
                        QuizzListScreen(navController)
                    }
                }
            }
        }
    }
}