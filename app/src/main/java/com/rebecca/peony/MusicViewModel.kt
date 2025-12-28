package com.rebecca.peony

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MusicPlayerState(
    val audioFiles: List<AudioFile> = emptyList(),
    val currentlyPlaying: AudioFile? = null,
    val isPlaying: Boolean = false
)

class MusicViewModel : ViewModel() {

    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()

    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        val sampleAudioFiles = listOf(
            AudioFile(1, "Sample Song 1", "Artist A", 215000, "/path/to/song1.mp3"),
            AudioFile(2, "Sample Song 2", "Artist B", 195000, "/path/to/song2.mp3"),
            AudioFile(3, "Sample Song 3", "Artist A", 245000, "/path/to/song3.mp3"),
            AudioFile(4, "Sample Song 4", "Artist C", 180000, "/path/to/song4.mp3"),
            AudioFile(5, "Sample Song 5", "Artist B", 220000, "/path/to/song5.mp3")
        )
        _state.value = _state.value.copy(audioFiles = sampleAudioFiles)
    }

    fun selectSong(audioFile: AudioFile) {
        _state.value = _state.value.copy(
            currentlyPlaying = audioFile,
            isPlaying = false
        )
    }

    fun play() {
        if (_state.value.currentlyPlaying != null) {
            _state.value = _state.value.copy(isPlaying = true)
        }
    }

    fun pause() {
        _state.value = _state.value.copy(isPlaying = false)
    }

    fun stop() {
        _state.value = _state.value.copy(
            currentlyPlaying = null,
            isPlaying = false
        )
    }
}