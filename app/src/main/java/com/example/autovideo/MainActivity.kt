// app/src/main/java/com/example/autovideo/MainActivity.kt
package com.example.autovideo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoVideoApp()
        }
    }
}

@Composable
fun AutoVideoApp() {
    val viewModel: VideoViewModel = viewModel()
    val player = remember { ExoPlayer.Builder(this).build() }

    LaunchedEffect(viewModel.isPlaying) {
        if (viewModel.isPlaying) {
            player.playWhenReady = true
            viewModel.startAutoPlay(player)
        } else {
            player.playWhenReady = false
            viewModel.stopAutoPlay()
        }
    }

    LaunchedEffect(viewModel.currentVideoUri) {
        if (viewModel.currentVideoUri != null) {
            player.setMediaItem(MediaItem.fromUri(viewModel.currentVideoUri!!))
            player.prepare()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("自动视频播放器") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply { player = this@AutoVideoApp.player }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                OutlinedButton(onClick = { viewModel.togglePlay() }) {
                    Text(if (viewModel.isPlaying) "停止" else "开始")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = { viewModel.nextVideo() }) {
                    Text("下一个")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("停留时间: ${viewModel.stayTime} 秒", Modifier.padding(horizontal = 16.dp))
            Slider(
                value = viewModel.stayTime.toFloat(),
                onValueChange = { viewModel.stayTime = it.toInt() },
                valueRange = 5f..60f,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text("间隔时间: ${viewModel.intervalTime} 秒", Modifier.padding(horizontal = 16.dp))
            Slider(
                value = viewModel.intervalTime.toFloat(),
                onValueChange = { viewModel.intervalTime = it.toInt() },
                valueRange = 1f..10f,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
