package com.test.skilllet.models

import java.io.Serializable


class User():Serializable {
    var name:String=""
    var email:String=""
    var password:String=""
    var accType:String=""
    var phNumber:String=""
    var address: String =""
    var rating:Float=0.0f
    var url=""
    var token=""
    var totalFeedbacks:Int=1
    var isAvailable:Boolean=true
    var key:String=""
    get() {
        return email.substring(0,email.indexOf("@"))
    }
    constructor(name:String="",email:String="",password:String="",accType:String="") : this() {
        this.name=name
        this.email=email
        this.password=password
        this.accType=accType
    }
    companion object{
        var accTypes= arrayListOf<String>("Client","ServiceProvider");
        fun getAccountType(isClient:Boolean):String{
            return if(isClient){
                accTypes[0]
            }else{
                accTypes[1]
            }
        }
    }
}