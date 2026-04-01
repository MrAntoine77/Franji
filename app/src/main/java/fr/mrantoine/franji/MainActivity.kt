package fr.mrantoine.franji

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
import fr.mrantoine.franji.ui.components.BottomBar
import fr.mrantoine.franji.ui.screens.auth.SplashScreen
import fr.mrantoine.franji.ui.screens.auth.WelcomeScreen
import fr.mrantoine.franji.ui.theme.FranjiTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.mrantoine.franji.ui.navigation.AuthRoutes
import fr.mrantoine.franji.ui.screens.auth.*
import fr.mrantoine.franji.ui.screens.main.MainScreen
import fr.mrantoine.franji.ui.screens.main.cards.CardsPlaying
import fr.mrantoine.franji.ui.screens.main.cards.CardsRecapScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FranjiTheme(dynamicColor = false) {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = AuthRoutes.SPLASH) {
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
                            onStartClick = { navController.navigate(AuthRoutes.CARD_PLAYING) }
                        )
                    }

                    composable(AuthRoutes.CARD_PLAYING) {
                        CardsPlaying()
                    }
                }
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