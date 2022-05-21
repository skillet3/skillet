package com.test.skilllet.util


import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.test.skilllet.R

enum class RequestStatus(var status:String){
    PENDING("pending"),
    APPROVED("approved"),
    COMPLETED("completed"),
    DECLINE("decline")
}
enum class ViewType(var view:String){
    CLIENT("clientView"),
    SERVICE_PROVIDER("spView"),
}

fun Context.showDialogBox(msg:String,block:()->Unit) {
    val dialog = Dialog(this)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.db_msg)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window!!.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT
    )
    val text: TextView = dialog.findViewById(R.id.tv_msg)
    text.text = msg
    val btnOk: Button = dialog.findViewById(R.id.btn_done)
    btnOk.setOnClickListener{
        block()
        dialog.cancel()
    }
    dialog.show()
}

fun Context.showProgressDialog(msg:String,title:String):ProgressDialog{
    var progressDialog = ProgressDialog(this)
    progressDialog.setMessage(msg) // Setting Message
    progressDialog.setTitle(title) // Setting Title
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
    progressDialog.setCancelable(false)
    return progressDialog
}

fun Context.showToast(s: String) {
    Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
}
