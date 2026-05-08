package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.ui.screens.main.home.kanji.KanjiState

@Composable
fun BottomBarItem(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = contentColor,
            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.background).navigationBarsPadding()
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 2.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            verticalAlignment = Alignment.Top
        ) {
            val items = listOf(
                "Accueil" to Icons.Outlined.Home,
                "Quizz" to Icons.Default.PlayArrow,
                "Révision" to Icons.Default.Menu,

            )

            items.forEachIndexed { index, (label, icon) ->
                BottomBarItem(
                    icon = icon,
                    label = label,
                    isSelected = index == selectedIndex,
                    onClick = {
                        when(index) {
                            0 -> navController.navigate(Screen.KanjiList.route(KanjiState.CategoryLsit, "", ""))
                            1 -> navController.navigate(Screen.QuizzWorldList.route)
                            2 -> navController.navigate(Screen.CardsList.route)
                            else -> navController.navigate(Screen.KanjiList.route(KanjiState.CategoryLsit, "", ""))
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}