package com.kinotech.kinotechappv1.ui.lists

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kinotech.kinotechappv1.R

public class RecyclerAdapterLists(val context: Context) :
    RecyclerView.Adapter<RecyclerAdapterLists.MyViewHolder>() {

    var listsOfMovie: List<ListsOfMovie> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listsOfMovie.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val lists = listsOfMovie[position]
        holder.bind(lists)

        // holder.imgList.setImageResource(listsOfMovie.)
        /* Glide.with(context).load(listsOfMovie.get(position).imgList)
             .apply(RequestOptions().centerCrop())
             .into(holder.imgList)*/
    }

    fun setMovieListItems(movieList: List<ListsOfMovie>) {
        this.listsOfMovie = movieList
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(lists: ListsOfMovie) {
            val itemTitle: TextView = itemView.findViewById(R.id.item_title)
            val filmCount: TextView = itemView.findViewById(R.id.film_count)
            val imgListH: ImageView = itemView.findViewById(R.id.img_list)

            itemTitle.text = lists.itemTitle
            filmCount.text = lists.filmCount
            val imgList: String = lists.imgList
            Log.d("tag", "karkar" + imgList)
            Glide
                // .with(itemView.context)
                .with(itemView.context)
                .load(imgList)
                .error(R.drawable.ic_home_black_24dp)
                .into(imgListH)
        }
    }
}
