package com.test.skilllet.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.skilllet.database.Repository
import com.test.skilllet.databinding.ActivityManageAccountsBinding
import com.test.skilllet.models.User
import com.test.skilllet.util.showProgressDialog

class ManageAccounts : AppCompatActivity() {

    lateinit var binding:ActivityManageAccountsBinding
    var accountList=ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityManageAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        var dialog=this.showProgressDialog("Please Wait","Loading Accounts")
        dialog?.show();
        Repository.getAccountsList {
            dialog.cancel()
            if (it != null) {
                accountList=it
            }
            var adapter= ManageAccountAdapter(this@ManageAccounts,accountList)

            binding.rv.layoutManager= LinearLayoutManager(this.applicationContext,
                LinearLayoutManager.VERTICAL,false)
            binding.rv.adapter=adapter
        }
    }
}