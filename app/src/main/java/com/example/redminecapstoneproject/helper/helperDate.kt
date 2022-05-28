package com.example.redminecapstoneproject.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.redminecapstoneproject.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.ChronoUnit
import java.util.*

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

    fun getTimeStamp():Long{
        return System.currentTimeMillis()
    }

    fun getDonorReqRef(uid:String): String {
        return "${uid}_${getTimeStamp()}"
    }

    fun getCurrentTime():String{
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)+1
        val day = c.get(Calendar.DAY_OF_MONTH)

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return LocalDateTime.of(year, month, day, hour,minute ).toString()
    }

    @SuppressLint("StringFormatMatches")
    fun toPostTime(date:String, context:Context):String{
        var posted=""
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)+1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val x=date.split("T")
        var y= stringToDate(x[0])

        val todayDate=LocalDate.of(year, month, day)
        var daysBetween=ChronoUnit.DAYS.between( y , todayDate )



        if(x[0]==todayDate.toString()){
            posted=context.resources.getString(R.string.posted,"today at ${x[1]}")
        }else{
            if(daysBetween.toInt()>7){
                posted=context.resources.getString(R.string.posted,"${x[0]} at ${x[1]}")
            }else if(daysBetween.toInt()==1){
                posted=context.resources.getString(R.string.posted," yesterday")+" at ${x[1]}"
            }else{
                posted=context.resources.getString(R.string.posted_days_ago,
                    "${ChronoUnit.DAYS.between( y , todayDate )}")+" at ${x[1]}"
            }

        }
        return  posted

    }

}