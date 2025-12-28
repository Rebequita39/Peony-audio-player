package com.rebecca.peony

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rebecca.peony.ui.theme.PeonyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeonyTheme {
                MusicPlayerApp()
            }
        }
    }
}

@Composable
fun MusicPlayerApp(viewModel: MusicViewModel = viewModel()) {
    val state by viewModel.state.collectAsState(initial = MusicPlayerState())
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            PlayerControls(
                currentlyPlaying = state.currentlyPlaying,
                isPlaying = state.isPlaying,
                onPlayClick = {
                    if (state.currentlyPlaying != null) {
                        viewModel.play()
                        Toast.makeText(context, "Playing: ${state.currentlyPlaying?.title}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Select a song first", Toast.LENGTH_SHORT).show()
                    }
                },
                onPauseClick = {
                    viewModel.pause()
                    Toast.makeText(context, "Paused", Toast.LENGTH_SHORT).show()
                },
                onStopClick = {
                    viewModel.stop()
                    Toast.makeText(context, "Stopped", Toast.LENGTH_SHORT).show()
                }
            )
        }
    ) { innerPadding ->
        MusicList(
            audioFiles = state.audioFiles,
            onSongClick = { audioFile ->
                viewModel.selectSong(audioFile)
                Toast.makeText(context, "Selected: ${audioFile.title}", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MusicList(
    audioFiles: List<AudioFile>,
    onSongClick: (AudioFile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(audioFiles) { audioFile ->
            AudioFileItem(
                audioFile = audioFile,
                onClick = { onSongClick(audioFile) }
            )
        }
    }
}

@Composable
fun AudioFileItem(
    audioFile: AudioFile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = audioFile.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = audioFile.artist,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatDuration(audioFile.duration),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PlayerControls(
    currentlyPlaying: AudioFile?,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (currentlyPlaying != null) {
                    "Now Playing: ${currentlyPlaying.title} - ${currentlyPlaying.artist}"
                } else {
                    "No song playing"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPlayClick,
                    enabled = !isPlaying,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Play")
                }

                Button(
                    onClick = onPauseClick,
                    enabled = isPlaying,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Pause")
                }

                Button(
                    onClick = onStopClick,
                    enabled = currentlyPlaying != null
                ) {
                    Text("Stop")
                }
            }
        }
    }
}

fun formatDuration(duration: Long): String {
    val seconds = (duration / 1000) % 60
    val minutes = (duration / (1000 * 60)) % 60
    return String.format("%d:%02d", minutes, seconds)
}