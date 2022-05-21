package com.test.skilllet.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityAddServiceBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showToast

class AddService : AppCompatActivity() {

    lateinit var binding:ActivityAddServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent!=null){

            binding.tvScreenTitle.text="Update Service"
            binding.btnAddService.text="Update"
            binding.etDesc.setText(intent.getStringExtra("desc"))
            binding.etServiceType.setText(intent.getStringExtra("type"))
            binding.etPrice.setText(intent.getStringExtra("price"))
            binding.etServiceName.setText(intent.getStringExtra("name"))

        }

        with(binding){
            btnAddService.setOnClickListener {
                if(!isValidated()){
                    showToast("No field should be empty")
                }else{
                    val service=ServiceModel()
                    service.apply {
                        description=etDesc.text.toString()
                        price=etPrice.text.toString()
                        type=etServiceType.text.toString()
                        name=etServiceName.text.toString()
                    }

                    Repository.addServiceByAdmin(service){
                        if(it){
                            this@AddService.showDialogBox("A new Service has been Successfully added wih the following information.\n" +
                                    "Type : ${service.type}\n" +
                                    "Name : ${service.name}\n" +
                                    "Price : ${service.price}"){
                                etDesc.text.clear()
                                etPrice.text.clear()
                                etServiceName.text.clear()
                                etServiceType.text.clear()
                            }
                        }else{
                            this@AddService.showDialogBox("Not able to add new Service.\n" +
                                    "Reason Unknown"){}
                        }
                    }
                }
            }
        }

    }



    private fun isValidated():Boolean{
        with(binding){
            if(etDesc.text.isEmpty()||etServiceName.text.isEmpty()||
                    etServiceName.text.isEmpty()||etPrice.text.isEmpty()){
                return false
            }
            return true
        }
    }
}