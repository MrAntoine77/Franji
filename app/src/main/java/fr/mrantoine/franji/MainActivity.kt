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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import fr.mrantoine.franji.ui.screens.auth.SplashScreen
import fr.mrantoine.franji.ui.theme.FranjiTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import fr.mrantoine.franji.ui.screens.auth.*
import fr.mrantoine.franji.ui.screens.main.cards.CardsListScreen
import fr.mrantoine.franji.ui.screens.main.cards.CardsPlayingScreen
import fr.mrantoine.franji.ui.screens.main.cards.CardsRecapScreen
import fr.mrantoine.franji.ui.screens.main.cards.Mode
import fr.mrantoine.franji.ui.screens.main.home.AllScreen
import fr.mrantoine.franji.ui.screens.main.home.kana.KanaCategoryListScreen
import fr.mrantoine.franji.ui.screens.main.home.kana.KanaCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.kana.KanaInfoScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.GrammarCategoryListScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.GrammarCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.GrammarInfoScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiCategoryListScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiInfoScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.SearchKanjiScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.VocabCategoryListScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.VocabCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.kanji.VocabInfoScreen
import fr.mrantoine.franji.ui.screens.main.profile.ProfileScreen
import fr.mrantoine.franji.ui.screens.main.quizz.QuizzListScreen
import fr.mrantoine.franji.ui.screens.main.settings.SettingsScreen


sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object HomeAll : Screen("home_all")
    data object Settings : Screen("settings")

    data object KanjiCategoryList : Screen("kanji_category_list")
    data object KanjiCategory : Screen("kanji_category/{categoryPath}") {
        const val ARG = "categoryPath"
        fun route(categoryPath: String) = "kanji_category/${Uri.encode(categoryPath)}"
    }
    data object KanjiInfo : Screen("kanji_info/{kanjiChar}") {
        const val ARG = "kanjiChar"
        fun route(kanjiChar: String) = "kanji_info/${Uri.encode(kanjiChar)}"
    }
    data object SearchKanji : Screen("search_kanji")




    data object KanaCategoryList : Screen("kana_category_list")
    data object KanaCategory : Screen("kana_category/{categoryPath}") {
        const val ARG = "categoryPath"
        fun route(categoryPath: String) = "kana_category/${Uri.encode(categoryPath)}"
    }
    data object KanaInfo : Screen("kana_info/{kanaId}") {
        const val ARG = "kanaId"
        fun route(kanaId: String) = "kana_info/${Uri.encode(kanaId)}"
    }



    data object VocabCategoryList : Screen("vocab_category_list")
    data object VocabCategory : Screen("vocab_category/{categoryPath}") {
        const val ARG = "categoryPath"
        fun route(categoryPath: String) = "vocab_category/${Uri.encode(categoryPath)}"
    }
    data object VocabInfo : Screen("vocab_info/{vocabId}") {
        const val ARG = "vocabId"
        fun route(vocabId: String) = "vocab_info/${Uri.encode(vocabId)}"
    }

    data object GrammarCategoryList : Screen("grammar_category_list")
    data object GrammarCategory : Screen("grammar_category/{categoryPath}") {
        const val ARG = "categoryPath"
        fun route(categoryPath: String) = "grammar_category/${Uri.encode(categoryPath)}"
    }
    data object GrammarInfo : Screen("grammar_info/{grammarId}") {
        const val ARG = "grammarId"
        fun route(grammarId: String) = "grammar_info/${Uri.encode(grammarId)}"
    }

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
    data object Profile : Screen("profile")

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
                    composable(Screen.HomeAll.route) {
                        AllScreen(navController)
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(navController)
                    }
                    composable(Screen.KanjiCategoryList.route) {
                        KanjiCategoryListScreen(navController)
                    }
                    composable(
                        route = Screen.KanjiCategory.route,
                        arguments = listOf(navArgument(Screen.KanjiCategory.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val categoryPath = backStackEntry.arguments
                            ?.getString(Screen.KanjiCategory.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        KanjiCategoryScreen(navController, categoryPath)
                    }
                    composable(
                        route = Screen.KanjiInfo.route,
                        arguments = listOf(navArgument(Screen.KanjiInfo.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val kanjiChar = backStackEntry.arguments
                            ?.getString(Screen.KanjiInfo.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        KanjiInfoScreen(navController, kanjiChar)
                    }
                    composable(Screen.SearchKanji.route) {
                        SearchKanjiScreen(navController)
                    }

                    composable(Screen.KanaCategoryList.route) {
                        KanaCategoryListScreen(navController)
                    }
                    composable(
                        route = Screen.KanaCategory.route,
                        arguments = listOf(navArgument(Screen.KanaCategory.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val categoryPath = backStackEntry.arguments
                            ?.getString(Screen.KanaCategory.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        KanaCategoryScreen(navController, categoryPath)
                    }
                    composable(
                        route = Screen.KanaInfo.route,
                        arguments = listOf(navArgument(Screen.KanaInfo.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val kanaId = backStackEntry.arguments
                            ?.getString(Screen.KanaInfo.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        KanaInfoScreen(navController, kanaId)
                    }

                    composable(Screen.VocabCategoryList.route) {
                        VocabCategoryListScreen(navController)
                    }

                    composable(
                        route = Screen.VocabCategory.route,
                        arguments = listOf(navArgument(Screen.VocabCategory.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val categoryPath = backStackEntry.arguments
                            ?.getString(Screen.VocabCategory.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        VocabCategoryScreen(navController, categoryPath)
                    }
                    composable(
                        route = Screen.VocabInfo.route,
                        arguments = listOf(navArgument(Screen.VocabInfo.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val vocabId = backStackEntry.arguments
                            ?.getString(Screen.VocabInfo.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        VocabInfoScreen(navController, vocabId)
                    }

                    composable(Screen.GrammarCategoryList.route) {
                        GrammarCategoryListScreen(navController)
                    }

                    composable(
                        route = Screen.GrammarCategory.route,
                        arguments = listOf(navArgument(Screen.GrammarCategory.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val categoryPath = backStackEntry.arguments
                            ?.getString(Screen.GrammarCategory.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        GrammarCategoryScreen(navController, categoryPath)
                    }
                    composable(
                        route = Screen.GrammarInfo.route,
                        arguments = listOf(navArgument(Screen.GrammarInfo.ARG) {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->

                        val grammarId = backStackEntry.arguments
                            ?.getString(Screen.GrammarInfo.ARG)
                            ?.let { Uri.decode(it) } ?: ""

                        GrammarInfoScreen(navController, grammarId)
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
                    composable(Screen.Profile.route) {
                        ProfileScreen(navController)
                    }
                }
            }
        }
    }
}