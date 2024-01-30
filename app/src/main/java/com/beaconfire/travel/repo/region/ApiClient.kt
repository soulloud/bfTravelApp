package com.beaconfire.travel.repo.region

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://www.universal-tutorial.com/api/"
    fun getRegionApi(): RegionApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(ServiceInterceptor())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RegionApi::class.java)
    }
}

private class ServiceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.header("No-Authentication") == null) {
            val token = TOKEN
            if (token.isNotEmpty()) {
                val finalToken = "Bearer $token"
                request = request.newBuilder()
                    .addHeader("Authorization", finalToken)
                    .build()
            }
        }
        return chain.proceed(request)
    }

    companion object {
        private const val TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJfZW1haWwiOiJ6aGFveGlhOTQwNkBnbWFpbC5jb20iLCJhcGlfdG9rZW4iOiJxUGg3amxYQzM1Z3UyTTJ2RGNwblg4bGEtREtwUUZDMnU2dE5DS0o0WXFTcTI1TVVhVUVFSGstVDRYLWxIWlhFbmZrIn0sImV4cCI6MTcwNjczMDM0MH0.7MCCq3ehUuJd3DU2aEt6OP4P_ZVslUhg3QCQpeEj2-8"
    }
}
