package com.festra.hotelapplication.ui.components

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.festra.hotelapplication.ui.theme.HotelApplicationTheme

@Composable
fun DisplayAlertDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
){
    if (openDialog){
        AlertDialog(
            text = { Text(text = "Hapus Bookingan ini ?") },
            confirmButton = {
                TextButton(onClick = {onConfirmation()})
                {
                    Text(text = "Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = {onDismissRequest()}) {
                    Text(text = "Batal")
                }
            },
            onDismissRequest = {onDismissRequest()}
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogPreview(){
    HotelApplicationTheme {
        DisplayAlertDialog(
            openDialog = true,
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}