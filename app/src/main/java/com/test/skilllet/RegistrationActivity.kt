package com.test.skilllet

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityRegistrationBinding
import com.test.skilllet.models.User
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showProgressDialog


class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {

            with(binding){
                var name=etName.text.toString().trim()
                var email=etEmail.text.toString().trim()
                var password=etPasword.text.toString().trim()
                if(name.isEmpty()||email.isEmpty()||password.isEmpty()){
                    if(name.isEmpty()){
                        tilName.error="Name is empty"
                    }
                    if(email.isEmpty()){
                        tilEmail.error="Email is empty"
                    }
                    if(password.isEmpty()){
                        tilPassword.error="Password is empty"
                    }
                }else if(password.length<6) {
                        tilPassword.error="Password should be at least 6 characters."
                }else{

                    var progressDialog:ProgressDialog=this@RegistrationActivity.showProgressDialog(
                        "Please Wait","Signing Up"
                    )
                    progressDialog.show()

                    var user= User(name,email,password,User.getAccountType(rbClient.isChecked))
                    Repository.createUserAccount(this@RegistrationActivity,user) { _user: User, isSuccess: Boolean ->
                        progressDialog.cancel()
                        if(isSuccess){
                            var user=Repository.currentFirebaseUser
                            user!!.sendEmailVerification()
                                .addOnCompleteListener { task ->

                                    if (task.isSuccessful) {
                                        // email sent
                                        this@RegistrationActivity.showDialogBox(
                                            resources.getString(R.string.successRegistration)
                                        ){
                                            // after email is sent just logout the user and finish this activity

                                            Repository.mAuth?.signOut()
                                            finish()
                                        }
                                    } else {
                                        this@RegistrationActivity.showDialogBox(
                                            resources.getString(R.string.failedToRegister)
                                        ){
                                            finish()
                                        }
                                    }
                                }
                        }else{
                            this@RegistrationActivity.showDialogBox(
                                resources.getString(R.string.failedToRegister)
                            ){

                            }
                        }

                    }
                }
            }


        }
        binding.btnLogin.setOnClickListener {
            finish()
        }
    }


}