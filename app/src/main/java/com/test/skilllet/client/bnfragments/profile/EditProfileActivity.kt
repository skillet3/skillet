package com.test.skilllet.client.bnfragments.profile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityEditProfileBinding
import com.test.skilllet.models.User
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showProgressDialog
import com.test.skilllet.util.showToast

class EditProfileActivity : AppCompatActivity() {

    lateinit var binding:ActivityEditProfileBinding

    var uri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user=Repository.loggedInUser
        with(binding){
            etName.setText(user?.name)
            if(user?.address?.isNotEmpty()==true){
                etAddress.setText(user.address)
            }
            if(user?.phNumber?.isNotEmpty()==true){
                etMobileNumber.setText(user.phNumber)
            }

            if(user?.url?.isNotEmpty()==true){
                Glide
                    .with(this@EditProfileActivity)
                    .load(user.url)
                    .placeholder(R.drawable.profile_charachter)
                    .into(ivProfilePic);
            }

            clEditPic.setOnClickListener {
                getContent.launch("image/*")
            }

            btnUpdate.setOnClickListener {
                if(etName.text.isEmpty()||etAddress.text.isEmpty()||etMobileNumber.text.isEmpty()){
                    this@EditProfileActivity.showToast("No field should be empty.")
                }else{
                    if(etName.text.equals(user?.name)&&etMobileNumber.text.equals(user?.phNumber)&&
                            etAddress.equals(user?.address)){
                        this@EditProfileActivity.showToast("NO changes made")
                    }else{
                        user?.name=etName.text.trim().toString()
                        user?.phNumber=etMobileNumber.text.trim().toString()
                        user?.address=etAddress.text.trim().toString()
                        val progressDialog=this@EditProfileActivity.showProgressDialog("Updating Profile","Please Wait")
                        progressDialog.show()
                        Repository.updateUserProfile(uri,user){
                            progressDialog.cancel()
                            if(it){
                                this@EditProfileActivity.showToast("Profile updated")
                                finish()
                            }else{
                                this@EditProfileActivity.showToast("Error in updating profile.\nError 404")

                            }
                        }

                    }
                }

            }
        }

    }
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { _uri: Uri? ->
        _uri?.let{
            this.uri=_uri
            binding.ivProfilePic.setImageURI(uri)
        }
    }

}