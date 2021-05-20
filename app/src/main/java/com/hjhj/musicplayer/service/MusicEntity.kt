package com.hjhj.musicplayer.service

import com.google.gson.annotations.SerializedName

data class MusicEntity (
    //SerializedName은 서버에서 내려오는 json키값
    @SerializedName("track") val track: String,
    @SerializedName("streamUrl") val streamUrl: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("cover") val coverUrl: String
        )