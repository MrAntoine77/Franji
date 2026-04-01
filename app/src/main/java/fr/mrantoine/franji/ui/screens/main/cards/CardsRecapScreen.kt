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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.ui.components.ThemedButton
import fr.mrantoine.franji.ui.components.TopBar
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun CardsRecapScreen(
    onStartClick: (() -> Unit) = {},
    title: String = "Titre"
) {
    Column(modifier = Modifier.statusBarsPadding()) {
        TopBar(
            title = "Révision",
            showBack = true
        )
        Column(
            modifier = Modifier.padding(Dimens.l).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "- $title -".uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            data class ItemInfo(
                val value: Int,
                val color: Color
            )
            val tab: Map<String, ItemInfo> = mapOf(
                "Total de cartes" to ItemInfo(100, Color.Black),
                "Total des nouvelles cartes" to ItemInfo(75, Color.Black),
                "À réviser" to ItemInfo(2, Color.Green),
                "À repasser" to ItemInfo(3, Color.Red),
                "Nouvelles" to ItemInfo(20, Color.Blue),
            )
            Spacer(modifier = Modifier.height(Dimens.m))
            tab.forEach { (label, itemInfo) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label
                    )
                    Text(
                        text = itemInfo.value.toString(),
                        color = itemInfo.color
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f) // occupe tout l'espace restant vertical
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ThemedButton(
                    onClick = onStartClick,
                    text = "Commencer la révision"
                )
            }
        }
    }
}