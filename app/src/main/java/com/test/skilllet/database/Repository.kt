package com.test.skilllet.database

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.User
import com.test.skilllet.util.Constants
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.ViewType
import java.util.*
import java.util.concurrent.atomic.AtomicIntegerArray
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class Repository {
    companion object {
        private val TAG: String = "9272"
        public var loggedInUser:User?=null
        private var allCleaningServices:ArrayList<ServiceModel>?=null
        private var allPlumbingServices:ArrayList<ServiceModel>?=null
        private var allElectricianServices:ArrayList<ServiceModel>?=null
        private var allServicesTypes:ArrayList<String>?=null
        private var servicesNameMap=HashMap<String,ArrayList<String>>()
        public var allOfferedServices:ArrayList<ServiceModel>?=null

        private var allServicesHistory=ArrayList<ServiceModel>()
        private var database: FirebaseDatabase? = null
            get() {
                if (field == null) {
                    field =
                        Firebase.database("https://skillet-54829-default-rtdb.asia-southeast1.firebasedatabase.app/");
                }
                return field
            }
        var servicesRef: DatabaseReference? = null
            get() {
                if (field == null) {
                    field = database?.getReference("services");
                }
                return field
            }
        var addServicesRef: DatabaseReference? = null
            get() {
                if (field == null) {
                    field = database?.getReference("addedServices");
                }
                return field
            }
        var serviceRequest: DatabaseReference? = null
            get() {
                if (field == null) {
                    field = database?.getReference("serviceRequest");
                }
                return field
            }

        /*var clientRef: DatabaseReference? = null
            get() {
                if (field == null) {
                    field = database?.getReference("client");
                }
                return field
            }
        var adminRef: DatabaseReference? = null
            get() {
                if (field == null) {
                    field = database?.getReference("admin");
                }
                return field
            }
        var spRef: DatabaseReference? = null
            get() {
                if (field == null) {
                    field = database?.getReference("serviceProvider");
                }
                return field
            }*/

        public var mAuth: FirebaseAuth? = null
            get() {
                if (field==null) {
                    field = FirebaseAuth.getInstance()
                }
                return field
            }

        public var user: FirebaseUser? = null
            get() = mAuth?.currentUser


        fun createUserAccount(context: Context, user: User, updateUI: (user: User,isSuccess:Boolean) -> Unit) {

            mAuth?.createUserWithEmailAndPassword(user.email, user.password)
                ?.addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(
                            context, "Authentication Succeeded.",
                            Toast.LENGTH_SHORT
                        ).show()
                        registerUser(context, user)
                        updateUI(user,true)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(user,false)
                    }
                }


        }

        private fun registerUser(context: Context, user: User) {
            database?.reference?.child(user.accType)?.child(user.key)?.setValue(user)
                ?.addOnSuccessListener {
                    Toast.makeText(context, "Data Entered successfully", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                Toast.makeText(context, "Data Couldn't be entered.", Toast.LENGTH_SHORT).show()
            }
        }

        fun loginUser(email:String,password:String,accType:String,block:(isSuccess:Boolean)->Unit){
            mAuth?.signInWithEmailAndPassword(email, password)?.addOnSuccessListener {
                if(!user?.isEmailVerified!!){
                    mAuth?.signOut()

                }else {
                    getUserInfo(email, accType){
                        if(it){
                           block(true)
                        }else{
                            block(false)
                        }
                    }
                }
            }?.addOnFailureListener {
                block(false)
            }
        }

        private fun getUserInfo(email: String,accType: String,block: (isSuccess: Boolean) -> Unit) {
            database?.getReference(accType)?.child((email.substring(0,email.indexOf("@"))))?.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        loggedInUser=snapshot.getValue(User::class.java)
                        block(true)
                    }else{
                        mAuth?.signOut()
                        block(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    block(false)
                }
            })
        }
        fun getAllCleaningServices(block:(list:ArrayList<ServiceModel>?)->Unit){
            if(allCleaningServices==null){
                allCleaningServices= ArrayList()
                servicesRef?.child("cleaning")?.addValueEventListener(object:
                    ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            snapshot.children.forEach{ snap: DataSnapshot? ->
                                snap?.getValue(ServiceModel::class.java)
                                    ?.let { allCleaningServices?.add(it) }
                            }
                            block(allCleaningServices)
                        }else{
                            block(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })
            }else{
                block(null)
            }
        }
        fun getAllPlumbingServices(block:(list:ArrayList<ServiceModel>?)->Unit){
            if(allPlumbingServices==null){
                allPlumbingServices= ArrayList()
                servicesRef?.child("plumbing")?.addValueEventListener(object:
                    ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            snapshot.children.forEach{ snap: DataSnapshot? ->
                                snap?.getValue(ServiceModel::class.java)
                                    ?.let { allPlumbingServices?.add(it) }
                            }
                            block(allPlumbingServices)
                        }else{
                            block(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })

            }else{
                block(null)
            }
        }
        fun getAllElectricianServices(block:(list:ArrayList<ServiceModel>?)->Unit){
            if(allElectricianServices==null){
                allElectricianServices= ArrayList()
                servicesRef?.child("electrician")?.addValueEventListener(object:
                    ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            snapshot.children.forEach{ snap: DataSnapshot? ->
                                snap?.getValue(ServiceModel::class.java)
                                    ?.let { allElectricianServices?.add(it) }
                            }
                            block(allElectricianServices)
                        }else{
                            block(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })

            }else{
                block(null)
            }
        }


        fun addServiceBySP(user:User,serviceModel: ServiceModel,block:(isSuccess:Boolean)->Unit){
            addServicesRef?.push()?.key?.let {
                serviceModel.id=it
                serviceModel.user=user
                addServicesRef?.child(ViewType.SERVICE_PROVIDER.view)?.child(user.key)?.child(it)?.
                setValue(serviceModel)?.addOnCompleteListener {
                   if(it.isSuccessful){
                       addServicesRef?.child(ViewType.CLIENT.view)?.child(serviceModel.type)?.
                       child(serviceModel.name)?.child(serviceModel.id)?.setValue(serviceModel)?.
                       addOnCompleteListener {
                           block(it.isSuccessful);
                       }
                   }else {
                       block(it.isSuccessful)
                   }
                }


            }

        }

        //added service
            //spView
                //sp ID
                    //serivce id
                        //service details
            //clientView
                //type
                    //name
                        //service id
                            //service details



        fun getAllServicesTypes(block:(serviceTypes:ArrayList<String>?)->Unit){
            if(allServicesTypes==null){
                servicesRef?.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                       if(snapshot.exists()){
                           val arr=ArrayList<String>()
                           for(snap in snapshot.children){
                               snap.key?.let { arr.add(it) }
                               for(s in snap.children){
                                   s.key?.let {
                                       if(!servicesNameMap.containsKey(snap.key)){
                                        servicesNameMap.set(snap!!.key.toString(), ArrayList())
                                       }
                                       var arr:ArrayList<String>?= servicesNameMap.get(snap.key)
                                       arr?.add(s.key.toString())
                                       servicesNameMap.set(snap!!.key.toString(),
                                           arr!!
                                       )
                                   }
                               }
                           }
                           allServicesTypes= ArrayList(arr)
                           block(allServicesTypes)
                       }else{
                           block(null)
                       }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })
            }else{
                block(allServicesTypes);
            }
        }
        fun getServicesByName(key:String):ArrayList<String>?{
            return  servicesNameMap.get(key)
        }

        fun getOfferedServicesBySp(loggedInUser: User?,block: (isSuccess: Boolean) -> Unit) {
            if(allOfferedServices==null){
                allOfferedServices= ArrayList()
                addServicesRef?.child(ViewType.SERVICE_PROVIDER.view)?.child(loggedInUser!!.key)?.
                addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            var arr=ArrayList<ServiceModel>()
                            for(snap in snapshot.children){
                                arr.add(snap!!.getValue(ServiceModel::class.java)!!)
                            }
                            allOfferedServices= ArrayList(arr)
                            block(true)
                        }else{
                            block(false)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(false)
                    }

                })
            }else{
                block(true)
            }

        }

        fun getServicesListByClient(type:String,name:String,block: (services:ArrayList<ServiceModel>?   ) -> Unit){
            addServicesRef?.child(ViewType.CLIENT.view)?.child(type.lowercase())?.child(name)?.
            addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var arr=ArrayList<ServiceModel>()
                        for(snap in snapshot.children){
                            arr.add(snap.getValue(ServiceModel::class.java)!!)
                        }
                        block(arr);
                    }else{
                        block(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    block(null)
                }

            })
        }

        fun insertServiceRequest(serviceModel: ServiceModel, block: (isSuccess:Boolean) -> Unit) {

            serviceModel.status= RequestStatus.PENDING.status
            var tempUser=serviceModel.user
            serviceModel.user= loggedInUser
            serviceRequest?.child(ViewType.SERVICE_PROVIDER.view)?.child(serviceModel.user!!.key)
             ?.child(serviceModel.id)?.setValue(serviceModel)?.addOnCompleteListener {
                    if(it.isSuccessful){
                        serviceModel.user= tempUser
                        serviceRequest?.child(ViewType.CLIENT.view)?.child(loggedInUser!!.key)
                            ?.child(serviceModel.id)?.setValue(serviceModel)?.addOnCompleteListener {
                                block(it.isSuccessful)
                            }
                    }else{
                        block(false)
                    }
                }
        }

        private fun getRequestsHistory(view:ViewType,block: (list: ArrayList<ServiceModel>?) -> Unit){
            if(allServicesHistory.isEmpty()){
                serviceRequest?.child(view.view)?.child(loggedInUser!!.key)?.
                addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            snapshot.children?.forEach{ snap:DataSnapshot->
                                allServicesHistory?.add(snap.getValue(ServiceModel::class.java)!!)
                            }
                            block(allServicesHistory)
                        }else{
                            block(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })
            }else{
                block(allServicesHistory)
            }
        }
        fun getServices(view:ViewType,status:RequestStatus,block: (list: ArrayList<ServiceModel>?) -> Unit){
             var servicesList:ArrayList<ServiceModel>?=null

            getRequestsHistory(view) { list: ArrayList<ServiceModel>? ->
                    list?.forEach{
                        servicesList= ArrayList()
                        if(it.status.equals(status.status)){
                            servicesList?.add(it)
                        }
                    }
                block(servicesList)
            }
        }

    }


}