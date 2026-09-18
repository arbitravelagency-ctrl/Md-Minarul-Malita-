package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.ui.TravelAgencyViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ContactUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    viewModel: TravelAgencyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookings by viewModel.allBookings.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Ranaghat Office Visit") }
    var selectedCountry by remember { mutableStateOf("Poland") }
    var selectedPurpose by remember { mutableStateOf("Work Permit Consultation") }
    var notes by remember { mutableStateOf("") }

    // Date options (next 5 days)
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    val dates = remember {
        (0..5).map { offset ->
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, offset)
            dateFormat.format(c.time)
        }
    }
    var selectedDate by remember { mutableStateOf(dates.first()) }

    val timeSlots = listOf("10:00 AM", "11:30 AM", "02:00 PM", "03:30 PM", "05:00 PM", "06:30 PM")
    var selectedTimeSlot by remember { mutableStateOf(timeSlots.first()) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var confirmedBookingId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Visa & Job Consultation Booking",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Schedule an in-person visit to our Ranaghat, Nadia office or book an online WhatsApp video / phone consultation.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Consultation Mode Selection
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "1. Choose Consultation Type", fontWeight = FontWeight.Bold, color = NavyDeep, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ConsultationTypeCard(
                            label = "Office Visit",
                            subtext = "Ranaghat, Nadia",
                            icon = Icons.Default.LocationOn,
                            isSelected = selectedType == "Ranaghat Office Visit",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = "Ranaghat Office Visit" }
                        )
                        ConsultationTypeCard(
                            label = "WhatsApp Video",
                            subtext = "Online Face-to-Face",
                            icon = Icons.Default.VideoCall,
                            isSelected = selectedType == "WhatsApp Video Call",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = "WhatsApp Video Call" }
                        )
                        ConsultationTypeCard(
                            label = "Phone Call",
                            subtext = "+91 8945502983",
                            icon = Icons.Default.Phone,
                            isSelected = selectedType == "Direct Phone Call",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedType = "Direct Phone Call" }
                        )
                    }
                }
            }
        }

        // Date and Time Slot
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "2. Select Appointment Date", fontWeight = FontWeight.Bold, color = NavyDeep, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dates) { date ->
                            val isSelected = selectedDate == date
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedDate = date },
                                label = { Text(date, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NavyDeep,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Select Preferred Time Slot", fontWeight = FontWeight.Bold, color = NavyDeep, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(timeSlots) { slot ->
                            val isSelected = selectedTimeSlot == slot
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedTimeSlot = slot },
                                label = { Text(slot, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NavyRoyal,
                                    selectedLabelColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Client Details
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "3. Client Contact Details", fontWeight = FontWeight.Bold, color = NavyDeep, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Full Name *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_name_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone / WhatsApp Number *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_phone_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address (Optional)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Specific Question / Trade Interest") },
                        placeholder = { Text("e.g. Poland warehouse worker eligibility, visa fees, etc.") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 2
                    )
                }
            }
        }

        if (errorMessage != null) {
            item {
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter your name"
                        return@Button
                    }
                    if (phone.isBlank() || phone.length < 8) {
                        errorMessage = "Please enter a valid phone number"
                        return@Button
                    }

                    errorMessage = null
                    viewModel.bookAppointment(
                        name = name.trim(),
                        phone = phone.trim(),
                        email = email.trim(),
                        date = selectedDate,
                        timeSlot = selectedTimeSlot,
                        consultationType = selectedType,
                        country = selectedCountry,
                        purpose = selectedPurpose,
                        notes = notes.trim(),
                        onSuccess = { bookingId ->
                            confirmedBookingId = bookingId
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("booking_submit_button")
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm Slot Booking", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        // My Bookings Section
        if (bookings.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "My Scheduled Bookings (${bookings.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep
                    )
                )
            }

            items(bookings) { b ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = b.bookingId, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = NavyDeep)
                            Surface(
                                color = SuccessGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = b.status,
                                    color = SuccessGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "📅 ${b.bookingDate} at ${b.timeSlot}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = NavyRoyal)
                        Text(text = "Type: ${b.consultationType} • Candidate: ${b.clientName}", fontSize = 11.sp, color = Color(0xFF475569))
                    }
                }
            }
        }
    }

    // Success Booking Dialog
    confirmedBookingId?.let { bId ->
        AlertDialog(
            onDismissRequest = {
                confirmedBookingId = null
                viewModel.clearLastBookingId()
            },
            confirmButton = {
                Button(
                    onClick = {
                        val msg = "Hello Arbi Pori Travel Agency, I have booked a consultation slot. Booking ID: $bId. Name: $name, Date: $selectedDate at $selectedTimeSlot ($selectedType)."
                        ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1, msg)
                        confirmedBookingId = null
                        viewModel.clearLastBookingId()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share on WhatsApp", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        ContactUtils.copyToClipboard(context, "Booking ID", bId)
                        confirmedBookingId = null
                        viewModel.clearLastBookingId()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy ID")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Slot Confirmed!", fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Your counseling slot has been booked with Arbi Pori Travel Agency.", fontSize = 12.sp)
                    Surface(
                        color = NavyDeep,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "BOOKING REFERENCE", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = bId, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "$selectedDate • $selectedTimeSlot", color = Color(0xFFE2E8F0), fontSize = 11.sp)
                        }
                    }
                    Text(
                        text = if (selectedType == "Ranaghat Office Visit")
                            "Office Location: Matiari Banpur, Ranaghat, Nadia, West Bengal - 741502."
                        else "Our officer will contact you on WhatsApp / Phone at the scheduled time.",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }
            }
        )
    }
}

@Composable
fun ConsultationTypeCard(
    label: String,
    subtext: String,
    icon: ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) NavyDeep else Color(0xFFF1F5F9),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NavyDeep else Color(0xFFCBD5E1))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) GoldAccent else NavyDeep,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else NavyDeep
            )
            Text(
                text = subtext,
                fontSize = 9.sp,
                color = if (isSelected) Color(0xFFCBD5E1) else Color(0xFF64748B)
            )
        }
    }
}
