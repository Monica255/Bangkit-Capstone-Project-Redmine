package com.example.redminecapstoneproject.ui.testing

class test {
    fun generateDummyDonationEvents(): List<donationEvents> {
        val newsList = ArrayList<donationEvents>()
        for (i in 1..5) {
            val events = donationEvents(
                "Event name","Event time"

            )
            newsList.add(events)
        }
        return newsList
    }

    fun generateDummyDonorRequest(): List<donorReq> {
        val newsList = ArrayList<donorReq>()
        for (i in 1..5) {
            val donorReq = donorReq(
                "Req name","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.","Denpasar"

            )
            newsList.add(donorReq)
        }
        return newsList
    }

}

data class donationEvents(
    var name:String,
    var time:String
)

data class donorReq(
    var name:String,
    var des:String,
    var city:String
)