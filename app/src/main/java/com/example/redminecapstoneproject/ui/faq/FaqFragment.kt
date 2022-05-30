package com.example.redminecapstoneproject.ui.faq

import FaqAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentFaqBinding
import com.example.redminecapstoneproject.ui.HomeActivity
import com.example.redminecapstoneproject.ui.mydonorreq.MyDonorReqViewModel
import com.example.redminecapstoneproject.ui.testing.Faq

class FaqFragment : Fragment() {
    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        if(isAdded){
            val activity= activity as HomeActivity
            activity.state= HomeActivity.FAQ
        }
    }
    private var list = ArrayList<Faq>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            val faqViewModel = ViewModelProvider(
                requireActivity(),
                RepoViewModelFactory.getInstance(requireActivity())
            )[FaqViewModel::class.java]
            faqViewModel.getAllFaq()
            faqViewModel.gelAllFaqDb.observe(requireActivity()){
                setAdapter(it)
            }
            //mAdaper.notifyDataSetChanged()

        }

    }

    fun saveFaq(){
        if(isAdded){
            val faqViewModel = ViewModelProvider(
                requireActivity(),
                RepoViewModelFactory.getInstance(requireActivity())
            )[FaqViewModel::class.java]
            val language1 = Faq(
                "faq_1",
                "Will it hurt when you insert the needle?",
                "Only for a moment. Pinch the fleshy, soft underside of your arm. That pinch is similar to what you will feel when the needle is inserted.",
                false
            )
            val language2 = Faq(
                "faq_2",
                "How long does a blood donation take?",
                "The entire process takes about one hour and 15 minutes; the actual donation of a pint of whole blood unit takes eight to 10 minutes. However, the time varies slightly with each person depending on several factors including the donor’s health history and attendance at the blood drive.",
                false
            )
            val language3 = Faq(
                "faq_3",
                "How long will it take to replenish the pint of blood I donate?",
                "The plasma from your donation is replaced within about 24 hours. Red cells need about four to six weeks for complete replacement. That’s why at least eight weeks are required between whole blood donations.",
                false
            )
            val language4 = Faq(
                "faq_4",
                "Why does the Red Cross ask so many personal questions when I give blood?",
                "The highest priorities of the Red Cross are the safety of the blood supply and our blood donors. Some individuals may be at risk of transferring communicable disease through blood donation due to exposure via travel or other activities or may encounter problems with blood donation due to their health. We ask these questions to ensure that it is safe for patients to receive your blood and to ensure that it is safe for you to donate blood that day.",
                false
            )

            val language5 = Faq(
                "faq_5",
                "Who can donate blood?",
                "In most states, donors must be age 17 or older. Some states allow donation by 16-year-olds with a signed parental consent form. Donors must weigh at least 110 pounds and be in good health. Additional eligibility criteria apply.",
                false
            )

            // add items to list
            list.add(language1)
            list.add(language2)
            list.add(language3)
            list.add(language4)
            list.add(language5)

            faqViewModel.saveFaq(list)
        }
    }

    fun setAdapter(data:List<Faq>){
        if(isAdded){

            if (data.isEmpty()) {
                binding.ltNoData.visibility = View.VISIBLE
                binding.rvList.visibility = View.GONE
            } else {
                binding.ltNoData.visibility = View.GONE
                binding.rvList.visibility = View.VISIBLE
            }
            val layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding.rvList.layoutManager = layoutManager
            val mAdaper = FaqAdapter(data)
            binding.rvList.adapter = mAdaper
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }


}