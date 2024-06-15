package com.festra.hotelapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.festra.hotelapplication.ui.screen.BookingApiScreen
import com.festra.hotelapplication.ui.screen.HomeScreen
import com.festra.hotelapplication.ui.screen.ProfileScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route){
            HomeScreen(navController)
        }
        composable(route = Screen.Booking.route){
            BookingApiScreen(navController)
        }
        composable(route = Screen.Profile.route){
            ProfileScreen(navController)
        }


    }
}