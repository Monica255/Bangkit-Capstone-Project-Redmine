package com.example.redminecapstoneproject.helper

import android.content.Context
import android.content.res.Resources
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.ui.testing.*

object helperBloodDonors {

    fun toLocation(city: String?, province: String?): String {
        return "${city ?: ""}, ${province ?: ""}"
    }

    fun toValidBloodDonorsList(
        accData: ArrayList<TempAccountData>,
        donorData: ArrayList<TempDonorData>,
        currentUid: String
    ): List<BloodDonors> {
        var counter = 0
        val mList = ArrayList<BloodDonors>()
        for (item in accData) {
            second@ for (item2 in donorData) {
                if (item2.uid == item.uid && item2.haveDonated == false && item2.hadCovid == false && item.uid != currentUid) {
                    val data = BloodDonors(
                        uid = item.uid,
                        name = item.name,
                        province = item2.province,
                        city = item2.city,
                        gender = item2.gender,
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

    fun toBloodType(bloodType: String?, rhesus: String?): String {
        var mRhesus: String = ""
        if (bloodType != null && rhesus != null) {
            mRhesus = if (rhesus == "positive") "+" else if (rhesus == "negative") "-" else ""
        }
        return "$bloodType$mRhesus".uppercase()
    }

    fun toDonorReqId(uid: String, timeStamp: String): String {
        var x = timeStamp.replace("-", "_")
        return "$uid$x"
    }

    fun toBagsFormat(bag: Int): String {
        return if (bag == 1) {
            "$bag bag"
        } else {
            "$bag bags"
        }
    }

    fun setColor(x:String,c: Context):Int{
        var s=0
        if(x==c.resources.getString(R.string.blood_type_compatible)){
            s=c.resources.getColor(R.color.dark_green)
        }else if(x==c.resources.getString(R.string.blood_type_may_be_compatible)){
            s=c.resources.getColor(R.color.dark_blue)
        }else if(x==c.resources.getString(R.string.blood_type_not_compatible)){
            s=c.resources.getColor(R.color.red)
        }
        return s
    }

    fun checkCompatibility(mBloodType: String, recipient: String, c: Context): String {
        val mBloodType = mBloodType.trim().lowercase()
        val recipient = recipient.trim().lowercase()
        var compatibility = ""

        if (mBloodType.contains("-") || mBloodType.contains("+")) {
            when (mBloodType) {
                "o-" -> compatibility = c.resources.getString(R.string.blood_type_compatible)

                "o+" -> if (recipient == "o+" || recipient == "a+" || recipient == "b+" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "a-" -> if (recipient == "a-" || recipient == "a+" || recipient == "ab-" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "a+" -> if (recipient == "a+" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "b-" -> if (recipient == "b-" || recipient == "b+" || recipient == "ab-" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "b+" -> if (recipient == "b+" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "ab-" -> if (recipient == "ab-" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "ab+" -> if (recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)
            }
        } else {
            when (mBloodType) {
                "o" -> compatibility = c.resources.getString(R.string.blood_type_may_be_compatible)

                "a" -> if (recipient == "a-" || recipient == "a+" || recipient == "ab-" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_may_be_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "b" -> if (recipient == "b-" || recipient == "b+" || recipient == "ab-" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_may_be_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)

                "ab" -> if (recipient == "ab-" || recipient == "ab+") {
                    compatibility = c.resources.getString(R.string.blood_type_may_be_compatible)
                } else compatibility = c.resources.getString(R.string.blood_type_not_compatible)


            }
        }
        return compatibility

    }

}