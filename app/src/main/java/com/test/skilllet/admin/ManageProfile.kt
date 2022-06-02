package com.test.skilllet.admin


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityManageProfileBinding
import com.test.skilllet.models.User
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showToast

class ManageProfile : AppCompatActivity() {
    lateinit var binding:ActivityManageProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityManageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var user =intent.getSerializableExtra("user") as User
        with(binding){
            tvName.text=user.name
            rb.rating=user.rating
            if(user.address.isNotEmpty()){
                address.text=user.address
            }
            if(user.phNumber.isNotEmpty()){
                tvMobileNumber.text=user.phNumber
            }
            if(user.email.isNotEmpty()){
                tvMail.text=user.email
            }
            if(user.url.isNotEmpty()){
                Glide
                    .with(this@ManageProfile)
                    .load(user.url)
                    .centerCrop()
                    .placeholder(R.drawable.profile_charachter)
                    .into(ivProfilePic);
            }

            btnDelete.setOnClickListener {
                this@ManageProfile.showDialogBox("You will be deleting the following User\n" +
                        "Account Type: ${user.accType}\n"+
                        "User Name: ${user.name}\n"+
                        "Rating : ${user.rating}\n"){
                    Repository.deleteAccount(user){
                        if (it){
                            finish()
                        }else{
                            this@ManageProfile.showToast("Could not delete this item")
                        }
                    }
                }
            }
        }


    }
}