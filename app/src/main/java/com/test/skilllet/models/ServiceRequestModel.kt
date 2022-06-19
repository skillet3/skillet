package com.test.skilllet.models

import com.test.skilllet.util.PaymentStatus
import com.test.skilllet.util.RequestStatus
import java.io.Serializable

class ServiceRequestModel(
    var serviceKey:String="",
    var serviceProviderKey:String="",
    var clientKey:String="",
    var serviceStatus:String=RequestStatus.PENDING.name,
    var key:String="",
    var paymentStatus:String=PaymentStatus.NOT_REQUESTED.name,
    var feedbackByClient:String="",
    var feedbackByProvider:String="",
    var ratingByProvider:Float=0.0F,
    var ratingByClient:Float=0.0F,
    var secretCode:String?=null,
    var date:String = "",
    var rejectionReason:String=""
    ):Serializable {

}