// app/src/main/java/com/example/autovideo/VideoViewModel.kt
package com.example.autovideo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class VideoViewModel : ViewModel() {
    var isPlaying by mutableStateOf(false)
    var stayTime by mutableStateOf(10)
    var intervalTime by mutableStateOf(2)
    var currentVideoIndex by mutableStateOf(0)

    private val videoUrls = listOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    )

    val currentVideoUri: Uri? get() = 
        videoUrls.getOrNull(currentVideoIndex)?.let { Uri.parse(it) }

    private var autoPlayJob: Job? = null

    fun togglePlay() {
        isPlaying = !isPlaying
    }

    fun nextVideo() {
        currentVideoIndex = (currentVideoIndex + 1) % videoUrls.size
    }

    fun startAutoPlay(player: Any) {
        autoPlayJob?.cancel()
        autoPlayJob = viewModelScope.launch {
            while (true) {
                delay(stayTime * 1000L)
                simulateLike()
                delay(500)
                simulateFollow()
                delay(500)
                simulateCollect()
                delay(intervalTime * 1000L)
                withContext(Dispatchers.Main) {
                    currentVideoIndex = (currentVideoIndex + 1) % videoUrls.size
                }
            }
        }
    }

    fun stopAutoPlay() {
        autoPlayJob?.cancel()
    }

    private fun simulateLike() = println("✅ 点赞")
    private fun simulateFollow() = println("✅ 关注")
    private fun simulateCollect() = println("✅ 收藏")

    override fun onCleared() {
        autoPlayJob?.cancel()
    }
}
