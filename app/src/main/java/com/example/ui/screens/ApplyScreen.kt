package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.AgencyTab
import com.example.ui.TravelAgencyViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ContactUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyScreen(
    viewModel: TravelAgencyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedVacancy by viewModel.selectedVacancy.collectAsState()
    val allVacancies by viewModel.allVacancies.collectAsState()

    var name by remember { mutableStateOf("") }
    var passportNumber by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("2 Years") }
    var education by remember { mutableStateOf("Higher Secondary (12th)") }

    var selectedJobTitle by remember(selectedVacancy) {
        mutableStateOf(selectedVacancy?.title ?: "Warehouse Logistics & Packing Staff")
    }
    var selectedCountry by remember(selectedVacancy) {
        mutableStateOf(selectedVacancy?.country ?: "Poland")
    }

    var isJobDropdownExpanded by remember { mutableStateOf(false) }
    var isDocPassportReady by remember { mutableStateOf(true) }
    var isDocPccReady by remember { mutableStateOf(false) }
    var isDocExpReady by remember { mutableStateOf(true) }

    var candidatePhotoUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            candidatePhotoUri = uri
        }
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var submittedTrackingId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "European Job Application",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Submit your profile for European work permit vetting. Once submitted, you will receive an official Application Tracking Number.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Job selection card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Selected Job & Country",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = isJobDropdownExpanded,
                        onExpandedChange = { isJobDropdownExpanded = !isJobDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = "$selectedJobTitle ($selectedCountry)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select European Vacancy") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isJobDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("apply_job_dropdown"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = isJobDropdownExpanded,
                            onDismissRequest = { isJobDropdownExpanded = false }
                        ) {
                            allVacancies.forEach { vacancy ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${vacancy.countryFlag} ${vacancy.title} (${vacancy.country} - ${vacancy.salaryEur})", fontSize = 13.sp)
                                    },
                                    onClick = {
                                        selectedJobTitle = vacancy.title
                                        selectedCountry = vacancy.country
                                        isJobDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Personal Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Candidate Passport & Personal Details",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Candidate Full Name (as on Passport) *") },
                        placeholder = { Text("e.g. Subrata Mondal") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_name_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = passportNumber,
                        onValueChange = { passportNumber = it.uppercase() },
                        label = { Text("Passport Number *") },
                        placeholder = { Text("e.g. V9823412") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_passport_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number *") },
                            placeholder = { Text("+91 9830123456") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("apply_phone_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = whatsappNumber,
                            onValueChange = { whatsappNumber = it },
                            label = { Text("WhatsApp No.") },
                            placeholder = { Text("+91 9830123456") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("apply_whatsapp_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        placeholder = { Text("candidate@example.com") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_email_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Full Address (Village/Town, District, State, PIN) *") },
                        placeholder = { Text("e.g. Ranaghat, Nadia, West Bengal - 741502") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_address_input"),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 3
                    )
                }
            }
        }

        // Qualifications
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. Trade & Experience",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = experience,
                            onValueChange = { experience = it },
                            label = { Text("Work Experience") },
                            placeholder = { Text("e.g. 2 Years") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("apply_experience_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = education,
                            onValueChange = { education = it },
                            label = { Text("Highest Education") },
                            placeholder = { Text("10th / 12th / ITI") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("apply_education_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Documents Ready for Submission:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = NavyDeep
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isDocPassportReady = !isDocPassportReady }
                    ) {
                        Checkbox(
                            checked = isDocPassportReady,
                            onCheckedChange = { isDocPassportReady = it },
                            colors = CheckboxDefaults.colors(checkedColor = NavyDeep)
                        )
                        Text("Original Passport with min. 18 months validity", fontSize = 12.sp, color = Color(0xFF334155))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isDocPccReady = !isDocPccReady }
                    ) {
                        Checkbox(
                            checked = isDocPccReady,
                            onCheckedChange = { isDocPccReady = it },
                            colors = CheckboxDefaults.colors(checkedColor = NavyDeep)
                        )
                        Text("Police Clearance Certificate (PCC) ready / applied", fontSize = 12.sp, color = Color(0xFF334155))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isDocExpReady = !isDocExpReady }
                    ) {
                        Checkbox(
                            checked = isDocExpReady,
                            onCheckedChange = { isDocExpReady = it },
                            colors = CheckboxDefaults.colors(checkedColor = NavyDeep)
                        )
                        Text("White Background Photos & Experience letter", fontSize = 12.sp, color = Color(0xFF334155))
                    }
                }
            }
        }

        // 4. Candidate Photo Upload / Post Photo
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("apply_photo_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "4. Post / Upload Candidate Photo",
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "White background 35x45mm or Passport front scan",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        if (candidatePhotoUri != null) {
                            Surface(
                                color = Color(0xFFDCFCE7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Photo Attached",
                                    color = SuccessGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (candidatePhotoUri != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = candidatePhotoUri,
                                    contentDescription = "Candidate Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(80.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Candidate Photo Selected",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = NavyDeep
                                )
                                Text(
                                    text = "Will be linked to your European Work Permit file",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            photoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NavyRoyal),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text("Change", fontSize = 11.sp)
                                    }

                                    OutlinedButton(
                                        onClick = { candidatePhotoUri = null },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Text("Remove", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Post Photo",
                                    tint = NavyRoyal,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Post / Upload Candidate Photo",
                                    fontWeight = FontWeight.Bold,
                                    color = NavyDeep,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Tap to choose image from phone gallery or camera",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Error message if any
        if (errorMessage != null) {
            item {
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = errorMessage!!, color = Color(0xFFDC2626), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Please enter candidate full name"
                        return@Button
                    }
                    if (passportNumber.isBlank() || passportNumber.length < 5) {
                        errorMessage = "Please enter a valid Passport Number"
                        return@Button
                    }
                    if (phone.isBlank() || phone.length < 8) {
                        errorMessage = "Please enter a valid contact phone number"
                        return@Button
                    }
                    if (address.isBlank()) {
                        errorMessage = "Please enter complete residential address"
                        return@Button
                    }

                    errorMessage = null
                    viewModel.submitApplication(
                        name = name.trim(),
                        passport = passportNumber.trim(),
                        phone = phone.trim(),
                        whatsapp = whatsappNumber.ifBlank { phone }.trim(),
                        email = email.trim(),
                        address = address.trim(),
                        jobTitle = selectedJobTitle,
                        country = selectedCountry,
                        experience = experience,
                        education = education,
                        photoUri = candidatePhotoUri?.toString(),
                        onSuccess = { generatedId ->
                            submittedTrackingId = generatedId
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("apply_submit_button")
            ) {
                Icon(Icons.Default.Assignment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submit Application & Generate Tracking ID", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }

    // Success Tracking ID Dialog
    submittedTrackingId?.let { trackingId ->
        AlertDialog(
            onDismissRequest = {
                submittedTrackingId = null
                viewModel.clearLastTrackingId()
            },
            confirmButton = {
                Button(
                    onClick = {
                        submittedTrackingId = null
                        viewModel.clearLastTrackingId()
                        viewModel.searchTracking(trackingId)
                        viewModel.switchTab(AgencyTab.TRACK)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.TrackChanges, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Track Status Now", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        ContactUtils.copyToClipboard(context, "Tracking Number", trackingId)
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
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(26.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Application Registered!", fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Your European work permit application has been registered successfully with Arbi Pori Travel Agency.",
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )

                    Surface(
                        color = NavyDeep,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "OFFICIAL TRACKING NUMBER", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = trackingId,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 22.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Keep this number safe to track visa progress", color = Color(0xFF94A3B8), fontSize = 10.sp)
                        }
                    }

                    if (candidatePhotoUri != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFCBD5E1))
                            ) {
                                AsyncImage(
                                    model = candidatePhotoUri,
                                    contentDescription = "Candidate Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(45.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Candidate Photo Attached", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                                Text("Linked with work permit file", fontSize = 10.sp, color = Color(0xFF64748B))
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val msg = "Hello Arbi Pori Travel Agency, I have submitted an application for European Job ($selectedJobTitle in $selectedCountry). My Tracking Number is: $trackingId. Candidate Name: $name."
                            ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1, msg)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Details on WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }
}
