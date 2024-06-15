package com.festra.hotelapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.festra.hotelapplication.R
import com.festra.hotelapplication.navigation.Screen

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)
@Composable
fun HomeScreen(navController: NavController){
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
        mutableIntStateOf(0)
    }

    Scaffold(
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
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    navController.navigate(Screen.DetailBooking.route)
//                },
//            ) {
//                Text(
//                    text = "Booking Sekarang ",
//                    modifier = Modifier.padding(16.dp),
//                    style = TextStyle(color = Color.White)
//                )
//            }
//        }
    ) {
        paddingValues ->
        ScreenContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    Column(
        modifier.fillMaxSize(),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.topappbar), contentDescription = "hotel", modifier = Modifier.fillMaxSize() )
        }
        
        Text(
            text = "Our Facility",
            Modifier.padding(start = 16.dp)
        )
        Text(
            text = "Fasilitas hotel yang kami miliki",
            Modifier.padding(start = 16.dp),
            style = TextStyle(fontSize = 10.sp, color = Color.Gray)
        )

        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Card(
                onClick = { /*TODO*/ },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .size(width = 240.dp, height = 150.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Image(painter = painterResource(id = R.drawable.topappbar), contentDescription = "hotel", modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                    )
                    Text(text = "INI FASILITAS", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
                }
            }
        }
    }

}