package com.kinotech.kinotechappv1.ui.lists

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.search.SimpleResult
import kotlin.reflect.KClass

class RecyclerAdapterLists(val context: Context, private val clickListener: MyClickListener) :
    RecyclerView.Adapter<RecyclerAdapterLists.MyViewHolder>() {

    private fun unreachable(): Nothing = throw Exception()
    private fun <E : Enum<E>> KClass<E>.enumValues(): Array<out E> =
        java.enumConstants ?: unreachable()

    private fun <E : Enum<E>> KClass<E>.enumValue(ordinal: Int): E = enumValues()[ordinal]
    private inline fun <reified E : Enum<E>> enumValueOf(ordinal: Int): E =
        E::class.enumValue(ordinal)

    val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)
    private var listsOfMovie: List<AnyItemInAdapterList> = listOf()

    interface MyClickListener {
        fun onItemClick(item: AnyItemInAdapterList?)
    }

    override fun getItemViewType(position: Int): Int =
        when (listsOfMovie[position]) {
            is AnyItemInAdapterList.ButtonCreateList -> RecyclerViewItemType.ButtonCreateList
            is AnyItemInAdapterList.ButtonFavList -> RecyclerViewItemType.ButtonFavList
            is AnyItemInAdapterList.ButtonShowList -> RecyclerViewItemType.ButtonShowList
        }.ordinal

    internal enum class RecyclerViewItemType {
        ButtonCreateList, ButtonFavList, ButtonShowList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        when (enumValueOf<RecyclerViewItemType>(viewType)) {
            RecyclerViewItemType.ButtonCreateList -> MyViewHolder.CreateViewHolder(parent)
            RecyclerViewItemType.ButtonFavList -> MyViewHolder.FavViewHolder(parent)
            RecyclerViewItemType.ButtonShowList -> MyViewHolder.ShowViewHolder(parent)
        }

    override fun getItemCount(): Int {
        return listsOfMovie.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val lists = listsOfMovie[position]
        holder.bind(lists, clickListener)
    }

    fun setMovieListItems(movieList: ArrayList<AnyItemInAdapterList>) {
        this.listsOfMovie = movieList
        Log.d("recycler123", "setMovieListItems: $movieList")
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
                    .with(itemView.context)
                    .load(imgList)
                    .error(R.drawable.ic_add_24)
                    .into(imgListH)

                itemView.setOnClickListener {
                    clickListener.onItemClick(
                        lists
                    )
                }
            }
        }

        class FavViewHolder(container: ViewGroup) : MyViewHolder(
            container,
            R.layout.item_fav_list
        ) {
            private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            override fun bind(lists: AnyItemInAdapterList, clickListener: MyClickListener) {
                val itemTitle: TextView = itemView.findViewById(R.id.item_title)
                val filmCount: TextView = itemView.findViewById(R.id.film_count)
                val imgListH: ImageView = itemView.findViewById(R.id.img_list)
                var count: Int
                user?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Liked Movies")
                        .child(it1.toString())
                        .child("Movies")
                }.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        count = snapshot.childrenCount.toInt()
                        Log.d("dataFavourite", "onDataChange: $count ")
                        "$count фильмов".also { filmCount.text = it }
                        Log.d("dataFavourite", "onDataChange: ${filmCount.text} ")
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                itemTitle.text = (lists as AnyItemInAdapterList.ButtonFavList).itemTitle
                val imgList: String = lists.imgList
                Log.d("tag", "carCar$imgList")
                Glide
                    .with(itemView.context)
                    .load(imgList)
                    .error(R.drawable.ic_like_24)
                    .into(imgListH)

                itemView.setOnClickListener {
                    clickListener.onItemClick(
                        lists
                    )
                }
            }
        }

        class ShowViewHolder(container: ViewGroup) : MyViewHolder(
            container,
            R.layout.item_show_list
        ) {
            private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            override fun bind(lists: AnyItemInAdapterList, clickListener: MyClickListener) {
                val itemTitle: TextView = itemView.findViewById(R.id.item_title)
                val filmCount: TextView = itemView.findViewById(R.id.film_count)
                val imgListH: ImageView = itemView.findViewById(R.id.img_list)
                var count: Int
                itemTitle.text = (lists as AnyItemInAdapterList.ButtonShowList).itemTitle
                user?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Lists")
                        .child(it1.toString())
                        .child(itemTitle.text.toString())
                        .child("Movies")
                }.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        count = snapshot.childrenCount.toInt()
                        Log.d("dataFavourite", "onDataChange: $count ")
                        "$count фильмов".also { filmCount.text = it }
                        if (filmCount.text == "0 фильмов") {
                            Glide
                                .with(itemView.context)
                                .load(lists.imgList)
                                .error(R.drawable.ic_baseline_movie_creation_24)
                                .into(imgListH)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                val photoRef = user?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                        .child("Lists")
                        .child(it1.toString())
                        .child(itemTitle.text.toString())
                        .child("Movies")
                }
                val queryUid: Query = photoRef.orderByKey().limitToFirst(1)
                queryUid.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            try {
                                val result = snap.getValue(SimpleResult::class.java)!!
                                val imgList: String = result.posterUrlPreview
                                Log.d(
                                    "dbImg",
                                    "onDataChange3:${snapshot.childrenCount.toInt()} "
                                )
                                Glide
                                    .with(itemView.context)
                                    .load(imgList)
                                    .error(R.drawable.ic_baseline_movie_creation_24)
                                    .into(imgListH)

                            } catch (e: Exception) {
                                Log.d("dataFavourite", "onDataChange: $e")
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                itemView.setOnClickListener {
                    clickListener.onItemClick(
                        lists
                    )
                }
            }
        }
    }
}
