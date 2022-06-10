package com.test.skilllet.models

import com.test.skilllet.util.OfferingStatus
import java.io.Serializable

class ServiceModel():Serializable {
    var key=""
    var description:String=""
    var name:String=""
    var type:String=""
    var price:String=""
    var userKey:String=""
    var tags=ArrayList<String>()
    var offeringStatus=OfferingStatus.REQUESTED.name
    var rejectionReason=""
    var isAvailable:Boolean=true
    constructor(description:String,name:String,type:String,price:String) : this() {
        this.description=description
        this.name=name
        this.price=price
        this.type=type
    }
}