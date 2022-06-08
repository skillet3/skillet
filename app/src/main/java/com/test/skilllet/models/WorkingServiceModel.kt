package com.test.skilllet.models

import java.io.Serializable

data class WorkingServiceModel(var service:ServiceModel, var serviceProvider:User?=null,
                               var client:User?=null,
                                var serviceRequest: ServiceRequestModel?=null):Serializable {
}