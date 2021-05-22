package com.hjhj.musicplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.hjhj.musicplayer.databinding.FragmentPlayerBinding
import com.hjhj.musicplayer.service.MusicDto
import com.hjhj.musicplayer.service.MusicService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayerFragment: Fragment(R.layout.fragment_player){

    private var binding: FragmentPlayerBinding? = null
    private var isWatchingPlayListView = true
    private var player:SimpleExoPlayer? = null
    private lateinit var playListAdapter: PlayListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initPlayView(fragmentPlayerBinding)
        //재생버튼ui처리
        initPlayListButton(fragmentPlayerBinding)
        //재생버튼 동작처리
        initPlayControlButtons(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)

        getVideoListFromServer()
    }

    private fun initPlayControlButtons(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playControlImageView.setOnClickListener {
            val player = this.player?: return@setOnClickListener
            if(player.isPlaying){
                player.pause()
            }else{
                player.play()
            }
        }
        fragmentPlayerBinding.skipNextImageView.setOnClickListener {

        }
        fragmentPlayerBinding.skipPrevImageView.setOnClickListener {

        }
    }


    private fun initPlayView(fragmentPlayerBinding: FragmentPlayerBinding) {
        context?.let{
            player = SimpleExoPlayer.Builder(it).build()
        }
        fragmentPlayerBinding.playerView.player = player
        binding?.let{ binding->
            player?.addListener(object: Player.EventListener{
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if(isPlaying){
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_pause_24)
                    }else{
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                }
            })

        }
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        playListAdapter = PlayListAdapter {  }
            //todo 음악재생
        fragmentPlayerBinding.playListRecyclerView.apply{
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playListImageView.setOnClickListener {
            //todo 만약에 서버에서 데이터가 다 불려오지 않은 상태일 때

            fragmentPlayerBinding.playerViewGroup.isVisible = isWatchingPlayListView
            fragmentPlayerBinding.playListViewGroup.isVisible = isWatchingPlayListView.not()

            isWatchingPlayListView = !isWatchingPlayListView
        }
    }

    private fun getVideoListFromServer(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(MusicService::class.java)
            .also{
                it.listMusics()
                    .enqueue(object :Callback<MusicDto>{
                        override fun onResponse(call: Call<MusicDto>, response: Response<MusicDto>) {
                            Log.d("PlayerFragment","${response.body()}")
                            response.body()?.let {
                                //response.body()타입은 MusicDto인데 이건 List<MusicEntity> 의 타입인 musics 하나만 멤버변수로 가지고있음
                                //그냥 map{}은 파라미터로 인덱스가 같이 안나와서 mapIndexed씀. mapper()에다가 순서를 넣어주고 싶기 때문이야
                                val modelList = it.musics.mapIndexed { index, musicEntity ->
                                    //mapper는 내가 만든 확장함수임. 반환타입은 musicmodel
                                    musicEntity.mapper(index.toLong())
                                }

                                setMusicList(modelList)

                                playListAdapter.submitList(modelList)

                            }
                        }

                        override fun onFailure(call: Call<MusicDto>, t: Throwable) {
                            Log.d("PlayerFragment","${t.toString()}")

                        }

                    })
            }
    }

    private fun setMusicList(modelList: List<MusicModel>) {
        context?.let{
            player?.addMediaItems(modelList.map{ musicModel->
                MediaItem.Builder()
                    .setMediaId(musicModel.id.toString())
                    .setUri(musicModel.streamUrl)
                    .build()
            })
            player?.prepare()
            player?.play()
        }
    }

    companion object{
        //이렇게 따로 함수만든것은 나중에 매개변수를 넣으면서 뉴할수도 있기에..
        fun newInstance(): PlayerFragment{
            return PlayerFragment()
        }
    }
}