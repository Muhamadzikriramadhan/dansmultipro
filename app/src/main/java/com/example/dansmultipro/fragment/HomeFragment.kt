package com.example.dansmultipro.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dansmultipro.DetailJobActivity
import com.example.dansmultipro.R
import com.example.dansmultipro.adapter.JobsAdapter
import com.example.dansmultipro.databinding.ActivityFacebookBinding
import com.example.dansmultipro.databinding.FragmentHomeBinding
import com.example.dansmultipro.model.ListJob
import com.example.dansmultipro.service.RetrofitInstance
import com.facebook.CallbackManager
import com.google.gson.GsonBuilder
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.Locale
import java.util.Objects
import kotlin.math.log

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: JobsAdapter
    lateinit var layoutManager: LinearLayoutManager
    var listJob = ArrayList<ListJob>()
    var listJobs = ArrayList<ListJob>()
    var isScroll = false
    var currentItem: Int = 0
    var totalItem: Int = 0
    var scrollOutItems: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.rvJob.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScroll = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem = layoutManager.childCount
                totalItem = layoutManager.itemCount
                scrollOutItems = layoutManager.findFirstVisibleItemPosition()

                if (isScroll && (currentItem + scrollOutItems == totalItem)) {
                    isScroll = false
                    fetchData()
                }
            }
        })
        layoutManager = LinearLayoutManager(context)
        binding.rvJob.layoutManager = layoutManager
        adapter = JobsAdapter(listJob, object : JobsAdapter.ListenerClickJob{
            override fun clickek(position: Int) {
                var intent = Intent(context, DetailJobActivity::class.java)
                intent.putExtra("id", listJob[position].id)
                startActivity(intent)
            }

        })
        binding.rvJob.adapter = adapter

        hitList()

        binding.filter.setOnClickListener {
            expandfunc()
        }

        binding.filterbtn.setOnClickListener {
            if (validate()) {
                hitFilter()
            }
        }

//        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                listrek(newText!!)
//                return true
//            }
//
//            private fun listrek(text: String) {
//                listJob = ArrayList<ListJob>()
//
//                for (i in listJobs) {
//                    if (i.title!!.toLowerCase(Locale.ROOT).contains(text)) {
//                        listJob.add(i)
//                    }
//                }
//                if (listJob.isEmpty()) {
//                    Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
//                } else {
//                    adapter.getFilter(listJob)
//                }
//            }
//
//        })

    }

    private fun fetchData() {
        binding.progressBar.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            kotlin.run {
                binding.progressBar.visibility = View.GONE
                var i = 1
                hitpagination(i.toString())
            }
        }, 3000)
    }

    private fun hitpagination(page: String) {
        RetrofitInstance().getRetrofitInstance()?.paginationListJob(page)
            ?.enqueue(object : retrofit2.Callback<ArrayList<ListJob>?> {
                override fun onResponse(
                    call: Call<ArrayList<ListJob>?>,
                    response: Response<ArrayList<ListJob>?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("hitList", "onResponse: $response")
                        listJob.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                        listJobs = response.body()!!
                    }
                }

                override fun onFailure(call: Call<ArrayList<ListJob>?>, t: Throwable) {
                    Log.d("hitList", "onFailure: $t")
                }
            })
    }

    private fun expandfunc() {
        val v = if (binding.layoutfilter!!.visibility === View.GONE) View.VISIBLE else View.GONE
        TransitionManager.beginDelayedTransition(binding.layoutfilter, AutoTransition())
        if (v == 8) {
            binding.filter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.baseline_keyboard_arrow_down_24))
        } else {
            binding.filter?.setImageDrawable(context?.resources?.getDrawable(R.drawable.baseline_keyboard_arrow_up_24))
        }
        binding.layoutfilter!!.visibility = v
    }

    private fun validate(): Boolean {
        if (Objects.requireNonNull(binding.search.text).toString() == "") {
            binding.search.error = "Input Search!"
            return false
        } else if (Objects.requireNonNull(binding.location.text).toString() == "") {
            binding.location.error = "Input Location!"
            return false
        }
        return true
    }

    private fun hitFilter() {
        RetrofitInstance().getRetrofitInstance()?.filterListJob(binding.search.text.toString(), binding.location.text.toString(), binding.fulltime.isChecked)
            ?.enqueue(object : retrofit2.Callback<ArrayList<ListJob>?> {
                override fun onResponse(
                    call: Call<ArrayList<ListJob>?>,
                    response: Response<ArrayList<ListJob>?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("hitList", "onResponse: $response")
                        listJob.clear()
                        listJob.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                        listJobs = response.body()!!
                    }
                }

                override fun onFailure(call: Call<ArrayList<ListJob>?>, t: Throwable) {
                    Log.d("hitList", "onFailure: $t")
                }
            })
    }

    private fun hitList() {
        RetrofitInstance().getRetrofitInstance()?.listJob()
            ?.enqueue(object : retrofit2.Callback<ArrayList<ListJob>?> {
                override fun onResponse(
                    call: Call<ArrayList<ListJob>?>,
                    response: Response<ArrayList<ListJob>?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("hitList", "onResponse: $response")
                        listJob.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                        listJobs = response.body()!!
                    }
                }

                override fun onFailure(call: Call<ArrayList<ListJob>?>, t: Throwable) {
                    Log.d("hitList", "onFailure: $t")
                }
            })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}