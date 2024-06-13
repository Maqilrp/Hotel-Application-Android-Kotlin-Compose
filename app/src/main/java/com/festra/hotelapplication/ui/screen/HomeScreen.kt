package com.festra.hotelapplication.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import com.festra.hotelapplication.network.UserDataStore
import com.festra.hotelapplication.BuildConfig
import com.festra.hotelapplication.R
import com.festra.hotelapplication.model.User
import com.festra.hotelapplication.navigation.Screen
import com.festra.hotelapplication.ui.components.BottomAppBarComponent
import com.festra.hotelapplication.ui.components.ProfileDialog
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        topBar = {

        },
        bottomBar = {
            BottomAppBarComponent(navController = navController, selectedIconIndex = 0)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.DetailBooking.route)
                },
            ) {
                Text(
                    text = "Booking Sekarang ",
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(color = Color.White)
                )
            }
        }
    ) { paddingValues ->
        ScreenContent(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier
) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    var showDialog by remember {
        mutableStateOf(false)
    }

    val showMenu = remember { mutableStateOf(false) }

    Column(
        modifier.fillMaxSize(),
    ) {
        // photo
        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.topappbar),
                contentDescription = "hotel",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Text Welcome
                    Text(text = "Selamat Datang")
                    // Icon Profile
                    IconButton(
                        onClick = {
                            if (user.email.isEmpty()) {
                                CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                            }
                            else {
//                            Log.d("SIGN-IN", "User: $user")
                                showMenu.value = !showMenu.value
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile"
                        )
                        // Menu
                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = { showMenu.value = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Profile") },
                                onClick = {
                                    showDialog = true
                                    showMenu.value = false
                                },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                                    showMenu.value = false
//                                    auth.signOut()
//                                    navController.navigate(Screen.Login.route) {
//                                        popUpTo(0) { inclusive = true }
//                                    }
                                },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
            if (showDialog){
                ProfileDialog(
                    user = user,
                    onDismissRequest = { showDialog = false },
                ) {
                    CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                    showDialog = false
                }
            }
        }

        // content
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.topappbar),
                        contentDescription = "hotel",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                    Text(
                        text = "INI FASILITAS",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

private suspend fun signIn(context: Context, dataStore: UserDataStore){
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore){
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
//            Log.d("SIGN-IN", "User email: ${googleId.id}")
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama,email, photoUrl))
        } catch (e: GoogleIdTokenParsingException){
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }
    else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore){
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException){
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}