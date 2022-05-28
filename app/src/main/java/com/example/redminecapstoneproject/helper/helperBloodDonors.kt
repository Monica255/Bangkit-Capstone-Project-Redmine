package com.example.redminecapstoneproject.helper

import com.example.redminecapstoneproject.ui.testing.*

object helperBloodDonors {

    fun toLocation(city: String?, province: String?): String {
        return "${city ?: ""}, ${province ?: ""}"
    }

    fun toValidBloodDonorsList(
        accData: ArrayList<TempAccountData>,
        donorData: ArrayList<TempDonorData>,
        currentUid:String
    ): List<BloodDonors> {
        var counter = 0
        val mList = ArrayList<BloodDonors>()
        for (item in accData) {
            second@ for (item2 in donorData) {
                if (item2.uid == item.uid && item2.haveDonated == false && item2.hadCovid == false&&item.uid!=currentUid) {
                    val data = BloodDonors(
                        uid = item.uid,
                        name = item.name,
                        province = item2.province,
                        city=item2.city,
                        gender=item2.gender,
                        bloodType = item2.bloodType,
                        rhesus = item2.rhesus,
                        phoneNumber = item2.phoneNumber
                    )

                    mList.add(data)
                    counter++
                    break@second
                }
            }

        }
        return mList

    }

    fun getFistFiveData(data: List<BloodDonors>): List<BloodDonors> {
        val mList = ArrayList<BloodDonors>()
        return if (data.size >= 5) {
            for (item in data) {
                mList.add(item)
            }
            mList
        } else data
    }

    fun toBloodType(bloodType:String?,rhesus:String?):String{
        var mRhesus:String=""
        if(bloodType!=null&&rhesus!=null){
            mRhesus= if(rhesus=="positive")"+" else if(rhesus=="negative") "-" else ""
        }
        return "$bloodType$mRhesus"
    }

}