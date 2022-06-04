package com.example.redminecapstoneproject.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.redminecapstoneproject.R
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentProfileBinding
import com.example.redminecapstoneproject.helper.HelperBloodDonors
import com.example.redminecapstoneproject.helper.HelperDate
import com.example.redminecapstoneproject.ui.home.HomeActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginActivity
import com.example.redminecapstoneproject.ui.loginsignup.LoginSignupViewModel
import com.example.redminecapstoneproject.ui.mydonorreq.MyDonorReqActivity
import com.example.redminecapstoneproject.ui.testing.DonorDataRoom
import com.example.redminecapstoneproject.ui.testing.RegisAccountDataRoom
import java.time.LocalDate

class ProfileFragment : Fragment(), View.OnFocusChangeListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userAccountData: RegisAccountDataRoom? = null
    private var userDonorData: DonorDataRoom? = null

    override fun onStart() {
        super.onStart()
        if(isAdded){
            val activity= activity as HomeActivity
            activity.state= HomeActivity.PROFILE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAction()

        val loginSignupViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[LoginSignupViewModel::class.java]


        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        loginSignupViewModel.firebaseUser.observe(requireActivity()) { fu ->
            if (fu == null) {
                if (isAdded) startActivity(intent)
            }
        }

        loginSignupViewModel.getUserDonorDataDb().observe(requireActivity()) {
            if (it != null) {
                userDonorData = it
                if (activity != null) setData(userDonorData, requireActivity())

            }
        }

        //userAccountData = loginSignupViewModel.userAccountData.value!!
        if (isAdded) {
            setData(userDonorData, requireActivity())
        }


        loginSignupViewModel.getUserAccountDataDb().observe(requireActivity()) {
            if (it != null) {
                val data =
                    RegisAccountDataRoom(it.uid, it.isVerified, it.name, it.email, it.otpCode)
                userAccountData = data
                if (isAdded) {
                    setData(userAccountData, requireActivity())
                }
            }

        }
    }

    @SuppressLint("StringFormatMatches")
    private fun setData(data: Any?, c: Context) {
        if (data != null) {
            when (data) {
                is RegisAccountDataRoom -> {
                    binding.tvName.text = data.name
                    binding.tvEmail.text = (data.email).toString()

                }
                is DonorDataRoom -> {
                    data.gender?.let { setAvatar(it) }
                    val mLastBloodDOnation =
                        if (data.lastDonateDate != null) HelperDate.stringToDate(data.lastDonateDate!!) else null
                    if (mLastBloodDOnation != null && isAdded) {
                        binding.lastBloodDonation.text = getString(
                            R.string.date_format,
                            HelperDate.monthToString(mLastBloodDOnation.month, requireActivity()),
                            mLastBloodDOnation.dayOfMonth,
                            mLastBloodDOnation.year
                        )

                        val x: LocalDate = HelperDate.canDonateAgain(mLastBloodDOnation)
                        binding.canDonateAgain.text = getString(
                            R.string.date_format,
                            HelperDate.monthToString(x.month, requireActivity()),
                            x.dayOfMonth,
                            x.year
                        )
                    } else {
                        binding.lastBloodDonation.text = "--"
                        binding.canDonateAgain.text = "--"
                    }
                    binding.tvVerifyState.text =
                        if ((data.isVerified)) c.resources.getString(R.string.verified_account) else getString(
                            R.string.unverified_account
                        )
                    binding.tvBloodType.text =
                        HelperBloodDonors.toBloodType(data.bloodType, data.rhesus)
                }
            }
        }
    }

    private fun setAvatar(data: String) {
        val x: Int =
            if (data == "male") R.drawable.img_profile_placeholder_male else R.drawable.img_profile_placeholder_female
        Glide.with(this)
            .load(x)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(binding.imgProfile)
    }

    private fun setAction() {
        val loginSignupViewModel = ViewModelProvider(
            requireActivity(),
            RepoViewModelFactory.getInstance(requireActivity())
        )[LoginSignupViewModel::class.java]
        binding.cvUserProfile.setOnClickListener {
            startActivity(Intent(activity, UserDetailActivity::class.java))
        }

        binding.cvDonorDetail.setOnClickListener {
            startActivity(Intent(activity, DonorDetailActivity::class.java))
        }

        binding.cvMyDonorReq.setOnClickListener {
            startActivity(Intent(activity, MyDonorReqActivity::class.java))
        }

        binding.cvLanguageSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.cvContactUs.setOnClickListener {

        }

        binding.cvAboutRedmine.setOnClickListener {

        }

        binding.cvSignOut.setOnClickListener {
            showConfirmDialog(loginSignupViewModel)
        }

    }

    private fun showConfirmDialog(vm: LoginSignupViewModel) {
        val builder = AlertDialog.Builder(requireActivity())
        val mConfirmDialog = builder.create()
        builder.setTitle(getString(R.string.sign_out))
        builder.setMessage(getString(R.string.sure_sign_out))
        builder.create()

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            vm.signOut()
        }

        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            mConfirmDialog.cancel()
        }
        builder.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object;


    override fun onFocusChange(p0: View?, p1: Boolean) {
        false
    }
}