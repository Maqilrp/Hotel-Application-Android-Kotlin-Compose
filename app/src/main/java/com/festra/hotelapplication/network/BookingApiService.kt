package com.festra.hotelapplication.network

import com.festra.hotelapplication.model.Booking
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

//private const val BASE_URL = "https://raw.githubusercontent.com/" +
//        "indraazimi/mobpro1-compose/static-api/"
private const val BASE_URL = "https://gh.d3ifcool.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private  val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookingApiService {
    //    @GET("static-api.json")
    // get data
    @GET("hewan.php")
    suspend fun getBooking(
        @Header("Authorization") userId: String
    ): List<Booking>

    // post data
    @Multipart
    @POST("hewan.php")
    suspend fun postBooking(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("namaLatin") namaLatin: RequestBody,
        @Part image: MultipartBody.Part
    )

    // delete data
    @DELETE("hewan.php")
    suspend fun deleteBooking(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    )
}

object BookingApi{
    val service: BookingApiService by lazy {
        retrofit.create(BookingApiService::class.java)
    }

    fun getBookingUrl(imageId: String): String{
//        return "$BASE_URL$imageId.jpg"
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }