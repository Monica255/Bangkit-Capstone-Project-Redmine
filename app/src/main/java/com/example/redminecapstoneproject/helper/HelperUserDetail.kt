package com.example.redminecapstoneproject.helper

object HelperUserDetail {


    fun getProvinceName(x:String):String{
        val name=x.split("#")
        return name[0]
    }

    fun getProvinceID(x:String):Int{
        val name=x.split("#")
        return name[1].toInt()
    }

    fun toProvNameID(prov:String,id:String):String{
        return "${prov}#${id}"
    }

    fun toValidPhoneNumber(number:String):String{
        return "+62${number}".trim()
    }

    fun toTitleCase(data:String?):String{
        var newString=""
        if(data!=null){
            val x=data.trim().split(" ")
            for (i in x){
                newString= "$newString ${
                    i.lowercase()
                        .replaceFirstChar(Char::titlecase)
                }"
            }
        }
        return newString
    }

}