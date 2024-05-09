package com.festra.hotelapplication.navigation

import com.festra.hotelapplication.ui.screen.KEY_ID_BOOKING

sealed class Screen(val route: String) {

    data object Home:Screen ("Home")
    data object Booking:Screen ("Booking")
    data object Profile:Screen ("Profile")
    data object DetailBooking:Screen ("DetailBooking")

    data object DetailBookingUpdate: Screen("DetailBooking/{$KEY_ID_BOOKING}"){
        fun withId(id: Long) = "DetailBooking/$id"
    }
}