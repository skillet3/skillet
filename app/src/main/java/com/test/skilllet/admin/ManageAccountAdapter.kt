package com.test.skilllet.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.RowAccountDetailsBinding
import com.test.skilllet.models.User
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showToast

class ManageAccountAdapter(var context: Context, var list:ArrayList<User>):
    RecyclerView.Adapter<ManageAccountAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=RowAccountDetailsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tvName.text=list[position].name
            rbAccount.rating= list[position].rating
            rbClient.isChecked=list[position].accType=="Client"
            rbServiceProvider.isChecked=list[position].accType=="ServiceProvider"

            ivCancel.setOnClickListener {
                context.showDialogBox("You will be deleting the following User\n" +
                        "Account Type: ${list[position].accType}\n"+
                        "User Name: ${list[position].name}\n"+
                        "Rating : ${list[position].rating}\n"){
                    Repository.deleteAccount(list[position]){
                        if (it){
                            list.removeAt(position)
                            notifyItemRemoved(position)
                        }else{
                            context.showToast("Could not delete this item")
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount()=  list.size

    public class ViewHolder(val binding:RowAccountDetailsBinding):RecyclerView.ViewHolder(binding.root){

    }
}