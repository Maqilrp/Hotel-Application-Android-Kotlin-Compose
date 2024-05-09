package com.festra.hotelapplication.ui.screen

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.festra.hotelapplication.util.ViewModelFactory
import com.festra.hotelapplication.database.BookingDb
import com.festra.hotelapplication.navigation.Screen
import com.festra.hotelapplication.ui.components.DisplayAlertDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val KEY_ID_BOOKING = "idBooking"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBookingScreen(navController: NavController, id: Long? = null) {
    val context = LocalContext.current
    val db = BookingDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    
    val viewModel: DetailBookingViewModel = viewModel(factory = factory)

    // val untuk form
    var nama by remember {
        mutableStateOf("")
    }
    var jumlahOrang by remember {
        mutableStateOf("")
    }
    var jenisKamar by remember {
        mutableStateOf("")
    }
    var checkIn by remember {
        mutableStateOf(Date())
    }
    var checkOut by remember {
        mutableStateOf(Date())
    }
    var pesan by remember {
        mutableStateOf("")
    }

    //dialog konfirmasi
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true){
        if (id == null) return@LaunchedEffect
        val data = viewModel.getBooking(id) ?: return@LaunchedEffect
        
        nama = data.nama
        jumlahOrang = data.jumlahOrang
        jenisKamar = data.jenisKamar
        checkIn = data.checkIn
        checkOut = data.checkOut
        pesan = data.pesan
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                // title dan warna
                title = {
                    if (id == null)
                        Text(text = "Booking")
                    else
                        Text(text = "Ubah Booking")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                
                // button simpan
                actions = {
                    IconButton(
                        onClick = {
                            if (nama == "" || jumlahOrang == "" || jenisKamar == "" || pesan == ""){
                                Toast.makeText(context,"Data Tidak Boleh Kosong", Toast.LENGTH_LONG).show()
                                return@IconButton
                            }

                            if (id == null){
                                viewModel.insert(nama, jumlahOrang, jenisKamar, checkIn, checkOut, pesan)
                            } else{
                                viewModel.update(id,nama, jumlahOrang, jenisKamar, checkIn, checkOut, pesan)
                            }

                            navController.navigate(Screen.Booking.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Simpan",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // panggil dialog
                    if (id != null){
                        DeleteAction {
                            showDialog = true
                        }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }
                        ) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) {
            paddingValues ->
        FormBooking(
            nama = nama, onNamaChange = { nama = it },
            jumlahOrang = jumlahOrang, onJumlahOrangChange = { jumlahOrang = it },
            jenisKamar = jenisKamar, onJenisKamarChange = { jenisKamar = it },
            checkIn = checkIn, onCheckInChange = { checkIn = it },
            checkOut = checkOut, onCheckOutChange = { checkOut = it },
            pesan = pesan, onPesanChange = { pesan = it },
            modifier = Modifier.padding(paddingValues)
        )
    }

}

@Composable
fun FormBooking(
    nama: String, onNamaChange: (String) -> Unit,
    jumlahOrang: String, onJumlahOrangChange: (String) -> Unit,
    jenisKamar: String, onJenisKamarChange: (String) -> Unit,
    checkIn: Date, onCheckInChange: (Date) -> Unit,
    checkOut: Date, onCheckOutChange: (Date) -> Unit,
    pesan: String, onPesanChange: (String) -> Unit,
    modifier: Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // nama
        OutlinedTextField(
            value = nama,
            onValueChange = {onNamaChange(it)},
            label = { Text(text = "Nama Pemesan") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = true,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Jumlah Orang
        Text(
            text = "Jumlah Orang",
            Modifier.padding(5.dp,5.dp,5.dp,0.dp)
        )
        Row(
            modifier = Modifier
                .padding(top = 1.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            listOf("1", "2", "3", "4", "5").forEach { jumlahOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onJumlahOrangChange(jumlahOption) // Update kelas through onKelasChange
                    }
                ) {
                    RadioButton(
                        selected = jumlahOrang == jumlahOption, // Use kelas directly
                        onClick = {
                            onJumlahOrangChange(jumlahOption) // Update kelas through onKelasChange
                        }
                    )
                    Text(text = jumlahOption)
                }
            }
        }

        // Jenis Kamar
        Text(
            text = "Jenis Kamar",
            Modifier.padding(5.dp,5.dp,5.dp,0.dp)
        )
        Row(
            modifier = Modifier
                .padding(top = 1.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            listOf("Regular", "President").forEach { jumlahOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onJenisKamarChange(jumlahOption) // Update kelas through onKelasChange
                    }
                ) {
                    RadioButton(
                        selected = jenisKamar == jumlahOption, // Use kelas directly
                        onClick = {
                            onJenisKamarChange(jumlahOption) // Update kelas through onKelasChange
                        }
                    )
                    Text(text = jumlahOption)
                }
            }
        }

        // Check-in date picker
        val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
        val formattedCheckInDate = remember(checkIn.time) {
            dateFormat.format(checkIn)
        }

        OutlinedTextField(
            value = formattedCheckInDate,
            onValueChange = {}, // No-op since the text field is read-only
            label = { Text(text = "Check-in Date") },
            singleLine = true,
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Show the date picker dialog for selecting check-in date
        MyDatePicker(
            selectedDate = checkIn,
            onDateSelected = { date ->
                onCheckInChange(date)
            }
        )

        // Check-out date picker
        val formattedCheckOutDate = remember(checkOut.time) {
            dateFormat.format(checkOut)
        }

        OutlinedTextField(
            value = formattedCheckOutDate,
            onValueChange = {}, // No-op since the text field is read-only
            label = { Text(text = "Check-out Date") },
            singleLine = true,
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Show the date picker dialog for selecting check-out date
        MyDatePicker(
            selectedDate = checkOut,
            onDateSelected = { date ->
                onCheckOutChange(date)
            }
        )

        // Pesan
        OutlinedTextField(
            value = pesan,
            onValueChange = {onPesanChange(it)},
            label = { Text(text = "Pesan") },
            maxLines = 3,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = true,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun MyDatePicker(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    // Set the minimum date to today
    val minDate = Calendar.getInstance()

    // Display a button to open the DatePickerDialog
    Button(
        onClick = {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    onDateSelected(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Set the minimum date for selection
            datePickerDialog.datePicker.minDate = minDate.timeInMillis

            datePickerDialog.show()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Select Check-in Date")
    }
}

@Composable
fun DeleteAction(delete: () -> Unit){
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true}) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Opsi Lainnya",
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}) {
            DropdownMenuItem(
                text = {
                    Text(text = " Batalkan Bookingan")
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}
