package com.katdmy.android.myfirstkotlinapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ActorsAdapter(private val listener: ActorsClickListener): RecyclerView.Adapter<ActorsViewHolder>() {
    private var actors = listOf<Actor>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_actor, parent, false)
        return ActorsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.onBind(actors[position])
        holder.itemView.setOnClickListener { listener.onClick() }
    }

    override fun getItemCount(): Int {
        return actors.size
    }

    fun setData(newActors: List<Actor>) {
        actors = newActors
        notifyDataSetChanged()
    }

    fun shuffleData() {
        val oldList = actors
        val newList = actors.shuffled()
        actors = newList
        Log.e("ActorsAdapter", "ShuffledList: $newList")
        val diffCallback = ActorDiffUtilCallback(oldList, newList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);
//        actors = actors.shuffled()
//        notifyDataSetChanged()
    }

}

class ActorsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val photo: ImageView = itemView.findViewById(R.id.actor_photo)
    private val name: TextView = itemView.findViewById(R.id.actor_name)

    fun onBind(actor: Actor) {
        photo.setImageDrawable(ContextCompat.getDrawable(itemView.context, actor.photo))
        photo.clipToOutline = true
        name.text = actor.name
    }
}

interface ActorsClickListener {
    fun onClick()
}