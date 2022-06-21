package com.test.skilllet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rohitss.uceh.UCEHandler

import com.test.skilllet.admin.HomeActivity
import com.test.skilllet.client.ClientActivity
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityLoginBinding
import com.test.skilllet.models.User
import com.test.skilllet.serviceprovider.SPMainActivity
import com.test.skilllet.util.showDialogBox
import com.test.skilllet.util.showProgressDialog

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UCEHandler.Builder(applicationContext).build()

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {

            with(binding) {
                var email = etEmail.text.toString().trim()
                var password = etPassword.text.toString().trim()

                if (!checkIfLoggedIn()) {
                    if (email.isEmpty() || password.isEmpty()) {
                        if (email.isEmpty()) {
                            tilEmail.error = "Email is empty"
                        }
                        if (password.isEmpty()) {
                            tilPassword.error = "Password is empty"
                        }
                    } else if (password.length < 6) {
                        tilPassword.error = "Password should be at least 6 characters."
                    } else{
                        var progressDialog=this@LoginActivity.showProgressDialog("Please Wait","Logging you In").apply {
                            show()
                        }
                        Repository.loginUser(email,password, User.getAccountType(rbClient.isChecked)){
                            progressDialog.cancel()
                            if(!it){
                                this@LoginActivity.showDialogBox(
                                    "Login Failed! Try Again\n Check if you have verified your email."
                                ){

                                }
                            }else{
                                etEmail.setText("")
                                etPassword.setText("")
                                if(email.equals("skillskillet3@gmail.com")){
                                    startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                                }else if(rbClient.isChecked) {
                                    startActivity(Intent(this@LoginActivity,ClientActivity::class.java))
                                }else{
                                    startActivity(Intent(this@LoginActivity,SPMainActivity::class.java))
                                }
                            }
                        }
                    }
                }else{
                    Repository.mAuth?.signOut()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(checkIfLoggedIn()){
            Repository.mAuth?.signOut()
        }
        Repository.getListOfServiceTypes {  }
    }

    private fun checkIfLoggedIn(): Boolean {
        if (Repository.currentFirebaseUser != null) {
            //already logged in
            return true
        }
        return false
    }
}