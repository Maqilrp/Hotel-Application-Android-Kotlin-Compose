package com.festra.hotelapplication.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.festra.hotelapplication.util.SettingsDataStore
import com.festra.hotelapplication.util.ViewModelFactory
import com.festra.hotelapplication.R
import com.festra.hotelapplication.database.BookingDb
import com.festra.hotelapplication.model.Booking
import com.festra.hotelapplication.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController){
    // data store
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Booking",
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        ),
    )

    var selectedIconIndex by rememberSaveable {
        mutableIntStateOf(1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Booking") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIconIndex == index,
                        onClick = {
                            selectedIconIndex = index
                            navController.navigate(item.title)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon = {
                            Icon(
                                imageVector = if (index == selectedIconIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        }
    ) {
        paddingValues ->
        BookingContent(modifier = Modifier.padding(paddingValues), showList, navController)
    }
}

@Composable
fun BookingContent(modifier: Modifier, showList: Boolean, navController: NavController) {
    val context = LocalContext.current
    val db = BookingDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: BookingViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    if (data.isEmpty()){
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.silangmerah),
                contentDescription = "Data Kosong"
            )
            Text(text = stringResource(id = R.string.list_kosong))
        }
    }else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(booking = it) {
                        navController.navigate(Screen.DetailBookingUpdate.withId(it.id))
                    }
                    HorizontalDivider()
                }
            }
        }
        else{
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ){
                items(data){
                    GridItem(booking = it) {
                        navController.navigate(Screen.DetailBookingUpdate.withId(it.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(booking: Booking, onClick: () -> Unit){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Column {
            Text(text = "Booking For")
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            // nama
            Row {
                Text(text = "Nama : ")
                Text(
                    text = booking.nama,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // tanggal check in dan check out
            Row {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val checkInDateFormatted = dateFormat.format(booking.checkIn)
                val checkOutDateFormatted = dateFormat.format(booking.checkOut)
                
                Text(text = "Date : ")
                
                Text(
                    text = checkInDateFormatted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(text = "  -  " )

                Text(
                    text = checkOutDateFormatted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        
//        Text(text = catatan.tanggal)
    }
}

@Composable
fun GridItem(booking: Booking, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(


                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column {
                Text(text = "Booking For")
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                // nama
                Row {
                    Text(text = "Nama : ")
                    Text(
                        text = booking.nama,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                }

                // tanggal check in dan check out
                Row {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val checkInDateFormatted = dateFormat.format(booking.checkIn)
                    val checkOutDateFormatted = dateFormat.format(booking.checkOut)

                    Text(text = "Date : ")

                    Text(
                        text = checkInDateFormatted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(text = "  -  " )

                    Text(
                        text = checkOutDateFormatted,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
//            Text(text = catatan.tanggal)
        }
    }
}