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

@Composable
fun BottomBarItem(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

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
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalDivider(
            color = Color.Gray,
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
                "Révision" to Icons.Outlined.Menu,
                "Quizz" to Icons.Outlined.Info,
                "Profile" to Icons.Outlined.Face,
                "Recherche" to Icons.Outlined.Search
            )

            items.forEachIndexed { index, (label, icon) ->
                BottomBarItem(
                    icon = icon,
                    label = label,
                    isSelected = index == selectedIndex,
                    onClick = {
                        when(index) {
                            0 -> navController.navigate(Screen.HomeAll.route)
                            1 -> navController.navigate(Screen.CardsList.route)
                            else -> navController.navigate(Screen.HomeAll.route)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}