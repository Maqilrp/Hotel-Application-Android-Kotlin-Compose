package com.festra.hotelapplication.ui.screen

import DetailBookingViewModel
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.festra.hotelapplication.R
import com.festra.hotelapplication.model.Booking
import com.festra.hotelapplication.network.ApiStatus
import com.festra.hotelapplication.network.UserDataStore
import com.festra.hotelapplication.ui.components.BookingDialog
import com.festra.hotelapplication.ui.components.BottomAppBarComponent
import com.festra.hotelapplication.ui.components.DeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenApi(
    navController: NavController,
) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(com.festra.hotelapplication.model.User())

    val viewModel: DetailBookingViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

//    var showDialog by remember {
//        mutableStateOf(false)
//    }
    var showDialogBooking by remember {
        mutableStateOf(false)
    }

    var bitmap: Bitmap? by remember {
        mutableStateOf(null)
    }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showDialogBooking = true
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
                        viewModel.showList.value = !viewModel.showList.value
                    }) {
                        Icon(
                            painter = painterResource(
                                if (viewModel.showList.value) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (viewModel.showList.value) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
                },
            ) {
                Text(
                    text = "Booking Sekarang ",
                    modifier = Modifier.padding(16.dp),
                    style = TextStyle(color = Color.White)
                )
            }
        },
        bottomBar = {
            BottomAppBarComponent(navController = navController, selectedIconIndex = 1)
        }
    ) { padding ->
        BookingApiContent(viewModel, 0L,Modifier.padding(padding))

        if (showDialogBooking){
            BookingDialog(
                bitmap = bitmap,
                onDismissRequest = { showDialogBooking = false }) {
                    nama, jumlahOrang, jenisKamar, checkIn, checkOut, pesan ->
                viewModel.saveData(0L,nama,jumlahOrang,jenisKamar,checkIn,checkOut,bitmap,pesan)
                showDialogBooking = false
            }
        }
        if (errorMessage != null) {
            Toast.makeText(context,errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun BookingApiContent(
    viewModel: DetailBookingViewModel,
    userId: Long,
    modifier: Modifier
) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retrieveData(userId)
    }

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        ApiStatus.SUCCESS -> {
            if (viewModel.showList.value) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(data) { booking ->
                        ListItem(booking = booking, viewModel = viewModel, userId = userId)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    contentPadding = PaddingValues(4.dp),
                    modifier = modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(data) { booking ->
                        GridItem(booking = booking, viewModel = viewModel, userId = userId)
                    }
                }
            }
        }
        ApiStatus.FAILED -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Failed to fetch data. Please try again.")
            }
        }
    }
}
@Composable
fun ListItem(booking: Booking, viewModel: DetailBookingViewModel, userId: Long) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DeleteDialog(
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                viewModel.deleteData(userId, booking.id.toString())
                showDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            booking.imageId?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(128.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = booking.nama,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Text(
                    text = "Jumlah Orang: ${booking.jumlah_orang}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Jenis Kamar: ${booking.jenis_kamar}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Check-in: ${booking.check_in}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Check-out: ${booking.check_out}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Pesan: ${booking.pesan}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

@Composable
fun GridItem(booking: Booking, viewModel: DetailBookingViewModel, userId: Long) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DeleteDialog(
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                viewModel.deleteData(userId, booking.id.toString())
                showDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            booking.imageId?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(128.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = booking.nama,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Jumlah Orang: ${booking.jumlah_orang}",
                style = TextStyle(fontStyle = FontStyle.Italic)
            )
            Text(
                text = "Jenis Kamar: ${booking.jenis_kamar}",
                style = TextStyle(fontStyle = FontStyle.Italic)
            )
            Text(
                text = "Check-in: ${booking.check_in}",
                style = TextStyle(fontStyle = FontStyle.Italic)
            )
            Text(
                text = "Check-out: ${booking.check_out}",
                style = TextStyle(fontStyle = FontStyle.Italic)
            )
            Text(
                text = "Pesan: ${booking.pesan}",
                style = TextStyle(fontStyle = FontStyle.Italic)
            )
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

fun getCroppedImage(contentResolver: ContentResolver, result: CropImageView.CropResult): Bitmap? {
    return try {
        if (result.isSuccessful) {
            val imageUri = result.uriContent
            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
            }
        } else {
            Log.e("Crop Error", "Error cropping image: ${result.error}")
            null
        }
    } catch (e: Exception) {
        Log.e("Crop Error", "Error getting cropped image: ${e.message}")
        null
    }
}
