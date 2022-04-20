package com.test.skilllet.models

class ServiceModel() {

    var description:String=""
    var name:String=""
    var type:String=""
    var price:String=""
    var id:String=""
    var user:User?=null
    var status:String=""

    constructor(description:String,name:String,type:String,price:String,id:String) : this() {
        this.description=description
        this.name=name
        this.price=price
        this.type=type
    }
}