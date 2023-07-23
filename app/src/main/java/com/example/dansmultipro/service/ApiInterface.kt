package com.example.dansmultipro.service

import com.example.dansmultipro.model.ListJob
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {

    @GET("positions.json")
    fun listJob() : Call<ArrayList<ListJob>>

    @GET("positions.json")
    fun filterListJob(
        @Query("description") desc: String,
        @Query("location") location: String,
        @Query("full_time") full_time: Boolean,
    ) : Call<ArrayList<ListJob>>

    @GET("positions.json")
    fun paginationListJob(
        @Query("page") page: String
    ) : Call<ArrayList<ListJob>>

    @GET("positions/{id}")
    fun detailJob(
        @Path("id") id: String
    ) : Call<ListJob>
}