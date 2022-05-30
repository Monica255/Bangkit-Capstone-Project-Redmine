package com.example.redminecapstoneproject.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.redminecapstoneproject.database.UserRoomDatabase
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.retrofit.ApiConfig
import com.example.redminecapstoneproject.ui.testing.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository(
    private val userRoomDatabase: UserRoomDatabase,
    private val c: Context
) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var mAuth: FirebaseAuth? = null
    private var mDb: FirebaseDatabase? = null
    private var dbRef: DatabaseReference? = null

    private val _message = MutableLiveData<Pair<Boolean, String>>()
    val message: LiveData<Pair<Boolean, String>> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val _otpCode = MutableLiveData<String?>()
    val otpCode: LiveData<String?> = _otpCode

    private val _validAccUsers = MutableLiveData<ArrayList<TempAccountData>>()
    val validAccUsers: LiveData<ArrayList<TempAccountData>> = _validAccUsers

    private val _validDonorDataUsers = MutableLiveData<ArrayList<TempDonorData>>()
    val validDonorDataUsers: LiveData<ArrayList<TempDonorData>> = _validDonorDataUsers

    private val _listProvinces = MutableLiveData<List<Province>>()
    val listProvinces: LiveData<List<Province>> = _listProvinces

    private val _listCities = MutableLiveData<List<City>>()
    val listCities: LiveData<List<City>> = _listCities

    private val _donorReq = MutableLiveData<List<DonorRequest>>()
    val donorReq: LiveData<List<DonorRequest>> = _donorReq

    private val _faq = MutableLiveData<List<Faq>>()
    val faq: LiveData<List<Faq>> = _faq

    //private var _userAccountData = MutableLiveData<RegisAccountData>()
    //var userAccountData: LiveData<RegisAccountData> = _userAccountData

    //private var _userDonorData = MutableLiveData<DonorData?>()
    //var userDonorData: LiveData<DonorData?> = _userDonorData

    init {
        mAuth = FirebaseAuth.getInstance()
        mDb =
            FirebaseDatabase.getInstance(FIREBASE_URL)
        _firebaseUser.value = FirebaseAuth.getInstance().currentUser
        dbRef =
            FirebaseDatabase.getInstance(FIREBASE_URL).reference
    }

    fun deleteDonorReq(id: String) {
        dbRef?.child(REF_DONOR_REQ)?.child(id)?.removeValue()?.addOnCompleteListener {
            if (it.isSuccessful) {
                _message.value = Pair(false, "Donor request is successfully deleted!")
            } else {
                _message.value = Pair(true, "Failed to delete donor request")
            }
        }
    }

    fun getMyDonoReq() {
        _isLoading.value = true
        dbRef?.child(REF_DONOR_REQ)?.orderByChild("uid")
            ?.equalTo(FirebaseAuth.getInstance().currentUser?.uid)
            ?.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    // _donorReq.value= listOf()
                    val donorReqs = ArrayList<DonorRequest>()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val donorReq = i.getValue(DonorRequest::class.java)
                            if (donorReq != null) {
                                donorReqs.add(donorReq)

                            }
                        }
                        val x = donorReqs.toMutableList().sortedByDescending { it.timestamp }
                        _donorReq.value = x
                    } else {
                        _donorReq.value = donorReqs
                    }
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _isLoading.value = false
                    Log.e("firebase", error.message)
                }
            })
    }

    fun getDonorReq() {
        _isLoading.value = true
        dbRef?.child(REF_DONOR_REQ)?.orderByChild("timestamp")
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val donorReqs = ArrayList<DonorRequest>()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val donorReq = i.getValue(DonorRequest::class.java)
                            if (donorReq != null) {
                                donorReqs.add(donorReq)
                            }
                        }
                    }
                    _donorReq.value = donorReqs
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                    _isLoading.value = false
                }
            })
    }

    fun postDonorReq(data: DonorRequest, uid: String) {
        mDb?.getReference(REF_DONOR_REQ)
            ?.child(helperDate.getDonorReqRef(uid, data.timestamp))?.setValue(data)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    _isLoading.value = false
                    _message.value = Pair(false, "Donor request is successfully posted!")
                } else {
                    _isLoading.value = false
                    _message.value = Pair(true, "Failed to post donor request")
                }
            }
    }

    fun getProvinces() {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getProvince()

        api.enqueue(object : Callback<ProvinceResponse> {
            override fun onResponse(
                call: Call<ProvinceResponse>,
                response: Response<ProvinceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listProvinces.value = responseBody.data
                    }
                    //_message.value = responseBody?.message.toString()

                } else {
                    //_message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                _isLoading.value = false
                //_message.value = t.message.toString()
            }

        })
    }

    fun getCities(id: Int) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getCities(id)

        api.enqueue(object : Callback<CityResponse> {
            override fun onResponse(
                call: Call<CityResponse>,
                response: Response<CityResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listCities.value = responseBody.data
                    }
                } else {
                    Log.d("ERROR", "error")
                }
            }

            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }


    fun getAllVerifiedUsers() {
        _isLoading.value = true
        dbRef?.child(REF_USERS)?.orderByChild("verified")?.equalTo(true)
            ?.addValueEventListener(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    val verifiedAccUsers = ArrayList<TempAccountData>()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val verifiedUser = i.getValue(TempAccountData::class.java)
                            if (verifiedUser != null) {
                                verifiedAccUsers.add(verifiedUser)
                            }
                        }
                    }
                    _validAccUsers.value = verifiedAccUsers
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                    _isLoading.value = false

                }
            })
    }


    fun getAllVerifiedDonorDataUsers() {
        _isLoading.value = true
        dbRef?.child(REF_USERS_DATA)?.orderByChild("verified")?.equalTo(true)
            ?.addValueEventListener(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    val verifiedDonorDataUsers = ArrayList<TempDonorData>()

                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val verifiedUser = i.getValue(TempDonorData::class.java)
                            if (verifiedUser != null) {
                                verifiedDonorDataUsers.add(verifiedUser)
                            }
                        }
                    }
                    _validDonorDataUsers.value = verifiedDonorDataUsers
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                }
            })

    }


    fun getOtpCode() {
        var otpCode: HashMap<String, String>?
        _isLoading.value = true
        val mEmail = FirebaseAuth.getInstance().currentUser?.email.toString().replace(".", "")
        dbRef?.child(REF_OTP_CODE)?.child(mEmail)
            ?.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        otpCode = snapshot.value as HashMap<String, String>
                        for (key in otpCode!!.keys) {
                            if (key == "otpCode") {
                                _otpCode.value = otpCode!![key] as String
                                _isLoading.value = false
                                Log.d("TAG", _otpCode.value.toString())
                            }
                        }
                    } else {
                        _isLoading.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                    _isLoading.value = false

                }
            })
    }


    fun registerAccount(email: String, pass: String, name: String) {
        _isLoading.value = true
        mAuth?.createUserWithEmailAndPassword(email, pass)?.addOnCompleteListener {
            Log.d("TAG", "user created")

            if (it.isSuccessful) {
                _isLoading.value = false
                _message.value = Pair(false, "Your account is successfully registered!")
                _firebaseUser.value = FirebaseAuth.getInstance().currentUser
                //Log.d("TAG", "registered "+FirebaseAuth.getInstance().currentUser!!.uid)
                setUserAccountData(name)

            } else {
                _isLoading.value = false
                _message.value = Pair(true, "Failed to register user")
            }
        }


    }


    fun setUserAccountData(
        name: String = "Redminer",
        otpCode: String? = null,
        isVerified: Boolean? = null
    ) {
        val accData = RegisAccountDataRoom(
            FirebaseAuth.getInstance().currentUser?.uid.toString(), isVerified,
            FirebaseAuth.getInstance().currentUser?.displayName ?: name,
            FirebaseAuth.getInstance().currentUser?.email,
            otpCode
        )
        mDb?.getReference(REF_USERS)
            ?.child(FirebaseAuth.getInstance().currentUser!!.uid)?.setValue(accData)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    _isLoading.value = false
                    getUserAccountData()
                } else {
                    _isLoading.value = false
                    val errorUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    FirebaseAuth.getInstance().signOut()
                    errorUser?.delete()
                    //_message.value = Pair(true, "Failed to register user")
                }
            }
    }

    fun signOut() {
        if (_firebaseUser.value != null) {
            if (FirebaseAuth.getInstance().currentUser != null) {
                val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                executorService.execute {
                    userRoomDatabase.userDao().deleteAccountData(uid)
                }
                executorService.execute {
                    userRoomDatabase.userDao().deleteDonorData(uid)
                }

                _firebaseUser.value = null
                FirebaseAuth.getInstance().signOut()
            }
        }


    }

    fun login(email: String, pass: String) {
        _isLoading.value = true
        mAuth?.signInWithEmailAndPassword(email, pass)?.addOnCompleteListener {
            if (it.isSuccessful) {
                _isLoading.value = false
                getUserAccountData()
                getUserDonorData()
                _message.value = Pair(false, "login successfully")
                _firebaseUser.value = FirebaseAuth.getInstance().currentUser
            } else {
                _isLoading.value = false
                _message.value = Pair(true, "Failed to login")
                Log.d("LOADING", _isLoading.value.toString() + " login")
            }
        }

    }

    fun saveUserDonorData(data: DonorDataRoom) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            _isLoading.value = true
                mDb?.getReference(REF_USERS_DATA)
                ?.child(FirebaseAuth.getInstance().currentUser!!.uid)?.setValue(data)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isLoading.value = false
                        executorService.execute { userRoomDatabase.userDao().insertDonorData(data) }
                        _message.value = Pair(false, "Your data is successfully updated!")

                    } else {
                        _isLoading.value = false

                        _message.value = Pair(true, "Failed to update data")

                    }
                }
        }
    }

    fun saveFaq(data: List<Faq>) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            _isLoading.value = true
            mDb?.getReference(REF_FAQ)?.setValue(data)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isLoading.value = false
                        _message.value = Pair(false, "Faq saved")

                    } else {
                        _isLoading.value = false
                        _message.value = Pair(true, "Failed to save faq")

                    }
                }
        }
    }

    fun saveUserAccountData(data: RegisAccountDataRoom) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            _isLoading.value = true
                mDb?.getReference(REF_USERS)
                ?.child(FirebaseAuth.getInstance().currentUser!!.uid)?.setValue(data)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isLoading.value = false
                        executorService.execute {
                            userRoomDatabase.userDao().insertAccountData(data)
                        }
                        _message.value = Pair(false, "Your data is successfully updated!")
                    } else {
                        _isLoading.value = false
                        _message.value = Pair(true, "Failed to update data")

                    }
                }
        }
    }


    fun setUserDonorData(donorData: DonorData) {
        val user = DonorDataRoom(
            FirebaseAuth.getInstance().currentUser?.uid.toString(),
            donorData.isVerified,
            donorData.gender,
            donorData.bloodType,
            donorData.rhesus,
            donorData.phoneNumber,
            donorData.province,
            donorData.city,
            donorData.haveDonated,
            donorData.hadCovid,
            if (donorData.lastDonateDate != null) donorData.lastDonateDate.toString() else null,
            if (donorData.recoveryDate != null) donorData.recoveryDate.toString() else null

        )
        if (FirebaseAuth.getInstance().currentUser != null) {
            _isLoading.value = true
            mDb?.getReference(REF_USERS_DATA)
                ?.child(FirebaseAuth.getInstance().currentUser!!.uid)?.setValue(user)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isLoading.value = false
                        getUserDonorData()
                        //executorService.execute { userRoomDatabase.userDao().insertDonorData(user) }
                        _message.value = Pair(false, "Your user data is successfully saved!")

                    } else {
                        _isLoading.value = false
                        _message.value = Pair(true, "Failed to save data")
                    }
                }
        }

    }

    fun getUserAccountDataDb(): LiveData<RegisAccountDataRoom> {
        //getUserDonorData()
        val data = userRoomDatabase.userDao()
            .getAccountData(FirebaseAuth.getInstance().currentUser?.uid.toString())
        /*if(data.value==null){
            getUserDonorData()
        }*/
        return data
    }

    fun getUserDonorDataDb(): LiveData<DonorDataRoom> {
        //getUserDonorData()
        val data = userRoomDatabase.userDao()
            .getDonorData(FirebaseAuth.getInstance().currentUser?.uid.toString())
        Log.d("TAG", "crn user " + FirebaseAuth.getInstance().currentUser?.uid.toString())
        /*if(data.value==null){
            getUserDonorData()
            Log.d("DATA","rp "+data.value.toString())
        }*/
        return data
    }

    fun getAllFaqDb(): LiveData<List<Faq>> {
        val data = userRoomDatabase.userDao()
            .getAllFaq()
        return data
    }

    fun getAllFaq(){
        //_isLoading.value=true
        mDb?.getReference(REF_FAQ)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val faqList = ArrayList<Faq>()

                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val faq = i.getValue(Faq::class.java)
                        if (faq != null) {
                            faqList.add(faq)
                        }
                    }
                }
                executorService.execute {
                    userRoomDatabase.userDao().insertFaq(
                        faqList
                    )
                }
                //_isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebase", error.message)
            }
        })
    }


    fun getUserAccountData() {
        var userAccountData: HashMap<String, Any>?
        dbRef?.child(REF_USERS)?.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("DATA", "changed")
                    if (snapshot.exists()) {
                        var uid = ""
                        var isVerified = false
                        var name: String? = null
                        var email: String? = null
                        var otpCode: String? = null
                        userAccountData = snapshot.value as HashMap<String, Any>
                        for (key in userAccountData!!.keys) {

                            when (key) {
                                "uid" -> uid =
                                    userAccountData!![key] as String
                                "verified" -> isVerified =
                                    userAccountData!![key] as Boolean
                                "name" -> name = userAccountData!![key] as String
                                "email" -> email =
                                    userAccountData!![key] as String
                                "otpCode" -> otpCode =
                                    userAccountData!![key] as String
                            }
                        }
                        executorService.execute {
                            userRoomDatabase.userDao().insertAccountData(
                                RegisAccountDataRoom(uid, isVerified, name, email, otpCode)
                            )
                        }

                        // Log.d("TAG", "repo " + _userAccountData.value.toString())
                    }//else _userAccountData.value=null
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                }
            })


    }

    fun getUserDonorData() {
        var userDonorData: HashMap<String, Any>?
        //var userDonorData2: DonorData? = null

        dbRef?.child(REF_USERS_DATA)?.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            ?.addValueEventListener(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var uid = ""
                        var isVerified = false
                        var gender: String? = null
                        var bloodType: String? = null
                        var rhesus: String? = null
                        var phoneNumber: String? = null
                        var province: String? = null
                        var city: String? = null
                        var haveDonated: Boolean? = null
                        var hadCovid: Boolean? = null
                        var lastDonateDate: String? = null
                        var recoveryDate: String? = null
                        userDonorData = snapshot.value as HashMap<String, Any>
                        for (key in userDonorData!!.keys) {

                            when (key) {
                                "uid" -> uid =
                                    userDonorData!![key] as String
                                "verified" -> {
                                    isVerified =
                                        userDonorData!![key] as Boolean
                                    Log.d("TAG", "isv " + userDonorData!![key].toString())
                                }

                                "gender" -> gender = userDonorData!![key] as String
                                "bloodType" -> bloodType =
                                    userDonorData!![key] as String
                                "rhesus" -> rhesus =
                                    userDonorData!![key] as String
                                "phoneNumber" -> phoneNumber =
                                    userDonorData!![key] as String
                                "province" -> province =
                                    userDonorData!![key] as String
                                "city" -> city =
                                    userDonorData!![key] as String
                                "haveDonated" -> haveDonated =
                                    userDonorData!![key] as Boolean
                                "hadCovid" -> hadCovid =
                                    userDonorData!![key] as Boolean
                                "lastDonateDate" -> lastDonateDate =
                                        //if (userDonorData!![key] != null) helperDate.stringToDate(
                                    userDonorData!![key] as String
                                //) else null
                                "recoveryDate" -> recoveryDate =
                                        //if (userDonorData!![key] != null) helperDate.stringToDate(
                                    userDonorData!![key] as String
                                //) else null

                            }

                        }
                        val x = DonorDataRoom(
                            uid,
                            isVerified,
                            gender,
                            bloodType,
                            rhesus,
                            phoneNumber,
                            province,
                            city,
                            haveDonated,
                            hadCovid,
                            lastDonateDate,
                            recoveryDate
                        )


                        executorService.execute {
                            userRoomDatabase.userDao().insertDonorData(
                                x
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                }
            })
    }


    fun firebaseAuthWithGoogle(idToken: String) {
        _isLoading.value = true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoading.value = false
                    _message.value = Pair(false, "Login with google account")
                    //setUserAccountData()
                    getUserAccountData()
                    getUserDonorData()
                    _firebaseUser.value = FirebaseAuth.getInstance().currentUser
                } else {
                    _isLoading.value = false
                    _message.value = Pair(true, "Failed to register user")
                    Log.d("TAG", "signInWithCredential:failure")
                }
            }


    }

    companion object {
        const val REF_FAQ="faq"
        const val REF_USERS = "users"
        const val REF_USERS_DATA = "users_data"
        const val REF_DONOR_REQ = "donor_req"
        const val REF_OTP_CODE = "otp_codes"
        const val FIREBASE_URL =
            "https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app"
    }

}

