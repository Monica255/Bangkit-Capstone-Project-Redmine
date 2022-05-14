package com.example.redminecapstoneproject

import android.app.Dialog
import android.content.DialogInterface
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
import com.example.redminecapstoneproject.ui.profile.UserDetailViewModel

class CustomDialogFragment() : DialogFragment() {
    private lateinit var dialogView: View
    private lateinit var title: String
    private lateinit var sv: SearchView
    private lateinit var btClose: Button
    private lateinit var tv: TextView
    private lateinit var rv:RecyclerView
    private var listItemsProvince= listOf<String>()
    private var listItemsCity= listOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDetailViewModel =
            ViewModelProvider(requireActivity())[UserDetailViewModel::class.java]



        userDetailViewModel.provinces.observe(requireActivity()) {
            listItemsProvince=it
            if (title == "Select Province") {
                if(userDetailViewModel.searchProvinceQuery!=null){
                    sv.setQuery(userDetailViewModel.searchProvinceQuery,false)
                    setSearcedData(userDetailViewModel.searchProvinceQuery!!,userDetailViewModel)
                }else{
                    setAlertDialogAdapter(title, listItemsProvince, userDetailViewModel)
                }
            }
        }

        userDetailViewModel.cities.observe(requireActivity()) {
            listItemsCity=it
            if (title == "Select City") {
                if(userDetailViewModel.searchCityQuery!=null){
                    sv.setQuery(userDetailViewModel.searchCityQuery,false)
                    setSearcedData(userDetailViewModel.searchCityQuery!!,userDetailViewModel)
                }else{
                    setAlertDialogAdapter(title, listItemsCity, userDetailViewModel)
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
                setSearcedData(s,userDetailViewModel)
                return false
            }
        })


    }

    private fun setSearcedData(query:String,vm:UserDetailViewModel){
        val filteredList = ArrayList<String>()

        if(title=="Select Province"){
            vm.searchProvinceQuery=query
            for (item in listItemsProvince) {
                if (item.contains(query, true)) filteredList.add(item)
            }
        }else{
            vm.searchCityQuery=query
            for (item in listItemsCity) {
                if (item.contains(query, true)) filteredList.add(item)
            }
        }

        setAlertDialogAdapter(title, filteredList, vm)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_custom_dialog, null)
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d("TAG","dismissed")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val userDetailViewModel =
            ViewModelProvider(requireActivity())[UserDetailViewModel::class.java]
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = layoutInflater
        dialogView = inflater.inflate(R.layout.fragment_custom_dialog, null)
        builder.setView(dialogView)

        arguments?.getString("title")?.let {
            title = it
            Log.d("TAG", it)
        }

        sv = dialogView.findViewById(R.id.sv_alert_dialog)
        btClose = dialogView.findViewById(R.id.bt_close)
        tv = dialogView.findViewById(R.id.tv_dialog_title)
        rv= dialogView.findViewById(R.id.rv_alert_dialog)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        rv.layoutManager = layoutManager

        tv.text = title

        return builder.create()

    }

    private fun setAlertDialogAdapter(
        title: String,
        items: List<String>?,
        vm: UserDetailViewModel
    ) {

        val mAdapter = AlertDialogAdapter(items)
        rv.adapter = mAdapter

        mAdapter.setOnItemClickCallback(object : AlertDialogAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                if (title == "Select Province") {
                    vm.updateUserDetail(data,"province")
                    val tv:TextView=requireActivity().findViewById(R.id.tv_province)
                    tv.text=data
                    //setButtonSaveEnable(isDataDifferent())
                    dismiss()
                } else {
                    vm.updateUserDetail(data,"city")
                    val tv:TextView=requireActivity().findViewById(R.id.tv_city)
                    tv.text=data
                    //setButtonSaveEnable(isDataDifferent())
                    dismiss()
                }

            }
        })
    }


    companion object {


    }
}