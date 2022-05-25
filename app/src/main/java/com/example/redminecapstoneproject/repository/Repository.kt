package com.example.redminecapstoneproject.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.redminecapstoneproject.database.UserRoomDatabase
import com.example.redminecapstoneproject.ui.testing.DonorData
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
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

    //private var _userAccountData = MutableLiveData<RegisAccountData>()
    //var userAccountData: LiveData<RegisAccountData> = _userAccountData

    //private var _userDonorData = MutableLiveData<DonorData?>()
    //var userDonorData: LiveData<DonorData?> = _userDonorData

    init {
        mAuth = FirebaseAuth.getInstance()
        mDb =
            FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app")
        _firebaseUser.value = FirebaseAuth.getInstance().currentUser
        dbRef =
            FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    }

    fun registerAccount(email: String, pass: String, name: String) {
        _isLoading.value = true
        mAuth?.createUserWithEmailAndPassword(email, pass)?.addOnCompleteListener {
            Log.d("TAG", "user created")

            if (it.isSuccessful) {
                _isLoading.value = false
                _message.value = Pair(false, "Your account is successfully registered!")
                _firebaseUser.value=FirebaseAuth.getInstance().currentUser
                //Log.d("TAG", "registered "+FirebaseAuth.getInstance().currentUser!!.uid)
                setUserAccountData(name)

            } else {
                _isLoading.value = false
                _message.value = Pair(true, "Failed to register user")
            }
        }


    }

    fun setUserAccountData(name:String="Redminer"){
        val accData = RegisAccountDataRoom(
            FirebaseAuth.getInstance().currentUser?.uid.toString(),
            false, FirebaseAuth.getInstance().currentUser?.displayName?:name, FirebaseAuth.getInstance().currentUser?.email
        )
        FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(accData)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _isLoading.value = false
                    executorService.execute {
                        userRoomDatabase.userDao().insertAccountData(accData)
                    }

                    //_message.value = Pair(false, "Your account data is successfully saved!")

                } else {
                    _isLoading.value = false
                    val errorUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    FirebaseAuth.getInstance().signOut()
                    errorUser?.delete()
                    //_message.value = Pair(true, "Failed to register user")

                }
            }
    }

    fun signOut(){
        if(_firebaseUser.value!=null){
            Log.d("TAG",FirebaseAuth.getInstance().currentUser?.uid.toString())
            Log.d("TAG",firebaseUser.value.toString())

            if(FirebaseAuth.getInstance().currentUser!=null){
                val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
                executorService.execute {
                    userRoomDatabase.userDao().deleteAccountData(uid)
                }
                executorService.execute {
                    userRoomDatabase.userDao().deleteDonorData(uid)
                }
                
                _firebaseUser.value=null
                FirebaseAuth.getInstance().signOut()
            }
        }


    }

    fun login(email:String,pass:String){
        _isLoading.value = true
        mAuth?.signInWithEmailAndPassword(email,pass)?.addOnCompleteListener {
            if(it.isSuccessful){
                _isLoading.value = false
                getUserAccountData()
                getUserDonorData()
                _message.value = Pair(false, "login successfully")
                _firebaseUser.value=FirebaseAuth.getInstance().currentUser
            }else{
                _isLoading.value = false
                _message.value = Pair(true, "Failed to login")

            }
        }

    }

    fun saveUserDonorData(data:DonorDataRoom){
        if (FirebaseAuth.getInstance().currentUser != null) {
            _isLoading.value = true
            FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users_data")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data)
                .addOnCompleteListener {
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

    fun saveUserAccountData(data:RegisAccountDataRoom){
        if (FirebaseAuth.getInstance().currentUser != null) {
            _isLoading.value = true
            FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isLoading.value = false


                        executorService.execute { userRoomDatabase.userDao().insertAccountData(data) }
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
            FirebaseDatabase.getInstance("https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("users_data")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _isLoading.value = false


                        executorService.execute { userRoomDatabase.userDao().insertDonorData(user) }
                        _message.value = Pair(false, "Your user data is successfully saved!")

                    } else {
                        _isLoading.value = false

                        _message.value = Pair(true, "Failed to save data")

                    }
                }
        }

    }

    fun getUserAccountDataDb(): LiveData<RegisAccountDataRoom> {
        return userRoomDatabase.userDao().getAccountData(FirebaseAuth.getInstance().currentUser?.uid.toString())
    }

    fun getUserDonorDataDb(): LiveData<DonorDataRoom> {
        Log.d("TAG","crn user "+FirebaseAuth.getInstance().currentUser?.uid.toString())
        return userRoomDatabase.userDao().getDonorData(FirebaseAuth.getInstance().currentUser?.uid.toString())
    }

    private fun getUserAccountData() {
        var userAccountData: HashMap<String, Any>?
        dbRef?.child("users")?.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            ?.addValueEventListener(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var uid = ""
                        var isVerified = false
                        var name: String? = null
                        var email: String? = null
                        userAccountData = snapshot.value as HashMap<String, Any>
                        for (key in userAccountData!!.keys) {

                            when (key) {
                                "uid" -> uid =
                                    userAccountData!![key] as String
                                "isVerified" -> isVerified =
                                    userAccountData!![key] as Boolean
                                "name" -> name = userAccountData!![key] as String
                                "email" -> email =
                                    userAccountData!![key] as String
                            }
                        }
                        executorService.execute {
                            userRoomDatabase.userDao().insertAccountData(
                                RegisAccountDataRoom(uid, isVerified, name, email)
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

    private fun getUserDonorData() {
        var userDonorData: HashMap<String, Any>?
        //var userDonorData2: DonorData? = null

        dbRef?.child("users_data")?.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            ?.addValueEventListener(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var uid =""
                        var isVerified= false
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
                                "uid"->uid=
                                    userDonorData!![key] as String
                                "isVerified" -> isVerified =
                                    userDonorData!![key] as Boolean
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

                        executorService.execute {
                            userRoomDatabase.userDao().insertDonorData(
                                DonorDataRoom(
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
                            )
                        }
                        /*_userDonorData.value = DonorData(
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
                        )*/
                        //Log.d("TAG", "repo " + _userDonorData.value.toString())

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("firebase", error.message)
                }
            })


    }


    fun firebaseAuthWithGoogle(idToken: String) {
        _isLoading.value=true
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener {task->
                if (task.isSuccessful) {
                    _isLoading.value=false
                    _message.value = Pair(false, "Your google account is successfully registered!")
                    setUserAccountData()
                    getUserAccountData()
                    getUserDonorData()
                    _firebaseUser.value=FirebaseAuth.getInstance().currentUser
                    Log.d("TAG", "signInWithCredential:success")
                    /*val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()*/
                } else {
                    _isLoading.value=false
                    _message.value = Pair(true, "Failed to register user")
                    // If sign in fails, display a message to the user.
                    Log.d("TAG", "signInWithCredential:failure")
                }
            }


    }

    companion object

}

