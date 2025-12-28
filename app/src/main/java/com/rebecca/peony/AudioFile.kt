package com.rebecca.peony

data class AudioFile(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val filePath: String
)