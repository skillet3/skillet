package com.test.skilllet.client.bnfragments.history


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.databinding.RowHistoryTempBinding
import com.test.skilllet.models.WorkingServiceModel


class ClientServiceStatusAdapter(
    var list: ArrayList<WorkingServiceModel>,
    var canChat: Int, canCancel: Int
) :
    RecyclerView.Adapter<ClientServiceStatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding =
            RowHistoryTempBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.tvName.text = list[position].name
            binding.tvType.text = list[position].type
            binding.tvDes.text = list[position].description

        }
    }

    override fun getItemCount() = list.size
    class ViewHolder(val binding: RowHistoryTempBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}