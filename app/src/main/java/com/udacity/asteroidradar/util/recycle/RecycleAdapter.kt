package com.udacity.asteroidradar.util.recycle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class RecycleAdapter(private val clickListener: ClickListener) : ListAdapter<Asteroid, RecycleAdapter.Holder>(
    DiffCallback
) {
    class Holder(private var binding: AsteroidItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ClickListener, asteroid: Asteroid){
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>(){
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(AsteroidItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val asterItem = getItem(position)
        holder.bind(clickListener, asterItem)
    }

}

class ClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}