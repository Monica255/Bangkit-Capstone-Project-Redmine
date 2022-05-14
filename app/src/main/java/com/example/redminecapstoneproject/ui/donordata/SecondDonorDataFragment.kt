package com.example.redminecapstoneproject.ui.donordata

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.adapter.AlertDialogAdapter
import com.example.redminecapstoneproject.databinding.FragmentSecondDonorDataBinding
import com.example.redminecapstoneproject.helper.helperDate
import com.example.redminecapstoneproject.ui.HomeActivity
import com.example.redminecapstoneproject.ui.testing.test
import www.sanju.motiontoast.MotionToast
import java.time.LocalDate
import java.util.Calendar


class SecondDonorDataFragment : Fragment() {

    private var _binding: FragmentSecondDonorDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogView: View
    private var isProvinceValid = false
        get() {
            field = binding.tvProvince.text.toString().trim() != "Province"
            return field
        }
    private var isCityValid = false
        get() {
            field = binding.tvCity.text.toString().trim() != "City"
            return field
        }
    private var isHaveDonatedValid = false
        get() {
            checkHaveDonated()
            return field
        }
    private var isHadCovidValid = false
        get() {
            checkHadCovid()
            return field
        }
    private var donateDate: LocalDate? = null
    private var recoveryDate: LocalDate? = null


    private var province = ""
        set(value) {
            field = value
            binding.tvProvince.text = value
            binding.tvProvince.alpha = 1F
        }

