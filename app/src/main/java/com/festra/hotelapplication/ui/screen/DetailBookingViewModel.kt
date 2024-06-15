package com.festra.hotelapplication.ui.screen
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festra.hotelapplication.model.Booking
import com.festra.hotelapplication.navigation.Screen
import com.festra.hotelapplication.network.ApiStatus
import com.festra.hotelapplication.network.BookingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class DetailBookingViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<Screen.Booking>())
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    var showList = mutableStateOf(true)

    fun retrieveData(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = BookingApi.service.getBooking(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: Long, nama: String, jumlahOrang: String, jenisKamar: String, checkIn: String, checkOut: String, imageId: Bitmap?, pesan: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imagePart = imageId?.toMultipartBody()
                val booking = Booking(userId, nama, jumlahOrang, jenisKamar, checkIn, checkOut,
                    imagePart.toString(), pesan)
                val result = BookingApi.service.postBooking(userId, booking)
                if (result != null) retrieveData(userId)
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(userId: Long, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                BookingApi.service.deleteBooking(userId.toString(), id)
                retrieveData(userId)
            } catch (e: Exception) {
                status.value = ApiStatus.FAILED
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}
