package com.test.skilllet.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowManageSertypesBinding
import com.test.skilllet.models.ServiceType
import com.test.skilllet.util.showProgressDialog
import com.test.skilllet.util.showToast

class ManageSerTypesAdapter(val context: Context, val list:ArrayList<String>) : RecyclerView.Adapter<ManageSerTypesAdapter.ViewHolder>() {




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ManageSerTypesAdapter.ViewHolder {
        val view=RowManageSertypesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tvName.text=list[position]
            btnDelete.setOnClickListener {
                val dialog=context?.showProgressDialog("Deleting Service Type")
                dialog?.show()
                Repository.deleteService(list[position]){
                    dialog?.cancel()
                    if(it){
                        list.removeAt(position)
                        notifyItemRemoved(position)
                        context?.showToast("Deleted Successfully")
                    }else{
                        context?.showToast("Operation Failed")
                    }
                }
            }
        }
    }

    override fun getItemCount()=list.size

    class ViewHolder(val binding:RowManageSertypesBinding) : RecyclerView.ViewHolder(binding.root){

    }
}