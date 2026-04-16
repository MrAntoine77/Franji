package fr.mrantoine.franji.ui.screens.main.cards


import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun CardsRecapScreen(
    navController: NavController,
    cardsPath: String
) {

    var cardsList by remember { mutableStateOf(emptyArray<String>()) }
    var mode by remember { mutableStateOf(Mode.THEME) }
    LaunchedEffect(Unit) {
        cardsList = CategoryStorage.getCategoryByPath(cardsPath)
    }


    val title = cardsPath.replace("/", " ")

    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher


    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = "Révision",
                showBack = true,
                onBackClick = { backDispatcher?.onBackPressed() }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(Dimens.l)
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "- $title -".uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(Dimens.m))

            val total = cardsList.size
            val totalKanji = cardsList.count { it.startsWith("kanji") }
            val totalVocab = cardsList.count { it.startsWith("vocab") }
            val totalGrammar = cardsList.count { it.startsWith("grammar") }


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total de kanjis"
                )
                Text(
                    text = if(mode == Mode.BOTH) (totalKanji * 2).toString() else totalKanji.toString(),
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total de mots de vocabulaire"
                )
                Text(
                    text = if(mode == Mode.BOTH) (totalVocab * 2).toString() else totalVocab.toString(),
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total de leçons de grammaire"
                )
                Text(
                    text = if(mode == Mode.BOTH) (totalGrammar * 2).toString() else totalGrammar.toString(),
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total de cartes",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if(mode == Mode.BOTH) (total * 2).toString() else total.toString(),
                    fontWeight = FontWeight.Bold
                )

            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.secondary,
                thickness = 2.dp
            )
            Spacer(modifier = Modifier.height(Dimens.m))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Choisissez un mode :")
                val fr_flag = "\uD83C\uDDEB\uD83C\uDDF7"
                val jp_flag = "\uD83C\uDDEF\uD83C\uDDF5"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mode == Mode.THEME,
                        onClick = { mode = Mode.THEME }
                    )
                    Text("$fr_flag → $jp_flag Thème")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mode == Mode.VERSION,
                        onClick = { mode = Mode.VERSION }
                    )
                    Text("$jp_flag → $fr_flag Version")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mode == Mode.BOTH,
                        onClick = { mode = Mode.BOTH }
                    )
                    Text("$jp_flag ↔ $fr_flag Les deux")
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                cardsList.shuffle()
                ThemedButton(
                    onClick = { navController.navigate(Screen.CardsPlaying.route(cardsList, title, mode)) },
                    text = "Commencer la révision"
                )
            }
        }
    }
}