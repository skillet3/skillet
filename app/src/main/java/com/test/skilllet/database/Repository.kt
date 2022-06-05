package com.test.skilllet.database

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.test.skilllet.models.ServiceModel
import com.test.skilllet.models.ServiceType
import com.test.skilllet.models.User
import com.test.skilllet.models.WorkingServiceModel
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.ViewType


class Repository {
    companion object {
        private val TAG: String = "9272"
         var loggedInUser: User? = null
        private var allCleaningServices: ArrayList<ServiceModel>? = null
        private var allPlumbingServices: ArrayList<ServiceModel>? = null
        private var allElectricianServices: ArrayList<ServiceModel>? = null
        private var allServicesTypes: ArrayList<String>? = null
        private var servicesNameMap = HashMap<String, ArrayList<String>>()
         var allOfferedServices: ArrayList<ServiceModel>? = null
        private val SERVICE_TYPES="ServiceTypes"


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
        var storage: FirebaseStorage? = null
            get() {
                if (field == null) {
                    field = FirebaseStorage.getInstance()
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
                if (field == null) {
                    field = FirebaseAuth.getInstance()
                }
                return field
            }

        public var currentFirebaseUser: FirebaseUser? = null
            get() = mAuth?.currentUser


        fun createUserAccount(
            context: Context,
            user: User,
            updateUI: (user: User, isSuccess: Boolean) -> Unit
        ) {

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
                        updateUI(user, true)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(user, false)
                    }
                }


        }

        private fun registerUser(context: Context, user: User) {
            user.rating = 5.0F
            database?.reference?.child(user.accType)?.child(user.key)?.setValue(user)
                ?.addOnSuccessListener {
                    Toast.makeText(context, "Data Entered successfully", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(context, "Data Couldn't be entered.", Toast.LENGTH_SHORT).show()
                }
        }

        fun loginUser(
            email: String,
            password: String,
            accType: String,
            block: (isSuccess: Boolean) -> Unit
        ) {
            mAuth?.signInWithEmailAndPassword(email, password)?.addOnSuccessListener {
                if (email.equals("skillskillet3@gmail.com")) {
                    block(true)
                } else if (!currentFirebaseUser?.isEmailVerified!!) {
                    block(false)
                    mAuth?.signOut()
                } else {
                    getUserInfo(email, accType)
                    {isSuccess: Boolean,user:User?->
                       if(isSuccess&&user!=null){
                           loggedInUser=user;
                           registerToken()
                       }else{
                           removeAuthUser()
                            mAuth?.signOut()
                       }
                        block(isSuccess)
                    }
                }
            }?.addOnFailureListener {
                block(false)
            }
        }

        private fun getUserInfo(
            email: String,
            accType: String,
            block: (isSuccess: Boolean,user:User?) -> Unit
        ) {
            var mail=email;
            if(email.contains("@")){
                mail=(email.substring(0, email.indexOf("@")));
            }
            database?.getReference(accType)?.child(mail)
                ?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            //loggedInUser = snapshot.getValue(User::class.java)
                            block(true,snapshot.getValue(User::class.java))
                        } else {
//                            removeAuthUser()
//                            mAuth?.signOut()
                            block(false,null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(false,null)
                    }
                })
        }

        fun getAllCleaningServices(block: (list: ArrayList<ServiceModel>?) -> Unit) {

            allCleaningServices = ArrayList()
            this.servicesRef?.child("Cleaning")?.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap: DataSnapshot? ->
                            snap?.getValue(ServiceModel::class.java)
                                ?.let { allCleaningServices?.add(it) }
                        }
                        block(allCleaningServices)
                    } else {
                        block(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    block(null)
                }

            })

        }

        fun getAllPlumbingServices(block: (list: ArrayList<ServiceModel>?) -> Unit) {
            allPlumbingServices = ArrayList()
            this.servicesRef?.child("Plumbing")?.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap: DataSnapshot? ->
                            snap?.getValue(ServiceModel::class.java)
                                ?.let { allPlumbingServices?.add(it) }
                        }
                        block(allPlumbingServices)
                    } else {
                        block(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    block(null)
                }

            })


        }

        fun removeAuthUser(){
            mAuth?.currentUser?.delete()
        }

        fun getAllElectricianServices(block: (list: ArrayList<ServiceModel>?) -> Unit) {

            allElectricianServices = ArrayList()
            this.servicesRef?.child("Electrician")?.addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { snap: DataSnapshot? ->
                            snap?.getValue(ServiceModel::class.java)
                                ?.let { allElectricianServices?.add(it) }
                        }
                        block(allElectricianServices)
                    } else {
                        block(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    block(null)
                }

            })


        }


