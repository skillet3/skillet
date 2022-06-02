package com.test.skilllet.models

import com.test.skilllet.serviceprovider.paments.PaymentRequest
import com.test.skilllet.util.PaymentStatus
import java.io.Serializable

class ServiceModel():Serializable {
    var key=""
    var description:String=""
    var name:String=""
    var type:String=""
    var price:String=""
    var id:String=""
    var userKey:String?=null
    var user:User?=null
    var status:String=""
    var paymentStatus=PaymentStatus.NOT_REQUESTED.value
    var tags=ArrayList<String>()
    constructor(description:String,name:String,type:String,price:String,id:String) : this() {
        this.description=description
        this.name=name
        this.price=price
        this.type=type
        this.id=id
    }
}