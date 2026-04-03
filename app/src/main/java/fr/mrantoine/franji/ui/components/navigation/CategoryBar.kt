package fr.mrantoine.franji.ui.components.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen

@Composable
fun CategoryBarItem(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
    val textColor = if (isSelected) Color.Black else Color.Gray

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
        HorizontalDivider(
            color = contentColor,
            thickness = 2.dp
        )
    }
}

@Composable
fun CategoryBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    selectedCategoryIndex: Int = 0,
) {
    val items = listOf(
        "Tout",
        "Kanji & Kana",
        "Vocabulaire",
        "Grammaire"
    )
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            items.forEachIndexed { index, item ->
                CategoryBarItem(
                    isSelected = index == selectedCategoryIndex,
                    onClick = {
                        when(index) {
                            0 -> navController.navigate(Screen.HomeAll.route)
                            1 -> navController.navigate(Screen.KanjiCategoryList.route)
                            else -> navController.navigate(Screen.HomeAll.route)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    label = item
                )
            }
        }
    }
}