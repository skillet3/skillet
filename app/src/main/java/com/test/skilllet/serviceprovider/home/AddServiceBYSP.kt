package com.test.skilllet.serviceprovider.home

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager

import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityAddServiceBinding

import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.*


class AddServiceBYSP : AppCompatActivity() {

    lateinit var binding: ActivityAddServiceBinding
    var list:ArrayList<String>?=null
    var service:ServiceModel?=null
    var tagsList=ArrayList<String>()
    var adapter:TagsAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getSerializableExtra("service")!=null){
            service=intent.getSerializableExtra("service") as ServiceModel
            binding.tvScreenTitle.text="Update Service"
            binding.btnRequestService.text="Update"
            binding.spServiceType.isEnabled=false
            binding.ivAddServiceType.isEnabled=false
            binding.etDesc.setText(service?.description)
            binding.etPrice.setText(service?.price)
            binding.etServiceName.setText(service?.name)
            tagsList= service?.tags!!

        }
         adapter= TagsAdapter(tagsList)


        with(binding){
            rvTags.layoutManager= GridLayoutManager(this@AddServiceBYSP,2)
            rvTags.adapter=adapter
            ivAddTag.setOnClickListener {
                if(tagsList.size<5){
                    addNewTag()
                }else{
                    this@AddServiceBYSP.showToast("Max 5 tags are allowed")
                }
            }

            ivAddServiceType.setOnClickListener {
                addNewService()
            }

            btnRequestService.setOnClickListener {
                if(!isValidated()){
                    showToast("No field should be empty")
                }else{
                    if(service==null){
                        service= ServiceModel()
                    }
                    service?.apply {
                        description=etDesc.text.toString()
                        price=etPrice.text.toString()
                        type=spServiceType.selectedItem.toString()
                        name=etServiceName.text.toString()
                        tags=tagsList
                        userKey=Repository.loggedInUser!!.key
                        offeringStatus=OfferingStatus.REQUESTED.name
                    }
                    val dialog:ProgressDialog?=this@AddServiceBYSP.showProgressDialog("Adding/Updating Service").apply {
                        show()
                    }
                    Repository.addOrUpdateService(service!!){
                        dialog?.cancel()
                        if(it){
                            this@AddServiceBYSP.showDialogBox("Service has been Successfully added/updated with the following information.\n" +
                                    "Type : ${service!!.type}\n" +
                                    "Name : ${service!!.name}\n"
                                +"Price : ${service!!.price}"
                            ){
                                onBackPressed()
                            }
                        }else{
                            this@AddServiceBYSP.showDialogBox("Not able to add new Service.\n" +
                                    "Reason Unknown"){}
                        }
                    }
                }
            }
            btnBack2.setOnClickListener {
                onBackPressed()
            }
        }


        Repository.getListOfServiceTypes {
            if(it!=null){
                list=it

                val spAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@AddServiceBYSP,
                    android.R.layout.simple_list_item_1, list!!
                )
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spServiceType.adapter = spAdapter
                if(intent.getSerializableExtra("service")!=null){
                    binding.spServiceType.setSelection(list!!.indexOf(service?.type))
                }
            }else{
                addNewService()
            }

        }

    }

    private fun addNewService() {
        this.showEditDialogBox("Add New Service Type","Enter Service Type Here"){
            if(it.isNotEmpty()){
                val dialog=this@AddServiceBYSP.showProgressDialog("Adding New Service Type",
                    "Please Wait").apply {
                    show()
                }
                Repository.addNewServiceType(it){
                    dialog.cancel()
                    if(it){
                        this@AddServiceBYSP.showToast("Service added")
                    }else{
                        this@AddServiceBYSP.showToast("Services not added")
                    }
                }
            }
        }
    }


    private fun isValidated():Boolean{
        with(binding){
            if(etServiceName.text.trim().isEmpty()||etPrice.text.trim().isEmpty()||etDesc.text.trim().isEmpty()||
                    tagsList.isEmpty()){
                return false
            }
            return true
        }
    }






    fun addNewTag(){
        this.showEditDialogBox("Add New Tag","Enter Tag Here"){
            if(it.isNotEmpty()){
                tagsList.add(it)
                adapter?.notifyItemInserted(tagsList.lastIndex)
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}