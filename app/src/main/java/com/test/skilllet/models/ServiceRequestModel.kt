package com.test.skilllet.models

import com.test.skilllet.util.PaymentStatus
import com.test.skilllet.util.RequestStatus

class ServiceRequestModel(
    var serviceKey:String, var serviceProviderKey:String, var clientKey:String,
    var serviceStatus:String=RequestStatus.PENDING.name,
    var paymentStatus:String=PaymentStatus.NOT_REQUESTED.name,
var feedback:String="") {

}