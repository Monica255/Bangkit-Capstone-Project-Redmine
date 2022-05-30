package com.example.redminecapstoneproject.ui.testing

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import java.time.LocalDate

object test {




}

data class TempAccountData(
    var uid: String?=null,
    var isVerified: Boolean?=null,
    var name: String?=null,
    var email: String?=null,
    var otpCode:String?=null
)

data class TempDonorData(
    var uid: String?=null,
    var isVerified: Boolean?=null,
    var gender: String?=null,
    var bloodType: String?=null,
    var rhesus: String?=null,
    var phoneNumber: String?=null,
    var province: String?=null,
    var city: String?=null,
    var haveDonated: Boolean?=null,
    var hadCovid: Boolean?=null,
    var lastDonateDate: String? = null,
    var recoveryDate: String? = null,
)

@Entity
@Parcelize
data class RegisAccountDataRoom(
    @PrimaryKey
    var uid:String,
    var isVerified: Boolean?=null,
    var name: String?,
    var email: String?,
    var otpCode:String?=null
):Parcelable


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
@Parcelize
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
):Parcelable


@Parcelize
data class DonorRequest(
    var uid: String?=null,
    var patientName:String?=null,
    var numberOfBloodBag:Int?=null,
    var bloodType: String?=null,
    var rhesus: String?=null,
    var province: String?=null,
    var city: String?=null,
    var hospitalName:String?=null,
    var description:String?=null,
    var contactName:String?=null,
    var phoneNumber: String?=null,
    var time:String?=null,
    var timestamp: Long?=null
):Parcelable

@Parcelize
data class BloodDonors(
    var uid:String?=null,
    var name:String?=null,
    var province: String?=null,
    var city: String?=null,
    var gender:String?=null,
    var bloodType: String?=null,
    var rhesus: String?=null,
    var phoneNumber: String?=null
):Parcelable

data class ProvinceResponse(
    val success:Boolean,
    val data:List<Province>
)

data class CityResponse(
    val success:Boolean,
    val data:List<City>
)

data class Province(
    @SerializedName("prov_id")
    val provId:Int,
    @SerializedName("prov_name")
    val provName:String
)

data class City(
    @SerializedName("nama_provinsi")
    val provinceName:String,
    @SerializedName("nama_kota")
    val cityName:String
)

@Entity
data class Faq(
    @PrimaryKey
    val faq_id:String="",
    val question:String="",
    val answer:String="",
    var expand:Boolean=false
)
