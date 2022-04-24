package com.test.skilllet.client.bnfragments.history


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.databinding.RowHistoryTempBinding
import com.test.skilllet.models.ServiceModel


class ClientServiceStatusAdapter (var list:ArrayList<ServiceModel>, var iconsList: ArrayList<Drawable>, @ColorInt var color: Int):
    RecyclerView.Adapter<ClientServiceStatusAdapter.ViewHolder>() {

    //comment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding= RowHistoryTempBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            binding.tvName.text=list[position].name
            binding.ivIcon.setImageDrawable(iconsList[position])
            binding.clIcon.setBackgroundColor(color)
            binding.tvType.text=list[position].type
            binding.tvDes.text=list[position].description
        }
    }

    override fun getItemCount()= list.size
    class ViewHolder(val binding: RowHistoryTempBinding): RecyclerView.ViewHolder(binding.root) {

    }
}