package com.app.focusonbackgroundexecution.network.services

import com.app.focusonbackgroundexecution.network.model.APIResponse
import retrofit2.Call
import retrofit2.http.GET

interface APIServices {

    @GET("facts")
    fun getFacts(): Call<APIResponse>

}