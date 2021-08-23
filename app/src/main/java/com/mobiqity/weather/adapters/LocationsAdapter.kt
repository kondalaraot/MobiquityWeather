package com.mobiqity.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiqity.weather.data.Location
import com.mobiqity.weather.databinding.LocationListItemBinding

class LocationsAdapter(onItemClickListener:OnItemClickListener) : RecyclerView.Adapter<LocationsAdapter.LocationsVH>() {

    private var items = mutableListOf<Location>()
    private var onItemClickListener: OnItemClickListener? = null

    init {
        this.onItemClickListener = onItemClickListener
    }
    interface OnItemClickListener{
        fun onDeleteLocation(location: Location)
        fun onItemClick(location: Location)
    }

    fun submitData(locations:ArrayList<Location>){
        this.items =locations
        this.notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsVH {
        val binding = LocationListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LocationsVH(binding)
    }

    override fun onBindViewHolder(holder: LocationsVH, position: Int) {
        holder.bind(items[position],onItemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class LocationsVH(itemBinding: LocationListItemBinding): RecyclerView.ViewHolder(itemBinding.root){

        private var itemBinding: LocationListItemBinding = itemBinding

        fun bind(location: Location, onItemClickListener: OnItemClickListener?){
            itemBinding.tvLocName.text = location.locationName
            itemBinding.ivRemove.setOnClickListener(View.OnClickListener {
                onItemClickListener?.onDeleteLocation(location)
            })
            itemBinding.root.setOnClickListener(View.OnClickListener {
                onItemClickListener?.onItemClick(location)
            })
        }

    }
}