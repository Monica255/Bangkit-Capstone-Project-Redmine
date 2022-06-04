package com.example.redminecapstoneproject.helper

import android.annotation.SuppressLint
import android.content.Context
import com.example.redminecapstoneproject.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.ChronoUnit
import java.util.*

object HelperDate {

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

    fun monthToString(x: Month, c:Context): String {
        return when(x){
            Month.JANUARY-> c.resources.getString(R.string.jan)
            Month.FEBRUARY-> c.resources.getString(R.string.feb)
            Month.MARCH-> c.resources.getString(R.string.mar)
            Month.APRIL-> c.resources.getString(R.string.apr)
            Month.MAY-> c.resources.getString(R.string.may)
            Month.JUNE-> c.resources.getString(R.string.jun)
            Month.JULY-> c.resources.getString(R.string.jul)
            Month.AUGUST-> c.resources.getString(R.string.aug)
            Month.SEPTEMBER-> c.resources.getString(R.string.sep)
            Month.OCTOBER-> c.resources.getString(R.string.oct)
            Month.NOVEMBER-> c.resources.getString(R.string.nov)
            Month.DECEMBER-> c.resources.getString(R.string.des)
            else -> c.resources.getString(R.string.dont_know)
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

    fun getDonorReqRef(uid:String,timeStamp:Long?): String {
        return "${uid}${timeStamp.toString().replace("-","_")}"
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
        val posted: String
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)+1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val x=date.split("T")
        val y= stringToDate(x[0])

        val todayDate=LocalDate.of(year, month, day)
        val daysBetween=ChronoUnit.DAYS.between( y , todayDate )



        posted = if(x[0]==todayDate.toString()){
            context.resources.getString(R.string.posted,"${context.resources.getString(R.string.today)}","${x[1]}")
        }else{
            when {
                daysBetween.toInt()>7 -> {
                    val r=x[0].split("-")
                    val date=context.resources.getString(R.string.date_format,r[1],r[2],r[0])
                    context.resources.getString(R.string.posted,date,"${x[1]}")
                }
                daysBetween.toInt()==1 -> {
                    context.resources.getString(R.string.posted,"${context.resources.getString(R.string.yesterday)}","${x[1]}")
                }
                else -> {
                    context.resources.getString(R.string.posted_days_ago,
                        "${ChronoUnit.DAYS.between( y , todayDate )}","${x[1]}")
                }
            }

        }
        return  posted

    }

}