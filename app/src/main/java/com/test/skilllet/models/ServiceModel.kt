package com.test.skilllet.models

import com.test.skilllet.serviceprovider.paments.PaymentRequest
import com.test.skilllet.util.PaymentStatus
import com.test.skilllet.util.ServiceRequest
import java.io.Serializable

class ServiceModel():Serializable {
    var key=""
    var description:String=""
    var name:String=""
    var type:String=""
    var price:String=""
    var userKey:String=""
    var tags=ArrayList<String>()
    var offeringStatus=ServiceRequest.REQUESTED.name
    var rejectionReason=""
    constructor(description:String,name:String,type:String,price:String) : this() {
        this.description=description
        this.name=name
        this.price=price
        this.type=type
    }
}