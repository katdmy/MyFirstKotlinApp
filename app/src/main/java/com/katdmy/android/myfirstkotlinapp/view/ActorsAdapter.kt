package com.katdmy.android.myfirstkotlinapp.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.katdmy.android.myfirstkotlinapp.R
import com.katdmy.android.myfirstkotlinapp.model.Actor

class ActorsAdapter(private val onActorClickListener: () -> Unit) :
    RecyclerView.Adapter<ActorsViewHolder>() {
    private var actors: List<Actor>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_actor, parent, false)
        return ActorsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.onBind(actors?.get(position))
        holder.itemView.setOnClickListener { onActorClickListener() }
    }

    override fun getItemCount(): Int {
        return actors?.size ?: 0
    }

    fun setData(newActors: List<Actor>?) {
        actors = newActors
        notifyDataSetChanged()
    }

    fun shuffleData() {
        val oldList = actors
        val newList = actors?.shuffled()
        actors = newList
        Log.e("ActorsAdapter", "ShuffledList: $newList")
        val diffCallback = ActorDiffUtilCallback(oldList, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }
}

class ActorsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val photo: ImageView = itemView.findViewById(R.id.actor_photo)
    private val name: TextView = itemView.findViewById(R.id.actor_name)

    fun onBind(actor: Actor?) {
        Glide.with(itemView.context)
            .load(actor?.picture)
            .apply(options)
            .into(photo)

        photo.clipToOutline = true
        name.text = actor?.name
    }

    companion object {
        private val options = RequestOptions()
            .transforms(CenterCrop(), RoundedCorners(8))
            .placeholder(R.drawable.movie_placeholder)
            .error(R.drawable.movie_placeholder)
    }
}

class ActorDiffUtilCallback(
    private val oldList: List<Actor>?,
    private val newList: List<Actor>?
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList?.size ?: 0

    override fun getNewListSize(): Int = newList?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList?.get(oldItemPosition)
        val newItem = newList?.get(newItemPosition)
        return oldItem?.name == newItem?.name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList?.get(oldItemPosition)
        val newItem = newList?.get(newItemPosition)
        return oldItem == newItem
    }
}