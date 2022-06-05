package com.test.skilllet.util


import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.test.skilllet.R
import com.test.skilllet.notifications.APIClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class RequestStatus(var status: String) {
    PENDING("pending"),
    APPROVED("approved"),
    COMPLETED("completed"),
    DECLINE("decline")
}

enum class ServiceRequest(){
    OFFERED(),
    REQUESTED(),
    REJECTED()
}


enum class ViewType(var view: String) {
    CLIENT("clientView"),
    SERVICE_PROVIDER("spView"),
}

enum class PaymentStatus(var value: String) {
    PAYED("Payed"),
    REQUESTED("Requested"),
    NOT_REQUESTED("NotRequested")
}

fun Context.showDialogBox(msg: String,title:String="Please note", block: () -> Unit) {
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
    dialog.findViewById<TextView>(R.id.tv_title).text=title
    val btnOk: Button = dialog.findViewById(R.id.btn_done)
    btnOk.setOnClickListener {
        block()
        dialog.cancel()
    }
    dialog.show()
}

fun Context.showMultiButtonDialogBox(msg: String, title:String="Exit", block: (boolean:Boolean) -> Unit) {
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
    dialog.findViewById<TextView>(R.id.tv_title).text=title
    val btnExit: Button = dialog.findViewById(R.id.btn_done)
    btnExit.text="Yes"
    val btnCancel:Button=dialog.findViewById(R.id.btn_cancel)
    btnCancel.visibility= View.VISIBLE
    btnExit.setOnClickListener {
        block(true)
        dialog.cancel()
    }
    btnCancel.setOnClickListener {
        block(false)
        dialog.cancel()
    }
    dialog.show()
}

fun Context.showProgressDialog(msg: String, title: String="Please Wait"): ProgressDialog {
    var progressDialog = ProgressDialog(this)
    progressDialog.setMessage(msg) // Setting Message
    progressDialog.setTitle(title) // Setting Title
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) // Progress Dialog Style Spinner
    progressDialog.setCancelable(false)
    return progressDialog
}

fun Context.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun Context.sendNotification(token:String,title:String,msg:String) {
    val payload = buildNotificationPayload(token,  title, msg)
    apiService.sendNotification(payload)!!.enqueue(
        object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                if (response.isSuccessful()) {
//                    Toast.makeText(
//                        this, "Notification send successful",
//                        Toast.LENGTH_LONG
//                    ).show()
                    Log.d("927277", "Notification sent: " + response.toString())
                } else {
                    Log.d("927277", "Error responce: " + response.toString())
                }
            }

            override fun onFailure(call: Call<JsonObject?>?, t: Throwable?) {
                Log.d("927277", "Error failed: ")
            }
        })
}

private fun buildNotificationPayload(
    token: String,
    title: String,
    msg: String
): JsonObject? {


    // compose notification json payload
    val payload = JsonObject()
    payload.addProperty("to", token)
    // compose data payload here
    val data = JsonObject()
    data.addProperty("title", title)
    data.addProperty("msg", msg)
    // add data payload
    payload.add("data", data)
    return payload
}

fun Context.showEditDialogBox(title:String,msg: String, block: (s:String) -> Unit) {
    val dialog = Dialog(this)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.db_get_string)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window!!.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    val text: TextView = dialog.findViewById(R.id.tv_title)
    text.text = title
    val et: EditText =dialog.findViewById(R.id.et)
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

