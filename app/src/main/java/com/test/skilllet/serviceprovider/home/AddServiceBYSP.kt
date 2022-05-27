package com.test.skilllet.serviceprovider.home

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import androidx.appcompat.app.AppCompatActivity
import com.test.skilllet.R

import com.test.skilllet.database.Repository

import com.test.skilllet.databinding.ActivityAddServiceByspBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showProgressDialog


class AddServiceBYSP : AppCompatActivity() {

    lateinit var binding: ActivityAddServiceByspBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServiceByspBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            var progressDiaog:ProgressDialog=this@AddServiceBYSP.showProgressDialog(
                "Please Wait",
                "Loading Service Types"
            )
            progressDiaog.show()
            Repository.getAllServicesTypes {
                progressDiaog.cancel()
                val spAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@AddServiceBYSP,
                    android.R.layout.simple_list_item_1, it!!
                )
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.etServiceName.setAdapter(spAdapter)
            }

            etServiceName.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var index=""
                    Repository.getAllServicesTypes {
                        index= it?.get(position).toString()
                    }
                    var arr=Repository.getServicesByName(index)
                    val spAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@AddServiceBYSP,
                        android.R.layout.simple_list_item_1, arr!!
                    )
                    spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.etServiceName.setAdapter(spAdapter)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            btnAddService.setOnClickListener {
                var service= ServiceModel()
                var price=etPrice.text.toString().trim()
                var desc=etDesc.text.toString().trim()
                if(price.isEmpty()||desc.isEmpty()){

                }else{
                    service.price=price
                    service.description=desc
                    service.type=etServiceType.selectedItem as String
                    service.name=etServiceName.selectedItem as String

                    var progressDialog=this@AddServiceBYSP.showProgressDialog("Please Wait",
                        "Adding Service"
                        )
                    progressDialog.show()
                    Repository.addServiceBySP(Repository.loggedInUser!!,service){
                        progressDialog.cancel()
                        if(it){

                            this@AddServiceBYSP.showDialogBox("Service has been added Successfully."){

                            }
                        }else{
                            this@AddServiceBYSP.showDialogBox("Service Could not be added."){

                            }
                        }
                    }

                }
            }
        }


    }
}