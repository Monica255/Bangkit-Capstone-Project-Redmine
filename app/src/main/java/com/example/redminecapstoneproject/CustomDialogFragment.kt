package com.example.redminecapstoneproject

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.adapter.AlertDialogAdapter
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqViewModel
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.donordata.DonorDataViewModel
import com.example.redminecapstoneproject.ui.profile.UserDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import java.lang.Exception

class CustomDialogFragment() : DialogFragment() {
    private lateinit var dialogView: View
    private lateinit var title: String
    private lateinit var sv: SearchView
    private lateinit var btClose: Button
    private lateinit var tvTitle: TextView
    private lateinit var tvWarning: TextView
    private lateinit var rv: RecyclerView
    private var listItemsProvince = listOf<String>()
    private var listItemsCity = listOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDetailViewModel =
            ViewModelProvider(requireActivity())[UserDetailViewModel::class.java]



        userDetailViewModel.provinces.observe(requireActivity()) {
            listItemsProvince = it
            if (title == "Select Province") {
                if (userDetailViewModel.searchProvinceQuery != null) {
                    sv.setQuery(userDetailViewModel.searchProvinceQuery, false)
                    setSearcedData(userDetailViewModel.searchProvinceQuery!!, userDetailViewModel)
                } else {
                    setAlertDialogAdapter(title, listItemsProvince)
                }
            }
        }

        userDetailViewModel.cities
            .observe(requireActivity()) {
                listItemsCity = it
                if (title == "Select City") {
                    if (userDetailViewModel.searchCityQuery != null) {
                        sv.setQuery(userDetailViewModel.searchCityQuery, false)
                        setSearcedData(
                            userDetailViewModel.searchCityQuery!!,
                            userDetailViewModel
                        )
                    } else {
                        setAlertDialogAdapter(title, listItemsCity)
                    }
                }
            }





        btClose.setOnClickListener {
            dismiss()
        }

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                setSearcedData(s, userDetailViewModel)
                return false
            }
        })


    }

    private fun setSearcedData(query: String, vm: UserDetailViewModel) {
        val filteredList = ArrayList<String>()

        if (title == "Select Province") {
            vm.searchProvinceQuery = query
            for (item in listItemsProvince) {
                if (item.contains(query, true)) filteredList.add(item)
            }
        } else {
            vm.searchCityQuery = query
            for (item in listItemsCity) {
                if (item.contains(query, true)) filteredList.add(item)
            }
        }

        setAlertDialogAdapter(title, filteredList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_custom_dialog, null)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val donorDataViewModel =
            ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]
        val userDetailViewModel =
            ViewModelProvider(requireActivity())[UserDetailViewModel::class.java]

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        dialogView = inflater.inflate(R.layout.fragment_custom_dialog, null)
        builder.setView(dialogView)

        arguments?.getString("title")?.let {
            title = it
            //Log.d("TAG", it)
        }

        sv = dialogView.findViewById(R.id.sv_alert_dialog)
        btClose = dialogView.findViewById(R.id.bt_close)
        tvTitle = dialogView.findViewById(R.id.tv_dialog_title)
        tvWarning = dialogView.findViewById(R.id.tv_please_select_province)
        rv = dialogView.findViewById(R.id.rv_alert_dialog)

        when (activity) {
            is DonorDataActivity -> {
                if (title == "Select City") {
                    if (donorDataViewModel.donorData.province == null) {
                        tvWarning.visibility = View.VISIBLE
                        rv.visibility = View.GONE
                    } else if (donorDataViewModel.donorData.province != null) {
                        tvWarning.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                        try {
                            userDetailViewModel.getCities(donorDataViewModel.donorData.province!!)

                        } catch (e: Exception) {
                            Log.e("ERROR", "Error message: $e")
                        }
                    }
                }
            }
            is UserDetailActivity -> {
                userDetailViewModel._newUserData.value?.let { userDetailViewModel.getCities(it.province) }
            }
        }


        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        rv.layoutManager = layoutManager

        tvTitle.text = title

        return builder.create()

    }


    private fun setAlertDialogAdapter(
        title: String,
        items: List<String>?

    ) {
        val userDetailViewModel =
            ViewModelProvider(requireActivity())[UserDetailViewModel::class.java]
        val donorDataViewModel =
            ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]
        val createDonorReqViewModel =
            ViewModelProvider(requireActivity())[CreateDonorReqViewModel::class.java]
        val mAdapter = AlertDialogAdapter(items)
        rv.adapter = mAdapter

        mAdapter.setOnItemClickCallback(object : AlertDialogAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                if (title == "Select Province") {
                    val tvCity: TextView = requireActivity().findViewById(R.id.tv_city)


                    when (activity) {
                        is DonorDataActivity -> {
                            tvCity.apply {
                                if (data != donorDataViewModel.donorData.province) {
                                    donorDataViewModel.donorData.city = null
                                    text = "City"
                                    alpha = 0.6F
                                    donorDataViewModel.donorData.province = data
                                }
                            }
                        }
                        is UserDetailActivity -> {
                            userDetailViewModel.updateUserDetail(Pair(data, "province"))
                            tvCity.apply {
                                if (userDetailViewModel.isProvinceDif()) {
                                    Log.d("TAG", userDetailViewModel.isProvinceDif().toString())
                                    text = "City"
                                    alpha = 0.6F
                                } else {
                                    val city = userDetailViewModel.userData.value?.city ?: "City"
                                    text = city
                                    alpha = 1F
                                }

                            }
                        }
                        is CreateDonorReqActivity -> {
                            tvCity.apply {
                                //if (data != createDonorReqViewModel.donorReq.province) {
                                    //createDonorReqViewModel.donorReq.city = null
                                    text = "City"
                                    alpha = 0.6F
                                    //createDonorReqViewModel.donorReq.province = data
                                //}
                            }
                        }
                    }


                    val tv: TextView = requireActivity().findViewById(R.id.tv_province)
                    tv.apply {
                        text = data
                        alpha = 1F
                    }

                    dismiss()
                } else {
                    when (activity) {
                        is DonorDataActivity -> {
                            donorDataViewModel.donorData.city = data
                        }
                        is UserDetailActivity -> {
                            userDetailViewModel.updateUserDetail(Pair(data, "city"))
                        }
                        is CreateDonorReqActivity -> {
                            //createDonorReqViewModel.donorReq.city = data
                        }
                    }

                    val tv: TextView = requireActivity().findViewById(R.id.tv_city)
                    tv.apply {
                        text = data
                        alpha = 1F
                    }
                    dismiss()
                }

            }
        })
    }


    companion object {


    }
}