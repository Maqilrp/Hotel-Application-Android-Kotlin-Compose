package com.festra.hotelapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.festra.hotelapplication.ui.screen.BookingScreenApi
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
        composable(route = Screen.BookingApi.route){
            BookingScreenApi(navController)
        }
        composable(route = Screen.Profile.route){
            ProfileScreen(navController)
        }
//        composable(route = Screen.DetailBooking.route){
//            DetailBookingSc(navController)
//        }
        // edit route
//        composable(
//            route = Screen.DetailBookingUpdate.route,
//            arguments = listOf(
//                navArgument(KEY_ID_BOOKING){ type = NavType.LongType }
//            )
//        ){
//            navBackStackEntry ->
//            val id = navBackStackEntry.arguments?.getLong(KEY_ID_BOOKING)
//            DetailBookingScreen(navController, id)
//        }
    }
}