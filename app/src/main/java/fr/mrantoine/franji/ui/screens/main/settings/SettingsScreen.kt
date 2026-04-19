package fr.mrantoine.franji.ui.screens.main.settings

import android.os.Trace.isEnabled
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch



data class SettingItem(
    val title: String,
    val checked: Boolean,
    val onToggle: (Boolean) -> Unit
)

@Composable
fun SettingsScreen(
    navController: NavController,
) {
    val context = LocalContext.current

    var isEdited by remember { mutableStateOf(false) }

    var isRomajiEnabled by remember { mutableStateOf(SettingsStorage.isRomaji()) }
    var isRestCacheOnLaunch by remember { mutableStateOf(SettingsStorage.isRestCacheOnLaunch()) }

    val settings = remember(isRomajiEnabled, isRestCacheOnLaunch) {
        listOf(
            SettingItem(
                title = "Caractères romaji",
                checked = isRomajiEnabled,
                onToggle = { value ->
                    SettingsStorage.setRomaji(value)
                    isRomajiEnabled = value
                    isEdited = true
                }
            ),
            SettingItem(
                title = "Reset du cache au lancement",
                checked = isRestCacheOnLaunch,
                onToggle = { value ->
                    SettingsStorage.setRestCacheOnLaunch(value)
                    isRestCacheOnLaunch = value
                    isEdited = true
                }
            )
        )
    }

    BackHandler(enabled = isEdited) {
        Toast.makeText(
            context,
            "Vous devez sauvegarder vos modifications avant de quitter",
            Toast.LENGTH_SHORT
        ).show()
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = "Paramètres",
                    showBack = true,
                    onBackClick = {
                        if (isEdited) {
                            Toast.makeText(
                                context,
                                "Vous devez sauvegarder vos modifications avant de quitter",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            navController.popBackStack()
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isEdited) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .padding(Dimens.m),
                    contentAlignment = Alignment.Center
                ) {
                    ThemedButton(
                        text = "Sauvegarder",
                        onClick = {
                            SettingsStorage.saveCache(context)
                            isEdited = false
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(settings.size) { index ->
                val item = settings[index]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { item.onToggle(!item.checked) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.m),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = item.checked,
                            onCheckedChange = null
                        )
                    }
                }
            }
        }
    }
}

