package com.test.skilllet.models

data class WorkingServiceModel(var service:ServiceModel, var serviceProvider:User?=null,
                               var client:User?=null) {
}