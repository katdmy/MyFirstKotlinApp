package com.katdmy.android.myfirstkotlinapp

import androidx.recyclerview.widget.DiffUtil
import com.katdmy.android.myfirstkotlinapp.data.Actor

class ActorDiffUtilCallback(
    private val oldList: List<Actor>?,
    private val newList: List<Actor>?
): DiffUtil.Callback() {
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