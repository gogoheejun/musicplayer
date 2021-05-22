package com.hjhj.musicplayer

import com.hjhj.musicplayer.service.MusicEntity

//MUsicEntiy을 확장!
fun MusicEntity.mapper(id:Long): MusicModel =
    //원래 함수 괄호치고 return있어야하는데 걍 생략하고 =으로바꿈
    MusicModel(
        id = id,
        streamUrl = this.streamUrl,
        coverUrl = this.coverUrl,
        track = track, //this생략(this는 musicEntity)
        artist = artist
    )