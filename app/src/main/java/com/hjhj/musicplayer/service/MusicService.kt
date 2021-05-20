package com.hjhj.musicplayer.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {
    @GET("/v3/c557edf1-6d92-49e6-81bf-e704031bf6f9")
    fun listMusics() : Call<MusicDto>
}