    private var city = ""
        set(value) {
            field = value
            binding.tvCity.text = value
            binding.tvCity.alpha = 1F
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cvPickDateLastDonate.setOnClickListener {
            showDatePicker("last donate")
        }

        binding.cvPickDateRecovery.setOnClickListener {
            showDatePicker("recovery date")
        }

        binding.cvPickProvince.setOnClickListener {
            val items = test.generateDummyProvince()
            showAlertDialog("Select Province", items)
        }

        binding.cvPickCity.setOnClickListener {
            val test2 = listOf<String>()
            val items = test.generateDummyCity()
            showAlertDialog("Select City", items)
        }

        binding.rbHaveDonateYes.setOnClickListener {
            binding.cvPickDateLastDonate.visibility = View.VISIBLE
        }

        binding.rbHaveDonatedNo.setOnClickListener {
            binding.cvPickDateLastDonate.visibility = View.GONE
        }

        binding.rbHadCovidYes.setOnClickListener {
            binding.cvPickDateRecovery.visibility = View.VISIBLE
        }

        binding.rbHadCovidNo.setOnClickListener {
            binding.cvPickDateRecovery.visibility = View.GONE
        }

        binding.btFinish.setOnClickListener {
            if (isDataValid()) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                if (!isFieldsEmpty()) {
                    MotionToast.Companion.createColorToast(
                        requireActivity(),
                        "Hey careful",
                        "Please enter you data correctly",
                        MotionToast.TOAST_WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(
                            requireActivity(),
                            www.sanju.motiontoast.R.font.helvetica_regular
                        )
                    )
                }
            }

        }
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.tvProvince.text.toString().trim() == "Province"
                && binding.tvCity.text.toString().trim() == "City"
                && binding.rgHaveYouDonated.checkedRadioButtonId == -1
                && binding.rgHaveYouHadCovid.checkedRadioButtonId == -1

    }

    private fun isDataValid(): Boolean {
        Log.d("TAG", isProvinceValid.toString())
        Log.d("TAG", isCityValid.toString())
        Log.d("TAG", isHaveDonatedValid.toString())
        Log.d("TAG", isHadCovidValid.toString())


        return isProvinceValid && isCityValid && isHaveDonatedValid && isHadCovidValid
    }


    private fun checkHaveDonated() {
        val haveDonated = binding.rgHaveYouDonated.checkedRadioButtonId
        val radio: RadioButton = requireView().findViewById(haveDonated)
        if (haveDonated == -1) {
            isHaveDonatedValid = false
        } else {
            when (radio.id) {
                R.id.rb_haveDonate_yes -> {
                    isHaveDonatedValid = donateDate != null
                }
                R.id.rb_haveDonated_no -> {
                    isHaveDonatedValid = true
                }
            }
        }
    }

    private fun checkHadCovid() {
        val hadCovid = binding.rgHaveYouHadCovid.checkedRadioButtonId
        val radio: RadioButton = requireView().findViewById(hadCovid)
        if (hadCovid == -1) {
            isHadCovidValid = false
        } else {
            when (radio.id) {
                R.id.rb_hadCovid_yes -> {
                    isHadCovidValid = recoveryDate != null
                }
                R.id.rb_hadCovid_no -> {
                    isHadCovidValid = true
                }
            }
        }
    }

    private fun showDatePicker(dateFor: String) {
        val c = Calendar.getInstance()
        var showYear = c.get(Calendar.YEAR)
        var showMonth = c.get(Calendar.MONTH)
        var showDay = c.get(Calendar.DAY_OF_MONTH)

        if(dateFor=="last donate"&&donateDate!=null){
            showYear=donateDate?.year!!
            showMonth = helperDate.toNumberMonthFormat(donateDate?.month!!,requireActivity())-1
            showDay = donateDate?.dayOfMonth!!

        }else if(dateFor=="recovery date"&& recoveryDate!=null){
            showYear = recoveryDate?.year!!
            showMonth = helperDate.toNumberMonthFormat(recoveryDate?.month!!,requireActivity())-1
            showDay = recoveryDate?.dayOfMonth!!
        }
        val dpd = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                var newMonth = monthOfYear + 1
                if (dateFor == "last donate") {
                    donateDate = LocalDate.of(year, newMonth, dayOfMonth)
                    binding.tvDateLastDonate.apply {
                        var newMonth = monthOfYear + 1
                        text = getString(
                            R.string.last_blood_donation,
                            helperDate.toMonthFormat(newMonth.toString(), requireActivity()),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        alpha = 1F
                    }
                } else {
                    recoveryDate = LocalDate.of(year, newMonth, dayOfMonth)
                    binding.tvDateRecovery.apply {
                        var newMonth = monthOfYear + 1
                        text = getString(
                            R.string.recovery_date,
                            helperDate.toMonthFormat(newMonth.toString(), requireActivity()),
                            dayOfMonth.toString(),
                            year.toString()
                        )
                        alpha = 1F
                    }
                }


            },
            showYear,
            showMonth,
            showDay
        )
        c.add(Calendar.MONTH, -3)

        dpd.datePicker.minDate = c.timeInMillis

        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun showAlertDialog(title: String, items: List<String>) {
        val builder = AlertDialog.Builder(
            requireActivity()
        )

        val inflater = layoutInflater
        dialogView = inflater.inflate(R.layout.activity_alert_dialog, null)
        builder.setView(dialogView)

        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        val rv: RecyclerView = dialogView.findViewById(R.id.rv_alert_dialog)
        rv.layoutManager = layoutManager

        val tv: TextView = dialogView.findViewById(R.id.tv_dialog_title)
        tv.text = title

        val mAlertDialog = builder.create()
        mAlertDialog.show()

        val tvWarning: TextView = dialogView.findViewById(R.id.tv_please_select_province)
        var sv: SearchView = dialogView.findViewById(R.id.sv_alert_dialog)
        val btClose: Button = dialogView.findViewById(R.id.bt_close)

        btClose.setOnClickListener {
            mAlertDialog.cancel()
        }

        if (title == "Select City" && province == "") {
            rv.visibility = View.GONE
            tvWarning.visibility = View.VISIBLE
        } else {
            setAlertDialogAdapter(title, items, rv, mAlertDialog)
            rv.visibility = View.VISIBLE
            tvWarning.visibility = View.GONE
        }


        if (items.isNotEmpty()) {
            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    val filteredList = ArrayList<String>()

                    for (item in items) {
                        if (item.contains(s, true)) filteredList.add(item)
                    }

                    setAlertDialogAdapter(title, filteredList, rv, mAlertDialog)
                    return false
                }
            })
        }
    }

    private fun setAlertDialogAdapter(
        title: String,
        items: List<String>,
        rv: RecyclerView,
        mAlertDialog: AlertDialog
    ) {
        Log.d("TAG", "set adapter")
        val mAdapter = AlertDialogAdapter(items)
        rv.adapter = mAdapter

        mAdapter.setOnItemClickCallback(object : AlertDialogAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                if (title == "Select Province") {
                    province = data
                    mAlertDialog.cancel()
                } else {
                    city = data
                    mAlertDialog.cancel()
                }

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondDonorDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
    }
}