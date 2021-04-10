package com.example.githubuserapplication3.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication3.R
import com.example.githubuserapplication3.adapter.FollowingListAdapter
import com.example.githubuserapplication3.data.ApiService
import com.example.githubuserapplication3.data.DataRetrofit.getData
import com.example.githubuserapplication3.databinding.FragmentFollowingBinding
import com.example.githubuserapplication3.model.FollowingItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    lateinit var binding: FragmentFollowingBinding

    companion object {
        private const val EXTRA_ID = "extra_id"

        @JvmStatic
        fun newInstance(id: String?) =
                FollowingFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_ID, id)
                    }
                }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerView
        binding.followingRecycler.setHasFixedSize(true)
        binding.followingRecycler.layoutManager = LinearLayoutManager(requireContext())

        //Get data from ViewPager
        val id: String? = arguments?.getString(FollowerFragment.EXTRA_ID)

        //Progress Bar
        showLoading(true)

        //Get data from API
        requestData(id.orEmpty())
    }

    private fun requestData(id: String) {
        val followingItem: ArrayList<FollowingItem> = ArrayList()
        val apiService = getData()?.create(ApiService::class.java)
        apiService?.getFollowing(id)
                ?.enqueue(object : Callback<List<FollowingItem>?> {
                    override fun onResponse(
                            call: Call<List<FollowingItem>?>,
                            response: Response<List<FollowingItem>?>
                    ) {

                        showLoading(false)
                        val items = response.body()
                        for (followingItems in items.orEmpty()) {
                            followingItem.add(followingItems)
                        }
                        val adapter = FollowingListAdapter(followingItem)
                        binding.followingRecycler.adapter = adapter
                        binding.followingRecycler.adapter?.notifyDataSetChanged()
                        noFollower(false)

                        if (followingItem.isEmpty()) {
                            noFollower(true)
                        }
                    }

                    override fun onFailure(call: Call<List<FollowingItem>?>, t: Throwable) {
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            Toast.makeText(requireContext(), resources.getString(R.string.check_internet), Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }, 2000)
                    }
                })
    }

    fun showLoading(state: Boolean) {
        if (state) {
            binding.followingProgress.visibility = View.VISIBLE
            binding.followingRecycler.visibility = View.GONE
        } else {
            binding.followingProgress.visibility = View.GONE
            binding.followingRecycler.visibility = View.VISIBLE
        }
    }

    fun noFollower(state: Boolean) {
        if (state) {
            binding.noFollower.visibility = View.VISIBLE
        } else {
            binding.noFollower.visibility = View.INVISIBLE
        }
    }

}