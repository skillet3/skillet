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
import com.test.skilllet.models.*
import com.test.skilllet.util.OfferingStatus
import com.test.skilllet.util.RequestStatus
import com.test.skilllet.util.ViewType
import java.util.*
import kotlin.collections.ArrayList


class Repository {
    companion object {
        private val TAG: String = "9272"
        var loggedInUser: User? = null
        private val SERVICE_TYPES = "ServiceTypes"


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


        var serviceRequestRef: DatabaseReference? = null
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
                    { isSuccess: Boolean, user: User? ->
                        if (isSuccess && user != null && user.accType == accType) {
                            loggedInUser = user;
                            registerToken()
                            block(true)
                        } else if (isSuccess && user?.accType != accType) {
                            block(false)
                            mAuth?.signOut()
                        } else {

                            removeAuthUser()
                            mAuth?.signOut()
                            block(false)
                        }

                    }
                }
            }?.addOnFailureListener {
                block(false)
            }
        }

        private fun getUserInfo(
            email: String,
            accType: String,
            block: (isSuccess: Boolean, user: User?) -> Unit
        ) {
            var mail = email;
            if (email.contains("@")) {
                mail = (email.substring(0, email.indexOf("@")));
            }
            database?.getReference("Client")?.child(mail)
                ?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            //loggedInUser = snapshot.getValue(User::class.java)
                            block(true, snapshot.getValue(User::class.java))
                        } else {
//                            removeAuthUser()
//                            mAuth?.signOut()

                            database?.getReference("ServiceProvider")?.child(mail)
                                ?.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            //loggedInUser = snapshot.getValue(User::class.java)
                                            block(true, snapshot.getValue(User::class.java))
                                        } else {
//                            removeAuthUser()
//                            mAuth?.signOut()
                                            block(false, null)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        block(false, null)
                                    }
                                })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        block(false, null)
                    }
                })
        }


        fun removeAuthUser() {
            mAuth?.currentUser?.delete()
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

            // serviceModel.status = status.status
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

        }

        private fun getRequestsHistory(
            view: ViewType,
            block: (list: ArrayList<ServiceModel>?) -> Unit
        ) {
            serviceRequestRef?.child(view.view)?.child(loggedInUser!!.key)
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
//                    if (it.status.equals(status.status)) {
//                        servicesList?.add(it)
//                    }
                }
                block(servicesList)
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
            database?.reference?.child(accountDetails.accType)?.child(accountDetails?.key!!)
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

        private fun registerToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                loggedInUser?.token = token
                loggedInUser?.accType?.let { accType ->
                    loggedInUser?.key?.let { key ->
                        database?.reference?.child(accType)?.child(key)?.setValue(loggedInUser)
                    }
                }

            })
        }

        fun addOrUpdateService(service: ServiceModel, function: (b: Boolean) -> Unit) {
            if (service.key.isEmpty()) {
                servicesRef?.push()?.key?.let {
                    service.key = it
                }
            }
            servicesRef?.child(service.key)?.setValue(service)
                ?.addOnCompleteListener {
                    function(it.isSuccessful)
                }
        }

        fun getListOfServiceTypes(callBack: (list: ArrayList<String>?) -> Unit) {

            database?.reference?.child(SERVICE_TYPES)?.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = ArrayList<String>()
                        if (snapshot.exists()) {
                            for (snap in snapshot.children) {
                                snap.getValue(ServiceType::class.java)?.let { list.add(it.name) }
                            }
                            callBack(list)
                        } else {
                            callBack(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                }
            )
        }

        fun addNewServiceType(name: String, function: (b: Boolean) -> Unit) {
            database?.reference?.child(SERVICE_TYPES)?.child(name)?.push()?.key?.let {
                var type = ServiceType(it, name)
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
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var list = ArrayList<ServiceModel>()
                            for (snap in snapshot.children) {
                                snap.getValue(ServiceModel::class.java)?.let {
                                    if (offeringStatus.equals(it.offeringStatus)) {
                                        list.add(it)
                                    }
                                }
                            }
                            callBack(list)
                        } else {
                            callBack(null)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                })
        }

        fun getServicesByOfferingStatus(
            offeringStatus: String,
            callBack: (ArrayList<WorkingServiceModel>?) -> Unit
        ) {
            servicesRef?.orderByChild("offeringStatus")?.equalTo(offeringStatus)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var list = ArrayList<WorkingServiceModel>()
                            for (snap in snapshot.children) {
                                snap.getValue(ServiceModel::class.java)?.let {
                                    val service = it;
                                    getUserInfo(
                                        service.userKey,
                                        "ServiceProvider"
                                    ) { isSuccess: Boolean,
                                        u: User? ->
                                        u?.let { it1 ->
                                            list.add(
                                                WorkingServiceModel(
                                                    service,
                                                    it1
                                                )
                                            )
                                        }
                                        callBack(list)
                                    }
                                }
                            }

                        } else {
                            callBack(null)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                })
        }

        fun getServicesByServiceType(
            type: String,
            callBack: (ArrayList<WorkingServiceModel>?) -> Unit
        ) {
            servicesRef?.orderByChild("type")?.equalTo(type)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var list = ArrayList<WorkingServiceModel>()
                            for (snap in snapshot.children) {
                                snap.getValue(ServiceModel::class.java)?.let {
                                    val service = it;
                                    if (it.isAvailable&&it.offeringStatus==OfferingStatus.OFFERED.name) {


                                        getUserInfo(
                                            service.userKey,
                                            "ServiceProvider"
                                        ) { isSuccess: Boolean,
                                            u: User? ->
                                            u?.let { it1 ->
                                                list.add(
                                                    WorkingServiceModel(
                                                        service,
                                                        it1
                                                    )
                                                )
                                            }
                                            callBack(list)
                                        }
                                    }else{
                                        callBack(list)
                                    }
                                }
                            }

                        } else {
                            callBack(null)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                })
        }

        fun sendRequest(
            workingServiceModel: WorkingServiceModel,
            date: String,
            callBack: (Boolean) -> Unit
        ) {
            var serviceRequest = ServiceRequestModel(
                workingServiceModel.service!!.key,
                workingServiceModel.serviceProvider?.key!!, loggedInUser?.key!!
            )
            serviceRequestRef?.push()?.key?.let {
                serviceRequest.key = it
                serviceRequest.date=date
                serviceRequest.secretCode=getRandomNumber()
                serviceRequestRef?.child(it)?.setValue(serviceRequest)?.addOnCompleteListener {
                    callBack(it.isSuccessful)
                }
            }

        }



        private fun getRandomNumber(): String? {
            val random=Random()
            return (100000+random.nextInt(900000)).toString()

        }

        fun getServiceByClientAndStatus(
            status: String,
            clientKey: String,
            callBack: (ArrayList<WorkingServiceModel>?) -> Unit
        ) {
            serviceRequestRef?.orderByChild("clientKey")?.equalTo(clientKey)
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val list = ArrayList<WorkingServiceModel>()
                            for (snap in snapshot.children) {
                                val serviceRequest = snap.getValue(ServiceRequestModel::class.java)
                                if (serviceRequest?.serviceStatus == status) {


                                    getServiceByServiceKey(serviceRequest!!.serviceKey) { service: ServiceModel? ->
                                        if (service != null) {


                                            getUserInfo(
                                                serviceRequest.serviceProviderKey,
                                                "ServiceProvider"
                                            ) { it: Boolean,
                                                provider: User? ->
                                                if (it) {
                                                    list.add(
                                                        WorkingServiceModel(
                                                            service!!,
                                                            provider,
                                                            null,
                                                            serviceRequest
                                                        )
                                                    )
                                                    callBack(list)
                                                } else {
                                                    callBack(null)
                                                }

                                            }


                                        } else {
                                            callBack(null)
                                        }
                                    }
                                }
                            }
                            if (list.isEmpty()) {
                                callBack(null)
                            }
                        } else {
                            callBack(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                })
        }

        private fun getServiceByServiceKey(key: String, callBack: (ServiceModel?) -> Unit) {
            servicesRef?.child(key)?.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val service = snapshot.getValue(ServiceModel::class.java)
                            callBack(service)
                        } else {
                            callBack(null)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                }
            )
        }

        fun deleteServiceRequest(serviceKey: String?, callBack: (Boolean) -> Unit) {
            serviceRequestRef?.child(serviceKey!!)?.removeValue()?.addOnCompleteListener {
                callBack(it.isSuccessful)
            }

        }

        fun getServiceByProviderAndStatus(
            status: String,
            key: String,
            callBack: (ArrayList<WorkingServiceModel>?) -> Unit
        ) {
            serviceRequestRef?.orderByChild("serviceProviderKey")?.equalTo(key)
                ?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val list = ArrayList<WorkingServiceModel>()
                            for (snap in snapshot.children) {
                                val serviceRequest = snap.getValue(ServiceRequestModel::class.java)
                                if (serviceRequest?.serviceStatus != status) {
                                    continue
                                }
                                getServiceByServiceKey(serviceRequest!!.serviceKey) { service: ServiceModel? ->
                                    if (service != null) {
                                        getUserInfo(
                                            serviceRequest.clientKey,
                                            "Client"
                                        ) { it: Boolean,
                                            provider: User? ->
                                            if (it) {
                                                list.add(
                                                    WorkingServiceModel(
                                                        service!!,
                                                        null,
                                                        provider,
                                                        serviceRequest
                                                    )
                                                )
                                                callBack(list)
                                            } else {
                                                callBack(null)
                                            }
                                        }

                                    } else {
                                        callBack(null)
                                    }
                                }
                            }
                            if (list.isEmpty()) {
                                callBack(null)
                            }
                        } else {
                            callBack(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(null)
                    }

                })
        }

        fun changeServiceRequestStatus(
            service: ServiceRequestModel?,
            status: String,
            callBack: (Boolean) -> Unit
        ) {
            serviceRequestRef?.child(service?.key!!)
                ?.setValue(service)?.addOnCompleteListener {
                    callBack(it.isSuccessful)
                }
        }

        fun updateServiceRequest(
            serviceRequest: ServiceRequestModel,
            callBack: (Boolean) -> Unit
        ) {
            serviceRequestRef?.child(serviceRequest.key!!)?.setValue(serviceRequest)
                ?.addOnCompleteListener {
                    callBack(it.isSuccessful)
                }
        }

        fun updateUser(user: User, callBack: (Boolean) -> Unit) {

            database?.reference?.child(user.accType)?.child(user.key)?.setValue(user)
                ?.addOnCompleteListener {
                    callBack(it.isSuccessful)
                }
        }

        fun setUserAvailability(user: User?, callBack: (Boolean) -> Unit) {
            loggedInUser = user
            updateUser(user!!) {
                if (it) {
                    getServicesOffered(OfferingStatus.OFFERED.name, loggedInUser!!.key) {
                        if (it != null) {
                            val list = it
                            for (service in list!!) {
                                service.isAvailable = loggedInUser!!.isAvailable
                                addOrUpdateService(service) {

                                }
                            }
                            callBack(true)
                        } else {
                            loggedInUser?.isAvailable = !loggedInUser?.isAvailable!!
                            updateUser(user) {
                                callBack(false)
                            }

                        }
                    }
                } else {
                    callBack(false)
                }
            }


        }

        fun isServiceInProcess(workingServiceModel: ServiceModel, callBack: (Boolean) -> Unit) {
            serviceRequestRef?.orderByChild("serviceKey")?.equalTo(workingServiceModel.key)
                ?.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            var isInProcess=false
                            for(snap in snapshot.children){
                                val service: ServiceRequestModel? =snap.getValue(ServiceRequestModel::class.java)
                                if(service?.serviceStatus==RequestStatus.APPROVED.name){
                                    isInProcess=true
                                    break
                                }
                            }
                            callBack(isInProcess)
                        }else{
                            callBack(false)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callBack(false)
                    }
                })

        }

        fun deleteService(key:String,callBack:(it:Boolean)->Unit){
            database?.reference?.child(SERVICE_TYPES)?.child(key)?.removeValue()?.addOnCompleteListener {
                callBack(it.isSuccessful)
            }
        }

        fun getFeedbacksForClients(serviceModel: ServiceModel,callBack: (ArrayList<WorkingServiceModel>?) -> Unit) {
                serviceRequestRef?.orderByChild("serviceKey")?.equalTo(serviceModel.key)
                    ?.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val list=ArrayList<WorkingServiceModel>()
                            if(snapshot.exists()){
                                for(snap in snapshot.children){
                                    val sr=snap.getValue(ServiceRequestModel::class.java) as ServiceRequestModel
                                    if(sr.serviceStatus==RequestStatus.COMPLETED.name){

                                        getUserInfo(sr!!.clientKey,"Client"){it:Boolean,user:User?->
                                            if(it){
                                                list.add(WorkingServiceModel(serviceRequest=sr, client = user))
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


