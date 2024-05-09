package com.festra.hotelapplication.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.festra.hotelapplication.database.BookingDao
import com.festra.hotelapplication.ui.screen.BookingViewModel
import com.festra.hotelapplication.ui.screen.DetailBookingViewModel

class ViewModelFactory(
    private val dao: BookingDao
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)){
        return BookingViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailBookingViewModel::class.java)){
            return DetailBookingViewModel(dao) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}