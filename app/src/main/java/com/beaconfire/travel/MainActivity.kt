package com.beaconfire.travel

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beaconfire.travel.login.LoginScreen
import com.beaconfire.travel.login.LoginViewModel
import com.beaconfire.travel.main.MainScreen
import com.beaconfire.travel.navigation.Navigation
import com.beaconfire.travel.register.RegisterScreen
import com.beaconfire.travel.register.RegisterViewModel
import com.beaconfire.travel.ui.theme.TravelTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelTheme {
                AppScreen(viewModel(factory = AppViewModel.Factory))
            }
        }
    }
}

@Composable
fun AppScreen(appViewModel: AppViewModel) {
    val appUiModel by appViewModel.appUiModel.collectAsState()
    val loginUser by (LocalContext.current.applicationContext as TravelApplication).container.userRepository.loginUserId.collectAsState()

    if (appUiModel.currentScreen == Navigation.Splash) {
        SplashScreen()
        LaunchedEffect(null) {
            delay(3000)
            appViewModel.navigateTo(Navigation.Login)
        }
    } else if (loginUser != null) {
        MainScreen()
    } else {
        when (appUiModel.currentScreen) {
            Navigation.Login -> {
                LoginScreen(loginViewModel = viewModel(factory = LoginViewModel.Factory)) {
                    appViewModel.navigateTo(it)
                }
            }

            Navigation.Register -> {
                RegisterScreen(registerViewModel = viewModel(factory = RegisterViewModel.Factory)) {
                    appViewModel.navigateTo(it)
                }
            }

            else -> {}
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    val exoPlayer = rememberExoPlayer(context, R.raw.splash)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        AndroidView(factory = { ctx ->
            StyledPlayerView(ctx).apply {
                player = exoPlayer
                useController = false
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        }, modifier = Modifier.fillMaxSize())

        Text(
            modifier = Modifier.padding(top = 96.dp),
            text = "Let's enjoy your Trip!",
            style = MaterialTheme.typography.titleLarge
                .copy(fontSize = 36.sp)
                .copy(fontStyle = FontStyle.Italic)
                .copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun rememberExoPlayer(context: Context, videoResId: Int): ExoPlayer {
    val exoPlayer = ExoPlayer.Builder(context).build().apply {
        val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
        val mediaItem = MediaItem.fromUri(videoUri)
        setMediaItem(mediaItem)
        prepare()
        playWhenReady = true
        volume = 0.0f
        repeatMode = Player.REPEAT_MODE_ONE
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    return exoPlayer
}
