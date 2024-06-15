package com.festra.hotelapplication.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.festra.hotelapplication.model.Booking
import com.festra.hotelapplication.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://retoolapi.dev/q9wLKl/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookingApiService {
    @GET("booking")
    suspend fun getBooking(
        @Header("Authorization") userId: String
    ): List<Booking>

    @POST("booking")
    suspend fun postBooking(
        @Body booking: Booking
    ): Booking

    @DELETE("booking/{id}")
    suspend fun deleteBooking(
        @Path("id") bookingId: Long
    ): OpStatus
}

object BookingApi {
    val service: BookingApiService by lazy {
        retrofit.create(BookingApiService::class.java)
    }

    fun getBookingUrl(imageId: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(imageId, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
