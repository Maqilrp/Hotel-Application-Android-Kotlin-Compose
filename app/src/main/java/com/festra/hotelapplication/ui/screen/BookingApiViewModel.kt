package com.festra.hotelapplication.ui.screen

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festra.hotelapplication.model.Booking
import com.festra.hotelapplication.network.ApiStatus
import com.festra.hotelapplication.network.BookingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.ByteArrayOutputStream

class BookingApiViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Booking>())

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    // Counter for generating unique IDs
    private var lastId: Long = 0L

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = BookingApi.service.getBooking(userId)
                // Update the lastId to be one more than the max id in the retrieved data
                lastId = data.value.maxOfOrNull { it.id ?: 0L } ?: 0L
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(
        userId: String,
        nama: String,
        jumlahOrang: String,
        jenisKamar: String,
        checkIn: String,
        checkOut: String,
        bitmap: Bitmap,
        pesan: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val byteArray = bitmap.resizeAndCompress()
                val imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT)

                // Recycle the bitmap to free up memory
                bitmap.recycle()

                val booking = Booking(
                    id = ++lastId, // Generate a unique ID
                    userId = userId,
                    nama = nama,
                    jumlahOrang = jumlahOrang,
                    jenisKamar = jenisKamar,
                    checkIn = checkIn,
                    checkOut = checkOut,
                    imageId = imageBase64,
                    pesan = pesan
                )
                val result = BookingApi.service.postBooking(booking)

                if (result != null) {
                    // Handle success
                    retrieveData(userId)
                } else {
                    // Handle failure or unexpected response
                    throw Exception("Failed to save booking")
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"

                // Additional logging for HTTP error response
                if (e is HttpException) {
                    Log.e("MainViewModel", "HTTP Exception: ${e.response()?.errorBody()?.string()}")
                }
            }
        }
    }


    fun deleteData(userId: String, bookingId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val result = BookingApi.service.deleteBooking(bookingId)

                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)

            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    private fun Bitmap.resizeAndCompress(maxSize: Int = 1024, maxBytes: Int = 50000, compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG): ByteArray {
        var width = this.width
        var height = this.height

        if (width > maxSize || height > maxSize) {
            val ratio = Math.min(
                maxSize.toFloat() / width,
                maxSize.toFloat() / height
            )
            width = (width * ratio).toInt()
            height = (height * ratio).toInt()
        }

        val resizedBitmap = Bitmap.createScaledBitmap(this, width, height, true)
        var quality = 1
        var stream: ByteArrayOutputStream
        var byteArray: ByteArray

        do {
            stream = ByteArrayOutputStream()
            resizedBitmap.compress(compressFormat, quality, stream)
            byteArray = stream.toByteArray()
            quality -= 5 // Reduce quality by 5 each iteration
        } while (byteArray.size > maxBytes && quality > 0)

        stream.close()
        return byteArray
    }


    fun clearMessage() {
        errorMessage.value = null
    }
}
