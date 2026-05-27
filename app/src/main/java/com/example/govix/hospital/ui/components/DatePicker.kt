package com.example.govix.hospital.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    value: String,
    onDateSelected: (String) -> Unit,
) {
    val AccentYellow = Color(0xFFFCB216)
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formatter = remember {
        java.time.format.DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(java.time.ZoneId.of("UTC"))
    }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        readOnly = true,
        enabled = false,
        trailingIcon = {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = AccentYellow,
                modifier = Modifier.clickable { showDialog = true },
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color(0xFFCCCCCC),
            disabledTextColor = Color(0xFF1A1A1A),
            disabledLabelColor = Color(0xFF9E9E9E),
            disabledTrailingIconColor = AccentYellow,
        ),
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        onDateSelected(
                            formatter.format(java.time.Instant.ofEpochMilli(millis))
                        )
                    }
                    showDialog = false
                }) { Text("OK", color = AccentYellow) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal", color = Color(0xFF9E9E9E))
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = AccentYellow,
                    todayDateBorderColor = AccentYellow,
                    selectedYearContainerColor = AccentYellow,
                ),
            )
        }
    }
}