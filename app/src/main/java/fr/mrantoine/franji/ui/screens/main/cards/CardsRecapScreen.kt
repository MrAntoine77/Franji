package fr.mrantoine.franji.ui.screens.main.cards


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.network.getCategoryKanjiChar
import fr.mrantoine.franji.network.getCategoryKanjiId
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun CardsRecapScreen(
    navController: NavController,
    cardsPath: String
) {
    var cardsList by remember { mutableStateOf(emptyArray<String>()) }
    LaunchedEffect(Unit) {
        cardsList = getCategoryKanjiId(cardsPath)
    }



    val title = cardsPath.replace("/", " ")

    Column(modifier = Modifier.statusBarsPadding()) {
        TopBar(
            title = "Révision",
            showBack = true,
            onBackClick = {  }
        )
        Column(
            modifier = Modifier
                .padding(Dimens.l)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "- $title -".uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(Dimens.m))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total de cartes"
                )
                Text(
                    text = cardsList.size.toString(),
                )

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                cardsList.shuffle()
                ThemedButton(
                    onClick = { navController.navigate(Screen.CardsPlaying.route(cardsList, title)) },
                    text = "Commencer la révision"
                )
            }
        }
    }
}