package com.example.projekt1.networking

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException

object RetrofitBuilder {

    private const val BASE_URL = "https://www.metaweather.com/"

    private fun getRetrofit(): Retrofit {


        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(ErrorInterceptor()).build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: APIService = getRetrofit().create(APIService::class.java)
}

class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        println("OVO SEUVIK PRINTA")
        val request: Request = chain.request()
        val response = try {
            chain.proceed(request)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            val message =
                if (e is ConnectException) "Connection Error" else "Something went wrong. Please try again."
            throw IOException(message + ", 500")
        }
        if (response.code() != 200) {
            println("RESPONSE CODE NOT 200")
        } else {
            println("RESPONSE COD 2000")
        }
        return response
    }

}

