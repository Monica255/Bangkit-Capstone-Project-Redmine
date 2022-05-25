package com.example.redminecapstoneproject.helper

import android.content.Context
import android.util.Log
import com.example.redminecapstoneproject.R
import java.time.LocalDate
import java.time.Month

object helperDate {

    fun toMonthFormat(x: String, c:Context): String {
        return when(x){
            "1"-> c.resources.getString(R.string.jan)
            "2"-> c.resources.getString(R.string.feb)
            "3"-> c.resources.getString(R.string.mar)
            "4"-> c.resources.getString(R.string.apr)
            "5"-> c.resources.getString(R.string.may)
            "6"-> c.resources.getString(R.string.jun)
            "7"-> c.resources.getString(R.string.jul)
            "8"-> c.resources.getString(R.string.aug)
            "9"-> c.resources.getString(R.string.sep)
            "10"-> c.resources.getString(R.string.oct)
            "11"-> c.resources.getString(R.string.nov)
            "12" -> c.resources.getString(R.string.des)
            else -> "error"
        }
    }
    fun toNumberMonthFormat(x: Month, c:Context): Int {
        return when(x){
            Month.JANUARY-> 1
            Month.FEBRUARY-> 2
            Month.MARCH-> 3
            Month.APRIL-> 4
            Month.MAY-> 5
            Month.JUNE-> 6
            Month.JULY-> 7
            Month.AUGUST-> 8
            Month.SEPTEMBER-> 9
            Month.OCTOBER-> 10
            Month.NOVEMBER-> 11
            Month.DECEMBER-> 12
            else -> -1
        }
    }

    fun canDonateAgain(lastBloodDonation:LocalDate):LocalDate{
        return LocalDate.of(lastBloodDonation.year,lastBloodDonation.month+3,lastBloodDonation.dayOfMonth)
    }

    fun dateToString(data:LocalDate,c: Context):String{
        return c.resources.getString(
            R.string.date_format,
            toMonthFormat(data.month.toString(), c),
            data.dayOfMonth.toString(),
            data.year.toString()
        )
    }

    fun stringToDate(data:String):LocalDate{
        Log.d("TAG","Date "+data)
        val x=data.split("-")
        return LocalDate.of(
            x[0].toInt(),
            x[1].toInt(),
            x[2].toInt()
        )
    }

}