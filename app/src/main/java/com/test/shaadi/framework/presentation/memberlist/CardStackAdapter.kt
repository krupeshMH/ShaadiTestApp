package com.test.shaadi.framework.presentation.memberlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.test.shaadi.R
import com.test.shaadi.business.domain.model.MemberEach

class CardStackAdapter(
    private var spots: List<MemberEach>
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        holder.name.text = "${spot.name.first}. ${spot.dob.age}"
        holder.city.text = spot.location.city
        Glide.with(holder.image)
            .load(spot.picture.large)
            .apply(
                RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(SIZE_ORIGINAL)
            )
            .placeholder(R.color.white)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return spots.size
    }

    /*fun setSpots(spots: List<MemberEach>) {
        this.spots = spots
    }*/

    fun getSpots(): List<MemberEach> {
        return spots
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.item_name)
        var city: TextView = view.findViewById(R.id.item_city)
        var image: ImageView = view.findViewById(R.id.item_image)
    }

}