//        fun addServiceBySP(
//            user: User,
//            serviceModel: ServiceModel,
//            block: (isSuccess: Boolean) -> Unit
//        ) {
//            addServicesRef?.push()?.key?.let {
//
//                addServicesRef?.child(ViewType.SERVICE_PROVIDER.view)?.child(user.key)?.child(it)
//                    ?.setValue(serviceModel)?.addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            addServicesRef?.child(ViewType.CLIENT.view)?.child(serviceModel.type)
//                                ?.child(serviceModel.name)?.child(serviceModel.id)
//                                ?.setValue(serviceModel)?.addOnCompleteListener {
//                                    block(it.isSuccessful);
//                                }
//                        } else {
//                            block(it.isSuccessful)
//                        }
//                    }
//
//
//            }
//
//        }

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


        fun getAllServicesTypes(block: (serviceTypes: ArrayList<String>?) -> Unit) {
            if (allServicesTypes == null) {
                this.servicesRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val arr = ArrayList<String>()
                            for (snap in snapshot.children) {
                                snap.key?.let { arr.add(it) }
                                for (s in snap.children) {
                                    s.key?.let {
                                        if (!servicesNameMap.containsKey(snap.key)) {
                                            servicesNameMap.set(snap!!.key.toString(), ArrayList())
                                        }
                                        var arr: ArrayList<String>? = servicesNameMap.get(snap.key)
                                        arr?.add(s.key.toString())
                                        servicesNameMap.set(
                                            snap!!.key.toString(),
                                            arr!!
                                        )
                                    }
                                }
                            }
                            allServicesTypes = ArrayList(arr)
                            block(allServicesTypes)
                        } else {
                            block(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })
            } else {
                block(allServicesTypes);
            }
        }

        fun getServicesByName(key: String): ArrayList<String>? {
            return servicesNameMap.get(key)
        }

        fun getOfferedServicesBySp(loggedInUser: User?, block: (isSuccess: Boolean) -> Unit) {
            if (allOfferedServices == null) {
                allOfferedServices = ArrayList()
                addServicesRef?.child(ViewType.SERVICE_PROVIDER.view)?.child(loggedInUser!!.key)
                    ?.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                var arr = ArrayList<ServiceModel>()
                                for (snap in snapshot.children) {
                                    arr.add(snap!!.getValue(ServiceModel::class.java)!!)
                                }
                                allOfferedServices = ArrayList(arr)
                                block(true)
                            } else {
                                block(false)
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            block(false)
                        }

                    })
            } else {
                block(true)
            }

        }

        fun getServicesListByClient(
            type: String,
            name: String,
            block: (services: ArrayList<ServiceModel>?) -> Unit
        ) {
            addServicesRef?.child(ViewType.CLIENT.view)?.child(type.lowercase())?.child(name)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var arr = ArrayList<ServiceModel>()
                            for (snap in snapshot.children) {
                                arr.add(snap.getValue(ServiceModel::class.java)!!)
                            }
                            block(arr);
                        } else {
                            block(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })
        }

        fun insertOrUpdateServiceRequest(
            status: RequestStatus,
            serviceModel: ServiceModel,
            block: (isSuccess: Boolean) -> Unit
        ) {

            serviceModel.status = status.status
            var spUser: User? = null
            var clUser: User? = null
            if (RequestStatus.APPROVED.status == status.status ||
                RequestStatus.DECLINE.status == status.status
            ) {
              //  clUser = serviceModel.user
                spUser = loggedInUser
            } else if (RequestStatus.PENDING.status == status.status) {
               // spUser = serviceModel.user
                clUser = loggedInUser
              //  serviceModel.user = clUser
            }
//            serviceRequest?.child(ViewType.SERVICE_PROVIDER.view)?.child(spUser!!.key)
//                ?.child(serviceModel.id)?.setValue(serviceModel)?.addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        serviceModel.user = spUser
//                        serviceRequest?.child(ViewType.CLIENT.view)?.child(clUser!!.key)
//                            ?.child(serviceModel.id)?.setValue(serviceModel)
//                            ?.addOnCompleteListener {
//                                block(it.isSuccessful)
//                            }
//                    } else {
//                        block(false)
//                    }
//                }
        }

        private fun getRequestsHistory(
            view: ViewType,
            block: (list: ArrayList<ServiceModel>?) -> Unit
        ) {
            serviceRequest?.child(view.view)?.child(loggedInUser!!.key)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var allServicesHistory = ArrayList<ServiceModel>()
                            snapshot.children?.forEach { snap: DataSnapshot ->
                                allServicesHistory?.add(snap.getValue(ServiceModel::class.java)!!)
                            }
                            block(allServicesHistory)
                        } else {
                            block(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })
        }

        fun getServices(
            view: ViewType,
            status: RequestStatus,
            block: (list: ArrayList<ServiceModel>?) -> Unit
        ) {
            var servicesList: ArrayList<ServiceModel>? = null

            getRequestsHistory(view) { list: ArrayList<ServiceModel>? ->
                servicesList = ArrayList()
                list?.forEach {
                    if (it.status.equals(status.status)) {
                        servicesList?.add(it)
                    }
                }
                block(servicesList)
            }
        }

        fun getServicesList(block: (ArrayList<ServiceModel>?) -> Unit) {
            var list = ArrayList<ServiceModel>()
            getAllCleaningServices { cleaningServices ->
                cleaningServices?.let { list.addAll(it) }
                getAllPlumbingServices { plumbingServices ->
                    plumbingServices?.let { list.addAll(it) }
                    getAllElectricianServices { electricianServices ->
                        electricianServices?.let { list.addAll(it) }
                        if(list.isEmpty()){
                            block(null)
                        }else{
                            block(list)
                        }
                    }
                }
            }
        }

        fun addServiceByAdmin(serviceModel: ServiceModel, block: (isSuccess: Boolean) -> Unit) {

            this.servicesRef?.child(serviceModel.type)?.child(serviceModel.name)
                ?.setValue(serviceModel)?.addOnCompleteListener {
                    block(it.isSuccessful)
                }
        }

        fun getAccountsList(block: (ArrayList<User>?) -> Unit) {
            database?.reference?.child("Client")
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var list = ArrayList<User>()
                        snapshot.children.forEach { shot ->

                            var user = shot.getValue(User::class.java)
                            if (user != null) {
                                list.add(user)
                            }

                        }
                        database?.reference?.child("ServiceProvider")
                            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.children.forEach { shot ->
                                        var user = shot.getValue(User::class.java)
                                        if (user != null) {
                                            list.add(user)
                                        }

                                    }
                                    block(list)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    block(null)
                                }
                            })


                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(null)
                    }

                })
        }

        fun deleteService(service: ServiceModel, block: (Boolean) -> Unit) {
            this.servicesRef?.child(service.type)?.child(service.name)?.removeValue()
                ?.addOnCompleteListener {
                    block(it.isSuccessful)
                }
        }

        fun deleteAccount(accountDetails: User, function: (Boolean) -> Unit) {
            database?.reference?.child(accountDetails.accType)?.child(accountDetails?.key)
                ?.removeValue()?.addOnCompleteListener {

                    function(it.isSuccessful)
                }
        }

        fun updateUserProfile(uri: Uri?, user: User?, function: (b: Boolean) -> Unit) {
            if (uri != null) {
                uploadImage(uri, user?.key) { url: String? ->
                    if (url != null) {
                        user?.url = url
                    }
                    uploadUserProfile(user) { b: Boolean ->
                        function(b)
                    }
                }
            } else {
                uploadUserProfile(user) {
                    function(it)
                }

            }
        }

        private fun uploadUserProfile(user: User?, function: (Boolean) -> Unit) {
            database?.reference?.child(user?.accType!!)?.child(user.key)
                ?.setValue(user)?.addOnCompleteListener {
                    loggedInUser = user
                    function(it.isSuccessful)
                }
        }

        private fun uploadImage(uri: Uri?, key: String?, function: (String?) -> Unit) {
            val ref = storage?.getReference(key!!)
            val uploadTask = ref?.putFile(uri!!)
            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    function(downloadUri.toString())
                } else {
                    function(null)
                }

            }


        }
        fun registerToken(){
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                loggedInUser?.token=token
                loggedInUser?.accType?.let { accType ->
                    loggedInUser?.key?.let { key -> database?.reference?.child(accType)?.child(key)?.setValue(loggedInUser) }
                }

            })
        }

        fun addOrUpdateService(service: ServiceModel, function: (b:Boolean) -> Unit) {
            if(service.key.isEmpty()){
                servicesRef?.push()?.key?.let {
                    service.key=it
                }
            }
            servicesRef?.child(service.key)?.setValue(service)
                ?.addOnCompleteListener {
                    function(it.isSuccessful)
                }
        }

        fun getListOfServiceTypes(callBack:(list:ArrayList<ServiceType>?)->Unit){

            database?.reference?.child(SERVICE_TYPES)?.addValueEventListener(
                object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list=ArrayList<ServiceType>()
                        if(snapshot.exists()){
                            for(snap in snapshot.children){
                                snap.getValue(ServiceType::class.java)?.let { list.add(it) }
                            }
                            callBack(list)
                        }else{
                            callBack(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                }
            )
        }

        fun addNewServiceType(name:String,function: (b: Boolean) -> Unit){
            database?.reference?.child(SERVICE_TYPES)?.child(name)?.push()?.key?.let{
                var type= ServiceType(it,name)
                database?.reference?.child(SERVICE_TYPES)?.child(type.name)?.setValue(type)
                    ?.addOnCompleteListener {
                        function(it.isSuccessful)
                    }
            }
        }

        fun getServicesOffered(
            offeringStatus: String,
            userKey: String,
            callBack: (ArrayList<ServiceModel>?) -> Unit
        ) {
            servicesRef?.orderByChild("userKey")?.equalTo(userKey)
                ?.addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                       if(snapshot.exists()){
                           var list=ArrayList<ServiceModel>()
                           for(snap in snapshot.children){
                               snap.getValue(ServiceModel::class.java)?.let {
                                   if(offeringStatus.equals(it.offeringStatus)){
                                       list.add(it)
                                   }
                               }
                           }
                           callBack(list)
                       }else{
                           callBack(null)
                       }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                })
        }

        fun getServicesByType(
            offeringStatus: String,
            callBack: (ArrayList<WorkingServiceModel>?) -> Unit
        ) {
            servicesRef?.orderByChild("offeringStatus")?.equalTo(offeringStatus)
                ?.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            var list=ArrayList<WorkingServiceModel>()
                            for(snap in snapshot.children){
                                snap.getValue(ServiceModel::class.java)?.let {
                                    val service=it;
                                    getUserInfo(service.userKey,"ServiceProvider"){isSuccess:Boolean,
                                        u:User?->
                                        u?.let { it1 ->
                                            list.add(WorkingServiceModel(service,
                                                it1
                                            ))
                                        }
                                        callBack(list)
                                    }
                                }
                            }

                        }else{
                            callBack(null)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                })
        }
    }
}


