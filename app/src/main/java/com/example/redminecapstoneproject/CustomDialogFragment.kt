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
import com.example.redminecapstoneproject.helper.helperUserDetail
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqActivity
import com.example.redminecapstoneproject.ui.createdonorreq.CreateDonorReqViewModel
import com.example.redminecapstoneproject.ui.donordata.DonorDataActivity
import com.example.redminecapstoneproject.ui.donordata.DonorDataViewModel
import com.example.redminecapstoneproject.ui.profile.UserDetailActivity
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel
import com.example.redminecapstoneproject.ui.testing.City
import com.example.redminecapstoneproject.ui.testing.Province
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

    private var mListItemsProvince = mutableListOf<Province>()
    private var mListItemsCity = mutableListOf<City>()

    var tempProvID = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userDetailViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[UserDetailViewModel::class.java]

        /*userDetailViewModel.provinces.observe(requireActivity()) {
            listItemsProvince = it
            if (title == "Select Province") {
                if (userDetailViewModel.searchProvinceQuery != null) {
                    sv.setQuery(userDetailViewModel.searchProvinceQuery, false)
                    setSearcedData(userDetailViewModel.searchProvinceQuery!!, userDetailViewModel)
                } else {
                    setAlertDialogAdapter(title, listItemsProvince)
                }
            }
        }*/

        userDetailViewModel.mProvince.observe(requireActivity()) {
            //Log.d("DONATION","prov "+it.toString())
            if (it != null) {

                if (title == "Select Province") {
                    mListItemsProvince = it.toMutableList()
                    if (userDetailViewModel.searchProvinceQuery != null) {
                        sv.setQuery(userDetailViewModel.searchProvinceQuery, false)
                        setSearcedData(
                            userDetailViewModel.searchProvinceQuery!!,
                            userDetailViewModel
                        )
                    } else {
                        setAlertDialogAdapter(title, mListItemsProvince)
                    }
                }
            }
        }

        if (title == "Select City") {
        userDetailViewModel.mCity
            .observe(requireActivity()) {
                Log.d("DATA", "mcity " + it.toString())
                if (it != null) {

                        mListItemsCity = it.toMutableList()
                        if (userDetailViewModel.searchCityQuery != null) {
                            sv.setQuery(userDetailViewModel.searchCityQuery, false)
                            setSearcedData(
                                userDetailViewModel.searchCityQuery!!,
                                userDetailViewModel
                            )
                        } else {
                            if (isAdded) setAlertDialogAdapter(title, mListItemsCity)
                        }
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
        val filteredList = ArrayList<Any>()

        if (title == "Select Province") {
            vm.searchProvinceQuery = query
            for (item in mListItemsProvince) {
                if (item.provName.contains(query, true)) filteredList.add(item)
            }
        } else {
            vm.searchCityQuery = query
            for (item in mListItemsCity) {
                if (item.cityName.contains(query, true)) filteredList.add(item)
            }
        }

        if (isAdded) setAlertDialogAdapter(title, filteredList)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_custom_dialog, null)
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val donorDataViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[DonorDataViewModel::class.java]
        //ViewModelProvider(requireActivity())[DonorDataViewModel::class.java]
        val userDetailViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[UserDetailViewModel::class.java]

        val createDonorReqViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[CreateDonorReqViewModel::class.java]

        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        dialogView = inflater.inflate(R.layout.fragment_custom_dialog, null)
        builder.setView(dialogView)

        mListItemsCity.clear()

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
            is UserDetailActivity -> {
                //to do
                if (title == "Select City") {
                    userDetailViewModel._newUserData.value?.let {
                        it.province?.let { it1 ->
                            userDetailViewModel.getCities(
                                helperUserDetail.getProvinceID(it1)
                            )
                        }
                    }
                }
            }
            is DonorDataActivity -> {
                if (title == "Select City") {
                    Log.d("TAG", "dialog city " + donorDataViewModel.donorData.province)
                    if (donorDataViewModel.donorData.province == null) {
                        tvWarning.visibility = View.VISIBLE
                        rv.visibility = View.GONE
                    } else if (donorDataViewModel.donorData.province != null) {
                        tvWarning.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                        userDetailViewModel.getCities(
                            helperUserDetail.getProvinceID(donorDataViewModel.donorData.province.toString())
                        )

                    }
                }
            }
            is CreateDonorReqActivity -> {
                if (title == "Select City") {
                    if (createDonorReqViewModel.donorReq.province == null) {
                        tvWarning.visibility = View.VISIBLE
                        rv.visibility = View.GONE
                    } else if (createDonorReqViewModel.donorReq.province != null) {
                        tvWarning.visibility = View.GONE
                        rv.visibility = View.VISIBLE
                        userDetailViewModel.getCities(
                            helperUserDetail.getProvinceID(createDonorReqViewModel.donorReq.province.toString())
                        )
                    }
                }
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
        items: List<Any>?

    ) {
        var stringItems = ArrayList<String>()
        val userDetailViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[UserDetailViewModel::class.java]

        val donorDataViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[DonorDataViewModel::class.java]

        val createDonorReqViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[CreateDonorReqViewModel::class.java]


        val mAdapter = AlertDialogAdapter(items)
        rv.adapter = mAdapter

        mAdapter.setOnItemClickCallback(object : AlertDialogAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Any) {
                if (data is Province) {
                    val tvCity: TextView = requireActivity().findViewById(R.id.tv_city)


                    when (activity) {
                        is DonorDataActivity -> {
                            tvCity.apply {
                                if (helperUserDetail.toProvNameID(
                                        data.provName,
                                        data.provId.toString()
                                    ) != donorDataViewModel.donorData.province
                                ) {
                                    donorDataViewModel.donorData.city = null
                                    text = "City"
                                    alpha = 0.6F
                                    donorDataViewModel.donorData.province =
                                        helperUserDetail.toProvNameID(
                                            data.provName,
                                            data.provId.toString()
                                        )
                                }
                            }

                            userDetailViewModel.getCities(
                                helperUserDetail.getProvinceID(donorDataViewModel.donorData.province.toString())
                            )
                        }
                        is UserDetailActivity -> {
                            userDetailViewModel.updateDonorDataRoom(
                                Pair(
                                    helperUserDetail.toProvNameID(
                                        data.provName,
                                        data.provId.toString()
                                    ), "province"
                                )
                            )
                            //userDetailViewModel.provinceId = data.provId
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
                                if (helperUserDetail.toProvNameID(
                                        data.provName,
                                        data.provId.toString()
                                    ) != createDonorReqViewModel.donorReq.province
                                ) {
                                    createDonorReqViewModel.donorReq.city = null
                                    text = "City"
                                    alpha = 0.6F
                                    createDonorReqViewModel.donorReq.province =
                                        helperUserDetail.toProvNameID(
                                            data.provName,
                                            data.provId.toString()
                                        )
                                }
                            }

                            userDetailViewModel.getCities(
                                helperUserDetail.getProvinceID(createDonorReqViewModel.donorReq.province.toString())
                            )
                        }
                    }


                    val tv: TextView = requireActivity().findViewById(R.id.tv_province)
                    tv.apply {
                        text = data.provName
                        alpha = 1F
                    }

                    dismiss()
                } else if (data is City) {
                    when (activity) {
                        is DonorDataActivity -> {
                            donorDataViewModel.donorData.city = data.cityName
                        }
                        is UserDetailActivity -> {
                            userDetailViewModel.updateDonorDataRoom(Pair(data.cityName, "city"))
                        }
                        is CreateDonorReqActivity -> {
                            createDonorReqViewModel.donorReq.city = data.cityName
                        }
                    }

                    val tv: TextView = requireActivity().findViewById(R.id.tv_city)
                    tv.apply {
                        text = data.cityName
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