package com.festra.hotelapplication.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.festra.hotelapplication.ui.components.BottomAppBarComponent

@Composable
fun ProfileScreen(navController: NavController){


    Scaffold(
        bottomBar = {
            BottomAppBarComponent(navController = navController, selectedIconIndex = 2)
        }
    ) {
            paddingValues ->
        ProfileContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun ProfileContent(modifier: Modifier) {
    Column(
        modifier.fillMaxSize()
    ) {
        Text(text = "Ini profile")
    }
}

