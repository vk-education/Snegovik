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
import com.kinotech.kinotechappv1.R
import kotlin.reflect.KClass

public class RecyclerAdapterLists(val context: Context, val clickListener: MyClickListener) :
    RecyclerView.Adapter<RecyclerAdapterLists.MyViewHolder>() {

    fun unreachable(): Nothing = throw Exception()
    fun <E : Enum<E>> KClass<E>.enumValues(): Array<out E> = java.enumConstants ?: unreachable()
    fun <E : Enum<E>> KClass<E>.enumValue(ordinal: Int): E = enumValues()[ordinal]
    inline fun <reified E : Enum<E>> enumValueOf(ordinal: Int): E = E::class.enumValue(ordinal)

    val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)
    var listsOfMovie: List<AnyItemInAdapterList> = listOf()

    interface MyClickListener {
        fun onItemClick(item: AnyItemInAdapterList?)
    }

    override fun getItemViewType(position: Int): Int =
        when (listsOfMovie.get(position)) {
            is AnyItemInAdapterList.ButtonCreateList -> ReyclerViewItemType.ButtonCreateList
            is AnyItemInAdapterList.ButtonShowList -> ReyclerViewItemType.ButtonShowList
        }.ordinal

    internal enum class ReyclerViewItemType {
        ButtonCreateList, ButtonShowList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        when (enumValueOf<ReyclerViewItemType>(viewType)) {
            ReyclerViewItemType.ButtonCreateList -> MyViewHolder.CreateViewHolder(parent)
            ReyclerViewItemType.ButtonShowList -> MyViewHolder.ShowViewHolder(parent)
        }
    /* val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
     return MyViewHolder(view)*/

    override fun getItemCount(): Int {
        return listsOfMovie.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val lists = listsOfMovie[position]
        holder.bind(lists, clickListener)
    }

    fun setMovieListItems(movieList: List<AnyItemInAdapterList>) {
        this.listsOfMovie = movieList
        notifyDataSetChanged()
    }


    sealed class MyViewHolder(
        container: ViewGroup,
        layoutResId: Int
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(container.context).inflate(layoutResId, container, false)
    ) {
        abstract fun bind(lists: AnyItemInAdapterList, clickListener: MyClickListener)

        class CreateViewHolder(container: ViewGroup) : MyViewHolder(
            container,
            R.layout.item_create_list
        ) {
            override fun bind(lists: AnyItemInAdapterList, clickListener: MyClickListener) {
                val itemTitle: TextView = itemView.findViewById(R.id.item_title)
                val imgListH: ImageView = itemView.findViewById(R.id.img_list)

                itemTitle.text = (lists as AnyItemInAdapterList.ButtonCreateList).itemTitle
                val imgList: String = lists.imgList
                Glide
                    // .with(itemView.context)
                    .with(itemView.context)
                    .load(imgList)
                    .error(R.drawable.ic_add_24)
                    .into(imgListH)

                itemView.setOnClickListener(View.OnClickListener {
                    clickListener.onItemClick(
                        lists
                    )
                })
            }
        }

        class ShowViewHolder(container: ViewGroup) : MyViewHolder(
            container,
            R.layout.item_show_list
        ) {
            override fun bind(lists: AnyItemInAdapterList, clickListener: MyClickListener) {
                val itemTitle: TextView = itemView.findViewById(R.id.item_title)
                val filmCount: TextView = itemView.findViewById(R.id.film_count)
                val imgListH: ImageView = itemView.findViewById(R.id.img_list)


                itemTitle.text = (lists as AnyItemInAdapterList.ButtonShowList).itemTitle
                filmCount.text = lists.filmCount
                val imgList: String = lists.imgList
                Log.d("tag", "karkar" + imgList)
                Glide
                    // .with(itemView.context)
                    .with(itemView.context)
                    .load(imgList)
                    .error(R.drawable.ic_like_24)
                    .into(imgListH)

                itemView.setOnClickListener(View.OnClickListener {
                    clickListener.onItemClick(
                        lists
                    )
                })
            }
        }
    }
}

