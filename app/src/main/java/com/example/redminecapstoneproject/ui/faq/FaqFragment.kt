package com.example.redminecapstoneproject.ui.faq

import FaqAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.redminecapstoneproject.RepoViewModelFactory
import com.example.redminecapstoneproject.databinding.FragmentFaqBinding
import com.example.redminecapstoneproject.repository.Repository
import com.example.redminecapstoneproject.ui.home.HomeActivity
import com.example.redminecapstoneproject.ui.home.HomeViewModel
import com.example.redminecapstoneproject.ui.testing.Faq
import java.util.*
import kotlin.collections.ArrayList

class FaqFragment : Fragment() {
    private lateinit var binding: FragmentFaqBinding
    //private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        if(isAdded){
            val activity= activity as HomeActivity
            activity.state= HomeActivity.FAQ
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {

            val faqViewModel = ViewModelProvider(
                requireActivity(),
                RepoViewModelFactory.getInstance(requireActivity())
            )[FaqViewModel::class.java]
            val local=Locale.getDefault().language
            if(local=="en"){
                faqViewModel.getAllFaq(Repository.REF_FAQ_EN)
            }else if(local=="in"){
                faqViewModel.getAllFaq(Repository.REF_FAQ_IN)
            }

            faqViewModel.gelAllFaqDb.observe(requireActivity()){
                setAdapter(it)
            }

            faqViewModel.isLoading.observe(requireActivity()){
                Log.d("EVT","l "+it.toString())
                showLoading(it)
            }

        }

    }


    private fun showLoading(show:Boolean){

        if(show){
            binding.rvList.visibility= View.GONE
            binding.ltJavrvis.visibility= View.VISIBLE

        }else{
            binding.ltJavrvis.visibility= View.GONE
            binding.rvList.visibility= View.VISIBLE
        }
    }

    private fun setAdapter(data:List<Faq>){
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

    /*override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }


}