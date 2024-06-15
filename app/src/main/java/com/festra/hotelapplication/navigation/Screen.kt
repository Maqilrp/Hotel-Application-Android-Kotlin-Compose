package com.festra.hotelapplication.navigation


sealed class Screen(val route: String) {

    data object Home:Screen ("Home")
    data object Booking:Screen ("Booking")
    data object Profile:Screen ("Profile")
    data object DetailBooking:Screen ("DetailBooking")


}