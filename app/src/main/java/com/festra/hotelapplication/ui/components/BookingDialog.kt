package com.festra.hotelapplication.ui.components


import android.app.DatePickerDialog
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.festra.hotelapplication.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BookingDialog(
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: (String,String,String,String,String,String) -> Unit
){
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
        mutableStateOf("")
    }
    var checkOut by remember {
        mutableStateOf("")
    }

    var pesan by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismissRequest()}){
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                //nama
                OutlinedTextField(
                    value = nama,
                    onValueChange = {nama = it},
                    label = { Text(text = stringResource(id = R.string.nama))},
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                // jumlah orang
                JumlahOrangDropDown(selectedText = jumlahOrang, onSelectedTextChange = {jumlahOrang = it})

                // jenis kamar
                JenisKamarDropDown(selectedText = jenisKamar, onSelectedTextChange = {jenisKamar = it})

                // check in
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val calendar = Calendar.getInstance()

                // Update the DatePicker logic
                OutlinedTextField(
                    value = checkIn,
                    onValueChange = {},
                    label = { Text("Tanggal Check-In") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val selectedDate = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth)
                                }
                                checkIn = dateFormat.format(selectedDate.time)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                        datePickerDialog.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Pilih Tanggal Check-In")
                }

// Similar logic for Check-Out date
                OutlinedTextField(
                    value = checkOut,
                    onValueChange = {},
                    label = { Text("Tanggal Check-Out") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val selectedDate = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth)
                                }
                                checkOut = dateFormat.format(selectedDate.time)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
                        datePickerDialog.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Pilih Tanggal Check-Out")
                }

                // pesan
                OutlinedTextField(
                    value = pesan,
                    onValueChange = {pesan = it},
                    label = { Text(text = stringResource(id = R.string.nama))},
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.batal))
                    }
                    OutlinedButton(
                        onClick = { onConfirmation(nama, jumlahOrang, jenisKamar, checkIn, checkOut, pesan) },
                        enabled = nama.isNotEmpty() && jumlahOrang.isNotEmpty() && jenisKamar.isNotEmpty() && checkIn.isNotEmpty() && checkOut.isNotEmpty() && pesan.isNotEmpty(),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.simpan))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddDialogPreview(){
//    Mobpro1Theme {
//        HewanDialog(
//            bitmap = null,
//            onDismissRequest = {  },
//            onConfirmation = { _, _-> }
//        )
//    }
}