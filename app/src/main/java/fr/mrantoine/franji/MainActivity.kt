package fr.mrantoine.franji

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import fr.mrantoine.franji.ui.components.BottomBar
import fr.mrantoine.franji.ui.screens.auth.SplashScreen
import fr.mrantoine.franji.ui.screens.auth.WelcomeScreen
import fr.mrantoine.franji.ui.theme.FranjiTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.mrantoine.franji.ui.navigation.AuthRoutes
import fr.mrantoine.franji.ui.screens.auth.*
import fr.mrantoine.franji.ui.screens.main.cards.CardsPlaying
import fr.mrantoine.franji.ui.screens.main.cards.CardsRecapScreen
import fr.mrantoine.franji.ui.screens.main.home.AllScreen
import fr.mrantoine.franji.ui.screens.main.home.KanjiCategoryList
import fr.mrantoine.franji.ui.screens.main.home.KanjiCategoryScreen
import fr.mrantoine.franji.ui.screens.main.home.KanjiInfoScreen


sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object HomeAll : Screen("home_all")
    data object KanjiCategoryList : Screen("kanji_category_list")

    data object KanjiCategory : Screen("kanji_category/{categoryPath}") {
        const val ARG = "categoryPath"
        fun route(categoryPath: String) = "kanji_category/${Uri.encode(categoryPath)}"
    }
    data object KanjiInfo : Screen("kanji_info/{kanjiChar}") {
        const val ARG = "kanjiChar"
        fun route(kanjiChar: String) = "kanji_info/${Uri.encode(kanjiChar)}"
    }



}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FranjiTheme(dynamicColor = false) {
                val navController = rememberNavController()


                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route
                ) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navController)
                    }
                    composable(Screen.Welcome.route) {
                        WelcomeScreen(navController)
                    }
                    composable(Screen.Register.route) {
                        RegisterScreen(navController)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(navController)
                    }
                    composable(Screen.HomeAll.route) {
                        AllScreen(navController)
                    }
                    composable(Screen.KanjiCategoryList.route) {
                        KanjiCategoryList(navController)
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
                }




                /*NavHost(navController = navController, startDestination = AuthRoutes.SPLASH) {
                    composable(AuthRoutes.SPLASH) {
                        SplashScreen(
                            onTimeout = { navController.navigate(AuthRoutes.WELCOME)  {
                                popUpTo(AuthRoutes.SPLASH) { inclusive = true }
                            } }
                        )
                    }
                    composable(AuthRoutes.WELCOME) {
                        WelcomeScreen(
                            onSignUpClick = { navController.navigate(AuthRoutes.REGISTER) },
                            onLoginClick = { navController.navigate(AuthRoutes.LOGIN) },
                        )
                    }
                    composable(AuthRoutes.REGISTER) {
                        RegisterScreen(
                            onBackClick = { navController.navigate(AuthRoutes.WELCOME) },
                            onRegisterClick = { navController.navigate(AuthRoutes.MAIN) },
                            onLoginClick = { navController.navigate(AuthRoutes.LOGIN) }
                        )
                    }
                    composable(AuthRoutes.LOGIN) {
                        LoginScreen(
                            onBackClick = { navController.navigate(AuthRoutes.WELCOME) },
                            onRegisterClick = { navController.navigate(AuthRoutes.REGISTER) },
                            onLoginClick = { navController.navigate(AuthRoutes.MAIN) }
                        )
                    }
                    composable(AuthRoutes.MAIN) {
                        MainScreen(
                            onCardClick = { navController.navigate(AuthRoutes.CARD_RECAP) }
                        )
                    }

                    composable(AuthRoutes.CARD_RECAP) {
                        CardsRecapScreen(
                            onStartClick = { navController.navigate(AuthRoutes.CARD_PLAYING) },
                            onBackClick = { navController.navigate(AuthRoutes.MAIN) },

                        )
                    }

                    composable(AuthRoutes.CARD_PLAYING) {
                        CardsPlaying()
                    }
                }*/
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FranjiTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Greeting("Android")

            Spacer(modifier = Modifier.weight(1f)) // pousse la BottomBar en bas
            BottomBar()
        }
    }
}