package com.festra.hotelapplication.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.festra.hotelapplication.database.BookingDao
import com.festra.hotelapplication.model.Booking
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class BookingViewModel(dao: BookingDao): ViewModel() {
    val data: StateFlow<List<Booking>> = dao.getBooking().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}