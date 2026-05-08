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
import fr.mrantoine.franji.storage.ProgressWorld
import fr.mrantoine.franji.ui.components.ProgressBar
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.TopBar


val worldColors = listOf(
    0xFFE06A8C,
    0xFFDB5F84,
    0xFFD6547C,
    0xFFD04974,
    0xFFCB3E6C,
    0xFFC53364,
    0xFFC0285C,
    0xFFBA2458, // proche base
    0xFFD5246B, // base
    0xFFC61F61,
    0xFFB91C5A,
    0xFFAC1953,
    0xFF9F164C,
    0xFF921245,
    0xFF850F3E,
    0xFF780C37,
    0xFF6B0931,
    0xFF5E062B,
    0xFF520425,
    0xFF46031F
)



@Composable
fun QuizzWorldListScreen(
    navController: NavController
) {
    var worldPaths by remember { mutableStateOf<Array<String>>(emptyArray()) }
    var worldCounts by remember { mutableStateOf<List<Map<String, Int>>>(emptyList()) }
    var worldFirsts by remember { mutableStateOf<List<Array<String>>>(emptyList()) }
    var worldProgress by remember { mutableStateOf<Map<String, Float>>(emptyMap()) }

    val context = LocalContext.current


    BackHandler {
        // TODO
    }

    LaunchedEffect(Unit) {
        worldPaths = CategoryStorage.getWorlds(context, "Quizz")
        worldCounts = worldPaths.map { path ->
            CategoryStorage.getCountsIds(context, "$path/Total")
        }
        worldFirsts = worldPaths.map { path ->
            CategoryStorage.getFirsts(context, "$path/Total")
        }
        worldProgress = worldPaths.associateWith { path ->
            CategoryStorage.getProgress(context, path)
        }
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
                    title = "Quizz",
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
            worldPaths.forEachIndexed { index, path ->
                val colorLong = worldColors[index % worldColors.size]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {
                            navController.navigate(Screen.QuizzLevelList.route(path, colorLong))
                        }
                        .background(Color(colorLong))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val title = path.split("/")[1].replace("_" , " ")
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        val progress = worldProgress[path] ?: 0f
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProgressBar(
                                progress = progress,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "${progress.toInt()} %",
                                color = Color.White,
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 10.sp
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = worldFirsts[index].getOrNull(0) ?: "",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Kanji",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "0/${worldCounts[index]["kanji"] ?: 0}",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = worldFirsts[index].getOrNull(1) ?: "",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Vocabulaire",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "0/${worldCounts[index]["vocab"] ?: 0}",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = worldFirsts[index].getOrNull(2) ?: "",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Grammaire",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "0/${worldCounts[index]["grammar"] ?: 0}",
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