package fr.mrantoine.franji.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.*
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

@Composable
fun SplashScreen(
    navController: NavController,
) {
    val context = LocalContext.current

    var showLoader by remember { mutableStateOf(false) }

    var lottie = """{"v":"5.5.7","fr":64,"ip":0,"op":288,"w":109,"h":109,"ddd":0,"layers":[{"ddd":0,"ind":1,"ty":4,"ks":{"o":{"a":0,"k":100,"ix":11},"r":{"a":0,"k":0,"ix":10},"p":{"a":0,"k":[0,0,0],"ix":2},"a":{"a":0,"k":[0,0,0],"ix":1},"s":{"a":0,"k":[100,100,100],"ix":6}},"shapes":[{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-1.44,0.12],[-9.92,0.88],[-0.91,-0.18]],"o":[[1.97,0.4],[6.92,-0.58],[1.44,-0.13],[0,0]],"v":[[12.78,39.23],[17.46,39.46],[46.31,36.73],[49.02,36.73]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":0,"s":[0]},{"t":32,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[0.0,-2.34],[0.14,-18.94],[0.03,-0.95]],"o":[[1.13,1.13],[0.0,0.83],[-0.03,3.54],[0,0]],"v":[[34.11,13.5],[36.4,18.84],[36.18,86.75],[36.1,94.25]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":32,"s":[0]},{"t":64,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[0.34,-0.77],[7.89,-9.26]],"o":[[0.0,0.93],[-4.46,10.08],[0,0]],"v":[[35.44,39.32],[34.84,41.67],[15.11,72.76]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":64,"s":[0]},{"t":96,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-1.88,-3.98]],"o":[[2.53,1.53],[0,0]],"v":[[40.72,48.97],[49.38,60.23]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":96,"s":[0]},{"t":128,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-1.85,0.2],[-10.35,0.41],[-1.58,-0.36]],"o":[[2.29,0.48],[13.35,-1.44],[1.83,-0.07],[0,0]],"v":[[52.62,41.52],[58.63,41.92],[92.78,38.21],[98.62,38.35]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":128,"s":[0]},{"t":160,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[0.0,-2.4],[0.08,-4.96],[4.53,4.53]],"o":[[1.49,1.49],[0.0,14.56],[-0.14,8.74],[0,0]],"v":[[79.89,13.25],[82.15,18.77],[82.39,89.64],[72.68,89.89]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":160,"s":[0]},{"t":192,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1},{"ty":"gr","it":[{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-0.68,-3.23]],"o":[[2.74,2.07],[0,0]],"v":[[59.58,56.78],[67.33,68.53]]},"ix":2}},{"ty":"tm","s":{"a":1,"k":[{"i":{"x":[0.667],"y":[1]},"o":{"x":[0.333],"y":[0]},"t":192,"s":[0]},{"t":224,"s":[100]}],"ix":1},"e":{"a":0,"k":0,"ix":2},"o":{"a":0,"k":0,"ix":3},"m":1,"ix":2},{"ty":"st","c":{"a":0,"k":[0,0,0,1],"ix":3},"o":{"a":0,"k":100,"ix":4},"w":{"a":0,"k":3,"ix":5},"lc":2,"lj":2,"ml":4,"bm":0},{"ind":0,"ty":"sh","ix":1,"ks":{"a":0,"k":{"i":[[0,0],[-3.12,0.25],[-8.74,0.77],[-2.12,-0.24]],"o":[[2.5,1.19],[11.55,-0.91],[2.64,-0.23],[0,0]],"v":[[32.63,34.69],[40.08,36.04],[67.12,32.86],[75.51,32.94]]},"ix":2}},{"ty":"tr","p":{"a":0,"k":[0,0],"ix":2},"a":{"a":0,"k":[0,0],"ix":1},"s":{"a":0,"k":[100,100],"ix":3},"r":{"a":0,"k":0,"ix":6},"o":{"a":0,"k":100,"ix":7},"sk":{"a":0,"k":0,"ix":4},"sa":{"a":0,"k":0,"ix":5}}],"np":4,"cix":2,"bm":0,"ix":1}],"ip":0,"op":288,"st":0,"bm":0}]}"""


    LaunchedEffect(Unit) {

        delay(100)

        val splashMinDelay = launch {
            delay(2000)
        }

        val loadingJob = launch(Dispatchers.IO) {

            SettingsStorage.loadCache(context)

            if (SettingsStorage.isRestCacheOnLaunch()) {
                CategoryStorage.clearCache(context)
                KanjiStorage.clearCache(context)
                KanaStorage.clearCache(context)
                LottieStorage.clearCache(context)
                VocabStorage.clearCache(context)
                GrammarStorage.clearCache(context)
                MainPageStorage.clearCache(context)
            }
            CategoryStorage.loadCache(context)
            KanjiStorage.loadCache(context)
            KanaStorage.loadCache(context)
            LottieStorage.loadCache(context)
            VocabStorage.loadCache(context)
            GrammarStorage.loadCache(context)
            MainPageStorage.loadCache(context)

            CategoryStorage.loadAll(context)
            KanjiStorage.loadAll(context)
            KanaStorage.loadAll(context)
            LottieStorage.loadAll(context)
            VocabStorage.loadAll(context)
            GrammarStorage.loadAll(context)
            MainPageStorage.loadAll(context)

            CategoryStorage.saveCache(context)
            KanjiStorage.saveCache(context)
            KanaStorage.saveCache(context)
            LottieStorage.saveCache(context)
            VocabStorage.saveCache(context)
            GrammarStorage.saveCache(context)
            MainPageStorage.saveCache(context)

            TtsStorage.init(context)
        }

        launch {
            delay(3000)
            showLoader = true
        }

        splashMinDelay.join()
        loadingJob.join()

        navController.navigate(Screen.QuizzWorldList.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .aspectRatio(1f)
            ) {

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .aspectRatio(1f)
                    ) {
                        Lottie(
                            data = lottie,
                            autoPlay = true,
                            color = MaterialTheme.colorScheme.primary,
                            speed = 3f,
                            lineMultiplier = 2.8f,
                            replayable = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.s))

            Text(
                text = "Mura Studio",
                color = Color.White,
                fontSize = 48.sp
            )
        }

        if (showLoader) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            )
        }
    }
}