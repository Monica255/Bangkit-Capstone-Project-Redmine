package com.example.redminecapstoneproject.ui.testing

import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

object test {
    fun generateDummyDonationEvents(): List<donationEvents> {
        val newsList = ArrayList<donationEvents>()
        for (i in 1..5) {
            val events = donationEvents(
                "Event name", "Event time"

            )
            newsList.add(events)
        }
        return newsList
    }

    fun generateDummyDonorRequest(): List<donorReq> {
        val newsList = ArrayList<donorReq>()
        for (i in 1..5) {
            val donorReq = donorReq(
                "Req name",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Denpasar"

            )
            newsList.add(donorReq)
        }
        return newsList
    }

    fun generateDummyProvince(): List<String> {
        val newsList = ArrayList<String>()
        for (i in 1..15) {
            val province = "Ini ceritanya province $i"
            newsList.add(province)
        }
        return newsList
    }

    fun generateDummyCity(): List<String> {
        val newsList = ArrayList<String>()
        for (i in 1..15) {
            val province = "Ini ceritanya city $i"
            newsList.add(province)
        }
        return newsList
    }

    fun generateLiveDummyProvince(): MutableLiveData<List<String>> {
        val list =MutableLiveData<List<String>>()
        val newsList = ArrayList<String>()
        for (i in 1..15) {
            val province = "Province name $i"
            newsList.add(province)
        }
        list.value=newsList
        return list
    }

    fun generateLiveDummyCity(province:String): MutableLiveData<List<String>>{
        val list =MutableLiveData<List<String>>()
        val newsList = ArrayList<String>()
        val p=province.replace("Province name","P")

        for (i in 1..15) {
            val province = "$p City name $i"
            newsList.add(province)
        }
        list.value=newsList
        return list
    }


    fun generateDummyUserDetail(): UserDetail {
        return UserDetail(
            true,
            "Dimas Ari Lumintang",
            "arilumintang@gmail.com",
            "87712345678",
            "Province name 1",
            "Province name 1"
        )
    }

    fun generateLiveDummyUserDetail(): MutableLiveData<UserDetail> {
        return MutableLiveData(
            UserDetail(
                true,
                "Dimas Ari Lumintang",
                "arilumintang@gmail.com",
                "87712345678",
                "Province name 1",
                "Province name 1"
            )
        )
    }

    fun generateDummyDonorDetail(): DonorDetail {
        return DonorDetail(
            false,
            "male",
            "a",
            "positive",
            true, false,
            LocalDate.of(2022, 5, 1),
            null
        )
    }


    fun generateLiveDummyUserData(): MutableLiveData<UserData> {
        return MutableLiveData(
            UserData(
                false,
                "male",
                "a",
                "positive",
                "87712345678",
                "Province name 1",
                "P 1 City name 1",
                true, false,
                LocalDate.of(2022, 5, 1),
                null
            )
        )
    }

    fun generateLiveDummyAccountData(): MutableLiveData<AccountData> {
        return MutableLiveData(
            AccountData(
                true,
                "Dimas Ari Lumintang",
                "arilumintang@gmail.com"
            )
        )
    }

}

data class donationEvents(
    var name: String,
    var time: String
)

data class donorReq(
    var name: String,
    var des: String,
    var city: String
)

data class UserDetail(
    val isVerified: Boolean,
    var name: String,
    var email: String,
    var phoneNumber: String,
    var province: String,
    var city: String,
)


data class DonorDetail(
    val isVerified: Boolean,
    val gender: String,
    var bloodType: String,
    var rhesus: String,
    var haveDonated: Boolean,
    var hadCovid: Boolean,
    var lastDonateDate: LocalDate? = null,
    var recoveryDate: LocalDate? = null,

    )


data class UserData(
    val isVerified: Boolean,
    var gender: String,
    var bloodType: String,
    var rhesus: String,
    var phoneNumber: String,
    var province: String,
    var city: String?=null,
    var haveDonated: Boolean,
    var hadCovid: Boolean,
    var lastDonateDate: LocalDate? = null,
    var recoveryDate: LocalDate? = null,
)

data class AccountData(
    var isVerified: Boolean,
    var name: String,
    var email: String,
)

data class RegisAccountData(
    var isVerified: Boolean?,
    var name: String?,
    var email: String?,
)

@Entity
data class RegisAccountDataRoom(
    @PrimaryKey
    var uid:String,
    var isVerified: Boolean?,
    var name: String?,
    var email: String?,
)


data class DonorData(
    var isVerified: Boolean=false,
    var gender: String?=null,
    var bloodType: String?=null,
    var rhesus: String?=null,
    var phoneNumber: String?=null,
    var province: String?=null,
    var city: String?=null,
    var haveDonated: Boolean?=null,
    var hadCovid: Boolean?=null,
    var lastDonateDate: LocalDate? = null,
    var recoveryDate: LocalDate? = null,
    )

@Entity
data class DonorDataRoom(
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uid:String,
    @ColumnInfo(name = "isVerified")
    var isVerified: Boolean=false,
    @ColumnInfo(name = "gender")
    var gender: String?=null,
    @ColumnInfo(name = "bloodType")
    var bloodType: String?=null,
    @ColumnInfo(name = "rhesus")
    var rhesus: String?=null,
    @ColumnInfo(name = "phoneNumber")
    var phoneNumber: String?=null,
    @ColumnInfo(name = "province")
    var province: String?=null,
    @ColumnInfo(name = "city")
    var city: String?=null,
    @ColumnInfo(name = "haveDonated")
    var haveDonated: Boolean?=null,
    @ColumnInfo(name = "hadCovid")
    var hadCovid: Boolean?=null,
    @ColumnInfo(name = "lastDonateDate")
    var lastDonateDate: String? = null,
    @ColumnInfo(name = "recoveryDate")
    var recoveryDate: String? = null,
)

data class DonorRequest(
    var patientName:String?=null,
    var numberOfBloodBag:Int?=null,
    var bloodType: String?=null,
    var rhesus: String?=null,
    var province: String?=null,
    var city: String?=null,
    var hospitalName:String?=null,
    var description:String?=null,
    var contactName:String?=null,
    var phoneNumber: String?=null
)

/*data class DonorReq(
    var patientName:String?=null,
    var numberOfBloodBag:Int?=null,
    var bloodType: String?=null,
    var rhesus: String?=null,
    var province: String?=null,
    var city: String?=null,
    var hospitalName:String?=null,
    var description:String?=null,
    var contactName:String?=null,
    var phoneNumber: String?=null

)*/
