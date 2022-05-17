package com.example.redminecapstoneproject.ui.donordata

import androidx.lifecycle.ViewModel
import com.example.redminecapstoneproject.ui.testing.DonorData
import java.time.LocalDate

class DonorDataViewModel:ViewModel() {
    var donorData=DonorData()
    var tempLastDonateDate:LocalDate?=null
    var tempRecoveryDate:LocalDate?=null


}