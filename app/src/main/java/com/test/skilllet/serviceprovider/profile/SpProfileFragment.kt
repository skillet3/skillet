package com.test.skilllet.serviceprovider.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.test.skilllet.R
import com.test.skilllet.client.bnfragments.profile.EditProfileActivity
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ProfileFragmentBinding
import com.test.skilllet.models.User

class SpProfileFragment: Fragment() {
    lateinit var binding: ProfileFragmentBinding
    var user: User?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        user= Repository.loggedInUser!!
        with(binding){
            tvName.text=user?.name
            rb.rating=user?.rating!!
            if(user?.address!!.isNotEmpty()){
                address.text=user?.address
            }
            if(user!!.phNumber.isNotEmpty()){
                tvMobileNumber.text=user!!.phNumber
            }
            if(user!!.email.isNotEmpty()){
                tvMail.text=user!!.email
            }
            if(user!!.url.isNotEmpty()){
                Glide
                    .with(this@SpProfileFragment)
                    .load(user!!.url)
                    .centerCrop()
                    .placeholder(R.drawable.profile_charachter)
                    .into(ivProfilePic);
            }

            btnEdit.setOnClickListener {
                startActivity(Intent(activity, EditProfileActivity::class.java))
            }
        }
    }
}