package com.hjhj.musicplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class PlayerFragment: Fragment(R.layout.fragment_player){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object{
        //이렇게 따로 함수만든것은 나중에 매개변수를 넣으면서 뉴할수도 있기에..
        fun newInstance(): PlayerFragment{
            return PlayerFragment()
        }
    }
}