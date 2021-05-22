package com.hjhj.musicplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initPlayListButton(fragmentPlayerBinding)

        getVideoListFromServer()
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

                            }
                        }

                        override fun onFailure(call: Call<MusicDto>, t: Throwable) {
                            Log.d("PlayerFragment","${t.toString()}")

                        }

                    })
            }
    }

    companion object{
        //이렇게 따로 함수만든것은 나중에 매개변수를 넣으면서 뉴할수도 있기에..
        fun newInstance(): PlayerFragment{
            return PlayerFragment()
        }
    }
}