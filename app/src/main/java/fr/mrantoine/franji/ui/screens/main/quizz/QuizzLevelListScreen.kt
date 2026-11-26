package fr.mrantoine.franji.ui.screens.main.quizz

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.FirstElement
import fr.mrantoine.franji.ui.components.ProgressBar
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.TopBar


@Composable
fun QuizzLevelListScreen(
    navController: NavController,
    worldPath: String,
    worldColor: Long
) {
    var levelPaths by remember { mutableStateOf<Array<String>>(emptyArray()) }
    var levelFirsts by remember { mutableStateOf<List<FirstElement>>(emptyList()) }
    var levelProgress by remember { mutableStateOf<Map<String, Float>>(emptyMap()) }

    val context = LocalContext.current


    BackHandler {
        navController.navigate(Screen.QuizzWorldList.route)
    }

    LaunchedEffect(Unit) {

        levelPaths = CategoryStorage.getKeys(context, "$worldPath/")

        levelFirsts = CategoryStorage.getLevelFirsts(context, worldPath)

        levelProgress = levelPaths.associateWith { path ->
            CategoryStorage.getProgress(context, path, true)
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
            levelPaths.forEachIndexed { index, path ->
                LevelCard(
                    navController = navController,
                    path = path,
                    worldColor = worldColor,
                    levelProgress = levelProgress,
                    levelFirsts = levelFirsts,
                    index = index
                )
            }
        }
    }
}


@Composable
fun LevelCard(
    navController: NavController,
    path: String,
    worldColor: Long,
    levelProgress: Map<String, Float>,
    levelFirsts: List<FirstElement>,
    index: Int
) {
    val accent = Color(worldColor)
    val accentDark = lerp(accent, Color.Black, 0.25f)
    val first = levelFirsts[index]
    val progress = levelProgress[path] ?: 0f
    val title = path.split("/")[2].replace("_", " ")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { navController.navigate(Screen.QuizzPlaying.route(path)) }
            .background(Brush.horizontalGradient(listOf(accent, accentDark)))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Progression circulaire
        Box(
            modifier = Modifier.size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.25f),
                strokeWidth = 5.dp,
                strokeCap = StrokeCap.Round
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                first.kanji?.let {
                    StatChip(it, "Kanji", first.kanji_count, Modifier.weight(1f))
                }
                first.vocab?.let {
                    StatChip(it.shorten(), "Vocab", first.vocab_count, Modifier.weight(1f))
                }
                first.grammar?.let {
                    StatChip(it.shorten(), "Gram.", first.grammar_count, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StatChip(
    symbol: String,
    label: String,
    count: Any?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .padding(vertical = 6.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = symbol, color = Color.White, fontSize = 18.sp, maxLines = 1)
        Text(text = label, color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
        Text(
            text = "0/$count",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun String.shorten() = if (length > 5) take(4) + "…" else this