package com.test.skilllet.util


import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.view.isVisible
import com.google.gson.JsonObject
import com.test.skilllet.R
import com.test.skilllet.notifications.APIClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

enum class RequestStatus(var status: String) {
    PENDING("pending"),
    APPROVED("approved"),
    COMPLETED("completed"),
    DECLINE("decline")
}

enum class OfferingStatus(){
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

fun sendNotification(token:String,title:String,msg:String) {
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

public fun getProperDate(date: Int, month: Int, year: Int): String {
    var d=""
    var m=""
    var y=""
    if(date<=9){
        d="0$date"
    }else{
        d="$date"
    }
    var mm=month
    //mm+=1
    if(mm<=9){
        m="0$mm"
    }else{
        m="$mm"
    }
    if(year<=9){
        y="0$year"
    }else{
        y="$year"
    }

    return "$d/$m/$y"
}


fun getDate(milliSeconds: Long): String {
    var dateFormat="dd/MM/yyyy"
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}
fun getTime(milliSeconds: Long): String {
    var dateFormat="hh:mm:ss"
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}
public fun Context.showDialoguePickerDialogue(date: String?,time:String?,callBack:(str:String)->Unit) {
    val dialog = Dialog(this)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.db_date_picker_dialogue)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window!!.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    var calendarView=dialog.findViewById<CalendarView>(R.id.calendarView)
    calendarView.date= getDate(date)
    calendarView.minDate=System.currentTimeMillis()
    var pickedDtae= date
    var pickedTime=time
    var tvDate=dialog.findViewById<TextView>(R.id.tv_date)
    var tvTime=dialog.findViewById<TextView>(R.id.tv_time)
    tvDate.setText(pickedDtae)
    tvTime.setText(pickedTime)
    var timePicker=dialog.findViewById<TimePicker>(R.id.timePicker)
    timePicker.setIs24HourView(false)
    calendarView.visibility=View.GONE
    timePicker.visibility=View.GONE
    dialog.findViewById<ImageView>(R.id.iv_pick_date).setOnClickListener {
        if(calendarView.isVisible){
            calendarView.visibility=View.GONE
            timePicker.visibility=View.GONE
        }else{
            calendarView.visibility=View.VISIBLE
            timePicker.visibility=View.GONE
        }
    }
    dialog.findViewById<ImageView>(R.id.iv_pick_time).setOnClickListener {
        if(timePicker.isVisible){
            calendarView.visibility=View.GONE
            timePicker.visibility=View.GONE
        }else{
            calendarView.visibility=View.GONE
            timePicker.visibility=View.VISIBLE
        }
    }
    calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
        pickedDtae = getProperDate(dayOfMonth, month, year)
        tvDate.setText(pickedDtae)
    }
    timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
        pickedTime=getProperTime(hourOfDay,minute)
        tvTime.setText(pickedTime)
    }

    val btnPick: Button = dialog.findViewById(R.id.btn_pick)
    val btnCancel: Button = dialog.findViewById(R.id.btn_cancel)
    btnPick.setOnClickListener {
        callBack(pickedDtae+" "+pickedTime)
        dialog.cancel()
    }
    btnCancel.setOnClickListener {
        callBack("")
        dialog.cancel()
    }
    dialog.show()
}

fun getProperTime(hourOfDay: Int, minute: Int): String {
    var time=""
    if(hourOfDay<9){
        time="0$hourOfDay:"
    }else{
        time="$hourOfDay:"
    }
    if(minute<9){
        time+="0$minute:00"
    }else{
        time+="$minute:00"
    }
    return time
}
fun getDate(d: String?): Long{
    val myDate = "$d"
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val date = sdf.parse(myDate)
    val millis = date.time
    return millis
}



