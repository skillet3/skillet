package com.test.skilllet.admin


import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityAddServiceBinding
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.ServiceType
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showProgressDialog
import com.test.skilllet.util.showToast


class AddService : AppCompatActivity() {

    lateinit var binding:ActivityAddServiceBinding
    var list:ArrayList<ServiceType>?=null
    var service:ServiceModel?=null
    //var tagsList=ArrayList<String>()
    //var adapter:TagsAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getSerializableExtra("service")!=null){
            service=intent.getSerializableExtra("service") as ServiceModel
            binding.tvScreenTitle.text="Update Service"
            binding.btnAddService.text="Update"
            binding.etDesc.setText(service?.description)
            //binding.etServiceType.setText(service.type)
            binding.etPrice.setText(service?.price)
            binding.etServiceName.setText(service?.name)
        }else{
            binding.etDesc.visibility= View.GONE
            binding.etPrice.visibility=View.GONE
            binding.tvTags.visibility=View.GONE
            binding.ivAddTag.visibility=View.GONE
            binding.constraint.visibility=View.GONE
        }
       // adapter= TagsAdapter(tagsList)


        with(binding){
         //   rvTags.layoutManager=GridLayoutManager(this@AddService,2)
           // rvTags.adapter=adapter
//            ivAddTag.setOnClickListener {
//                if(tagsList.size<5){
//                    addNewTag()
//                }else{
//                    this@AddService.showToast("Max 5 tags are allowed")
//                }
//            }

            ivAddServiceType.setOnClickListener {
                addNewService()
            }

            btnAddService.setOnClickListener {
                if(!isValidated()){
                    showToast("No field should be empty")
                }else{
                   if(service==null){
                       service=ServiceModel()
                   }
                    service?.apply {
                        //description=etDesc.text.toString()
                        //price=etPrice.text.toString()
                        //type=etServiceType.text.toString()
                        type=spServiceType.selectedItem.toString()
                        name=etServiceName.text.toString()
                       // tags=tagsList
                    }
                    val dialog:ProgressDialog?=this@AddService.showProgressDialog("Adding/Updating Service").apply {
                        show()
                    }
                    Repository.addOrUpdateServiceByAdmin(service!!){
                        dialog?.cancel()
                        if(it){
                            this@AddService.showDialogBox("Service has been Successfully added/updated with the following information.\n" +
                                    "Type : ${service!!.type}\n" +
                                    "Name : ${service!!.name}\n"
                                    //+"Price : ${service!!.price}"
                            ){
                              //  etDesc.text.clear()
                               // etPrice.text.clear()
                                etServiceName.text.clear()
                               // etServiceType.text.clear()
                                spServiceType.setSelection(0)
                                finish()
                            }
                        }else{
                            this@AddService.showDialogBox("Not able to add new Service.\n" +
                                    "Reason Unknown"){}
                        }
                    }
                }
            }
        }


        Repository.getListOfServiceTypes {
            if(it!=null){
                list=it
                var arr=getTypesList(list)
                val spAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@AddService,
                    android.R.layout.simple_list_item_1, arr!!
                )
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spServiceType.adapter = spAdapter
                if(intent.getSerializableExtra("service")!=null){
                    binding.spServiceType.setSelection(arr.indexOf(service?.type))
                    binding.spServiceType.isEnabled=false
                    binding.ivAddServiceType.isEnabled=false
                //    binding.ivAddTag.isEnabled=false
                }
            }else{
                addNewService()
            }

        }

    }

    private fun addNewService() {
        showDialogBox("Add New Service Type","Enter Service Type Here"){
            if(it.isNotEmpty()){
                val dialog=this@AddService.showProgressDialog("Adding New Service Type",
                    "Please Wait").apply {
                    show()
                }
                Repository.addNewServiceType(it){
                    dialog.cancel()
                    if(it){
                        this@AddService.showToast("Service added")
                    }else{
                        this@AddService.showToast("Services not added")
                    }
                }
            }
        }
    }


    private fun isValidated():Boolean{
        with(binding){
            if(etServiceName.text.isEmpty()){
                return false
            }
            return true
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun getTypesList(list: ArrayList<ServiceType>?): ArrayList<String> {
        val slist=ArrayList<String>()
        for(type in list!!){
            slist.add(type.name)
        }
        return slist
    }

    private fun showDialogBox(title:String,msg: String, block: (s:String) -> Unit) {
        val dialog = Dialog(this@AddService)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.db_get_string)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val text: TextView = dialog.findViewById(R.id.tv_title)
        text.text = title
        val et:EditText=dialog.findViewById(R.id.et)
        et.setHint(msg)
        val btnAdd: Button = dialog.findViewById(R.id.btn_add)
        btnAdd.setOnClickListener {
            var s=et.text.toString()
            if(s.isEmpty()){
                block("")
            }else{
                block(et.text.toString())
            }
            dialog.cancel()
        }
        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            block("")
            dialog.cancel()
        }
        dialog.show()
    }

//    fun addNewTag(){
//        showDialogBox("Add New Tag","Enter Tag Here"){
//            if(it.isNotEmpty()){
//                tagsList.add(it)
//                adapter?.notifyItemInserted(tagsList.lastIndex)
//            }
//        }
//    }
}