package com.example.dansmultipro

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import com.example.dansmultipro.databinding.ActivityDetailJobBinding
import com.example.dansmultipro.model.ListJob
import com.example.dansmultipro.service.RetrofitInstance
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class DetailJobActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailJobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        RetrofitInstance().getRetrofitInstance()?.detailJob(intent.extras!!.getString("id").toString())
            ?.enqueue(object : retrofit2.Callback<ListJob?> {
                override fun onResponse(
                    call: Call<ListJob?>,
                    response: Response<ListJob?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Log.d("hitList", "onResponse: $response")
                        var data = response.body()
                        binding.job.text = data!!.title
                        if (data.type == "Full Time") { binding.fulltime.text = "Yes" } else {  binding.fulltime.text = "No" }
                        binding.desc.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(data.description, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(data.description)
                        }
                        Picasso.get()
                            .load(data.companyLogo)
                            .placeholder(R.drawable.baseline_image_24)
                            .into(binding.imageCompany)
                        binding.corporateName.text = data.company
                        binding.corporateLocation.text = data.location
                        binding.created.text = data.createdAt
                        binding.website.setOnClickListener {
                            val uri: Uri =
                                Uri.parse(data.companyUrl)
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }

                        var urlstring = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Html.fromHtml(data.howToApply, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            Html.fromHtml(data.howToApply)
                        }
                        binding.hta.setOnClickListener {
                            val uri: Uri =
                                Uri.parse(urlstring.toString())
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(call: Call<ListJob?>, t: Throwable) {
                    Log.d("hitList", "onFailure: $t")
                }
            })

    }
}