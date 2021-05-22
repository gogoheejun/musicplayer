package com.hjhj.musicplayer

class MusicModel (
    val id: Long,
    val trakc: String,
    val streamUrl: String,
    val artist: String,
    val coverUrl: String,
    val isPlaying: Boolean = false
        )