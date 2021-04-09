package com.example.consumeapp.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumeapp.data.ApiService
import com.example.consumeapp.data.DataRetrofit.getData
import com.example.consumeapp.adapter.FollowerListAdapter
import com.example.consumeapp.model.FollowerItem
import com.example.consumeapp.R
import com.example.consumeapp.databinding.FragmentFollowerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {

    lateinit var binding: FragmentFollowerBinding

    companion object{
        const val EXTRA_ID = "extra_id"

        @JvmStatic
        fun newInstance(id: String?) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ID, id)
                }
            }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentFollowerBinding.inflate(layoutInflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerView
        binding.followerRecycler.setHasFixedSize(true)
        binding.followerRecycler.layoutManager = LinearLayoutManager(requireContext())

        //Get ID from ViewPager
        val id:String? = arguments?.getString(EXTRA_ID)

        //visibility
        showLoading(true)

        //Get data from API
        requestData(id.orEmpty())

    }

    private fun requestData(id: String) {
        val followerList: ArrayList<FollowerItem> = ArrayList()
        val apiService = getData()?.create(ApiService::class.java)
        apiService?.getFollower(id)
            ?.enqueue(object : Callback<List<FollowerItem>?> {
                override fun onResponse(
                        call: Call<List<FollowerItem>?>,
                        response: Response<List<FollowerItem>?>
                ) {

                    showLoading(false)
                    val items = response.body()
                    for (followerItem in items.orEmpty()) {
                        followerList.add(followerItem)
                    }

                    val adapter = FollowerListAdapter(followerList)
                    binding.followerRecycler.adapter = adapter
                    binding.followerRecycler.adapter?.notifyDataSetChanged()
                    noFollower(false)

                    if (followerList.isEmpty()) {
                        noFollower(true)
                    }

                }

                override fun onFailure(call: Call<List<FollowerItem>?>, t: Throwable) {
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        Toast.makeText(requireContext(), resources.getString(R.string.check_internet), Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }, 2000)
                }
            })

    }

    fun showLoading(state: Boolean){
        if(state){
            binding.followerProgress.visibility = View.VISIBLE
            binding.followerRecycler.visibility = View.GONE
        }else{
            binding.followerProgress.visibility = View.GONE
            binding.followerRecycler.visibility = View.VISIBLE
        }
    }

    fun noFollower(state: Boolean){
        if (state){
            binding.noFollower.visibility = View.VISIBLE
        }else{
            binding.noFollower.visibility = View.INVISIBLE
        }
    }

}