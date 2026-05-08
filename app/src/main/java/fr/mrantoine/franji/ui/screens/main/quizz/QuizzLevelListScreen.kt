package fr.mrantoine.franji.ui.screens.main.quizz

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.ui.components.ProgressBar
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.TopBar


@Composable
fun QuizzLevelListScreen(
    navController: NavController,
    worldPath: String,
    worldColor: Long
) {
    var paths by remember { mutableStateOf<Array<String>>(emptyArray()) }
    var counts by remember { mutableStateOf<List<Map<String, Int>>>(emptyList()) }
    var firsts by remember { mutableStateOf<List<Array<String>>>(emptyList()) }

    val context = LocalContext.current


    BackHandler {
        navController.navigate(Screen.QuizzWorldList.route)
    }

    LaunchedEffect(Unit) {
        paths = CategoryStorage.getKeys(context, "$worldPath/", true)
        print(paths)
        counts = paths.map { path ->
            CategoryStorage.getCountsIds(context, path)
        }
        firsts = paths.map { path ->
            CategoryStorage.getFirsts(context, path)
        }
        print(counts)

    }
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding())
            {
                TopBar(
                    title = worldPath.split("/").getOrNull(1)?.replace("_", " ") ?: "",
                    showBack = true,
                    onBackClick =  { backDispatcher?.onBackPressed() },
                    navController = navController
                )
            }
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 1,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            paths.forEachIndexed { index, path ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            navController.navigate(Screen.QuizzPlaying.route(path))
                        }
                        .background(Color(worldColor))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val title = path.split("/")[2].replace("_" , " ")
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        ProgressBar(
                            progress = 0.5f,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            data class WorldStatUi(
                                val key: String,
                                val label: String,
                                val indexIcon: Int
                            )
                            val stats = listOf(
                                WorldStatUi("kanji", "Kanji", 0),
                                WorldStatUi("vocab", "Vocabulaire", 1),
                                WorldStatUi("grammar", "Grammaire", 2)
                            )

                            Row {
                                stats.forEach { stat ->

                                    val count = counts[index][stat.key] ?: 0

                                    if (count != 0) {
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = firsts[index].getOrNull(stat.indexIcon) ?: "",
                                                color = Color.White,
                                                fontSize = 20.sp
                                            )

                                            Text(
                                                text = stat.label,
                                                color = Color.White,
                                                fontSize = 14.sp
                                            )

                                            Text(
                                                text = "0/$count",
                                                color = Color.White,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}