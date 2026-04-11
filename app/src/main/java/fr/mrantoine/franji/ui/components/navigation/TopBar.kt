package fr.mrantoine.franji.ui.components.navigation

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.Color

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    showBack: Boolean = false,
    showMenu: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Column() {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .statusBarsPadding(), // padding pour la status bar
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            if (showBack) {
                IconButton(onClick = {
                    if (onBackClick != {}) {
                        onBackClick()
                    } else {
                        backDispatcher?.onBackPressed()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Retour"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Text(
                text = title,
                fontSize = 18.sp
            )

            if (showMenu) {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            thickness = 2.dp
        )
    }
}