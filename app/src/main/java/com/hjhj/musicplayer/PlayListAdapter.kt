package com.hjhj.musicplayer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PlayListAdapter(private val callback:(MusicModel)->Unit): ListAdapter<MusicModel, PlayListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val view:View): RecyclerView.ViewHolder(view) {
        fun bind(item:MusicModel){
            val trackTextView = view.findViewById<TextView>(R.id.itemTrackTextView)
            val artistTextView = view.findViewById<TextView>(R.id.itemArtistTextView)
            val coverImageView = view.findViewById<ImageView>(R.id.itemCoverImageView)

            trackTextView.text = item.track
            artistTextView.text = item.artist
            Glide.with(coverImageView.context)
                .load(item.coverUrl)
                .into(coverImageView)

            if(item.isPlaying){
                //itemView는 부모클라스인 RecyclerView.ViewHolder의 멤버변수..ViewHolder 생성자 파라미터 view가 itemView가 됨
                    //다음처럼 선언되어있음
//                public ViewHolder(@NonNull View itemView) {
//                    if (itemView == null) {
//                        throw new IllegalArgumentException("itemView may not be null");
//                    }
//                    this.itemView = itemView;
//                }
                itemView.setBackgroundColor(Color.GRAY)
            }else{
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }
            itemView.setOnClickListener{
                callback(item)
            }



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].also{ musicModel ->
            holder.bind(musicModel)
        }
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<MusicModel>(){
            override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                //id값비교
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                //컨텐츠 비교
                return oldItem == newItem
            }

        }
    }


}