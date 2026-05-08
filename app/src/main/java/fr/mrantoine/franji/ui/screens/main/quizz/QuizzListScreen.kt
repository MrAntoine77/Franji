package fr.mrantoine.franji.ui.screens.main.quizz

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.ui.components.ProgressBar
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.components.navigation.Tree




val worldColors = listOf(
    0xFF4FC3F7, // bleu ciel (très facile)
    0xFF81C784, // vert clair
    0xFFFFCE20, // jaune doux
    0xFFFF8A65, // orange clair
    0xFFBA68C8, // violet doux
)



@Composable
fun QuizzListScreen(
    navController: NavController
) {
    var paths by remember { mutableStateOf<Array<String>>(emptyArray()) }
    var counts by remember { mutableStateOf<List<Map<String, Int>>>(emptyList()) }
    var firsts by remember { mutableStateOf<List<Array<String>>>(emptyList()) }

    val context = LocalContext.current


    BackHandler {
        // TODO
    }

    LaunchedEffect(Unit) {
        paths = CategoryStorage.getWorlds(context, "Quizz")
        counts = paths.map { path ->
            CategoryStorage.getCountsIds(context, "$path/Total")
        }
        firsts = paths.map { path ->
            CategoryStorage.getFirsts(context, "$path/Total")
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
            paths.forEachIndexed { index, path ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable {

                        }
                        .background(Color(worldColors[index % worldColors.size]))
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
                        ProgressBar(
                            progress = 0.5f,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = firsts[index].getOrNull(0) ?: "",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Kanji",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "0/${counts[index]["kanji"] ?: 0}",
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
                                    text = firsts[index].getOrNull(1) ?: "",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Vocabulaire",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "0/${counts[index]["vocab"] ?: 0}",
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
                                    text = firsts[index].getOrNull(2) ?: "",
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "Grammaire",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "0/${counts[index]["grammar"] ?: 0}",
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