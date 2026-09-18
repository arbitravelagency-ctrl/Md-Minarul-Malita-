package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.JobApplication
import com.example.ui.TrackingState
import com.example.ui.TravelAgencyViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ContactUtils

@Composable
fun TrackingScreen(
    viewModel: TravelAgencyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val trackingInput by viewModel.trackingInput.collectAsState()
    val trackingState by viewModel.trackingState.collectAsState()
    val allApplications by viewModel.allApplications.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrackChanges, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Application Tracking Number",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Enter your official Arbi Pori Travel Agency tracking number (e.g., APTA-EU-1082) to view live work permit & visa progress.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = trackingInput,
                            onValueChange = { viewModel.trackingInput.value = it.uppercase() },
                            placeholder = { Text("e.g. APTA-EU-1082", fontSize = 13.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tracking_search_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        Button(
                            onClick = { viewModel.searchTracking(trackingInput) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("tracking_search_button")
                        ) {
                            Text("Track", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { viewModel.openPostPhotoDialog(trackingInput) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyRoyal),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyRoyal),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("track_screen_post_photo_btn")
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Post Photo / Upload Visa Document", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Quick Recent / Sample Tracking Chips
        item {
            Column {
                Text(
                    text = "Quick Access / Recent Applications:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(allApplications) { app ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier.clickable {
                                viewModel.selectApplicationToTrack(app)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (app.stageIndex >= 4) SuccessGreen else NavyRoyal)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(text = app.trackingNumber, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NavyDeep)
                                    Text(text = "${app.applicantName} (${app.country})", fontSize = 9.sp, color = Color(0xFF64748B))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tracking Result State
        when (val state = trackingState) {
            is TrackingState.Searching -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NavyRoyal)
                    }
                }
            }

            is TrackingState.NotFound -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tracking Number Not Found",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E),
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "No record matched '${state.searchedQuery}'. Please ensure the ID is spelled correctly (e.g. APTA-EU-1082) or submit a new application.",
                                fontSize = 12.sp,
                                color = Color(0xFF78350F),
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1, "Hello, I want to check status for my tracking number: ${state.searchedQuery}") },
                                colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Ask Agency on WhatsApp")
                            }
                        }
                    }
                }
            }

            is TrackingState.Found -> {
                item {
                    ApplicationDetailView(
                        app = state.application,
                        onPostPhotoClick = { trackingId ->
                            viewModel.openPostPhotoDialog(trackingId)
                        }
                    )
                }
            }

            is TrackingState.Idle -> {
                // If user has applications in database, show the first one by default as an example
                item {
                    allApplications.firstOrNull()?.let { sampleApp ->
                        Column {
                            Text(
                                text = "Featured Status Demonstration:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ApplicationDetailView(
                                app = sampleApp,
                                onPostPhotoClick = { trackingId ->
                                    viewModel.openPostPhotoDialog(trackingId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationDetailView(
    app: JobApplication,
    onPostPhotoClick: (String) -> Unit
) {
    val context = LocalContext.current

    val stages = listOf(
        "Application Received & Vetted",
        "European Employer Offer & Contract",
        "Work Permit Application to Labour Ministry",
        "Official Work Permit Approved (Zezwolenie)",
        "VFS / Embassy Appointment & Visa Stamping",
        "National D-Visa Approved & Departure"
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Applicant Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(3.dp),
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
                            text = "Tracking ID: ${app.trackingNumber}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = NavyDeep
                        )
                        Text(
                            text = "Applied Date: ${app.appliedDate}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Surface(
                        color = if (app.stageIndex >= 4) SuccessGreen.copy(alpha = 0.15f) else Color(0xFF2563EB).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = app.status,
                            color = if (app.stageIndex >= 4) SuccessGreen else Color(0xFF2563EB),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Candidate info block
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (app.photoUri != null) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFCBD5E1))
                                        .border(1.dp, NavyRoyal, RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = app.photoUri,
                                        contentDescription = "Candidate Photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(54.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Candidate Name", fontSize = 10.sp, color = Color(0xFF64748B))
                                Text(app.applicantName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDeep)
                                if (app.photoUri != null) {
                                    Text("✓ Photo Verified on File", fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Passport No.", fontSize = 10.sp, color = Color(0xFF64748B))
                                val maskedPassport = if (app.passportNumber.length > 4) {
                                    app.passportNumber.take(3) + "****" + app.passportNumber.takeLast(1)
                                } else app.passportNumber
                                Text(maskedPassport, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDeep)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Target Role & Country", fontSize = 10.sp, color = Color(0xFF64748B))
                                Text("${app.jobTitle} (${app.country})", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = NavyRoyal)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Assigned Case Desk", fontSize = 10.sp, color = Color(0xFF64748B))
                                Text("Ranaghat European Desk", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = NavyDeep)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Case Officer Remarks
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = "Official Officer Remarks:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF1E40AF))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = app.remarks, fontSize = 12.sp, color = Color(0xFF1E3A8A), lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Next Target: ${app.targetSubmissionDate}", fontSize = 10.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 6-Stage Timeline Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "European Visa Processing Stages",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NavyDeep
                )
                Spacer(modifier = Modifier.height(14.dp))

                stages.forEachIndexed { index, stageTitle ->
                    val stageNum = index + 1
                    val isCompleted = stageNum < app.stageIndex
                    val isCurrent = stageNum == app.stageIndex
                    val isPending = stageNum > app.stageIndex

                    TimelineStageItem(
                        stageNumber = stageNum,
                        title = stageTitle,
                        isCompleted = isCompleted,
                        isCurrent = isCurrent,
                        isLast = index == stages.size - 1
                    )
                }
            }
        }

        // Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onPostPhotoClick(app.trackingNumber) },
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("track_post_photo_btn")
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (app.photoUri != null) "Update / Post Additional Photo or Document" else "Post Candidate Photo / Document",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val msg = "Hello Arbi Pori Travel Agency, I am checking my European Visa Application status for Tracking Number: ${app.trackingNumber} (${app.applicantName} - ${app.country})."
                        ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1, msg)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat on WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        ContactUtils.copyToClipboard(context, "Tracking ID", app.trackingNumber)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy ID", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun TimelineStageItem(
    stageNumber: Int,
    title: String,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> SuccessGreen
                            isCurrent -> GoldAccent
                            else -> Color(0xFFCBD5E1)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                } else if (isCurrent) {
                    Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                } else {
                    Text(text = "$stageNumber", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(28.dp)
                        .background(if (isCompleted) SuccessGreen else Color(0xFFE2E8F0))
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)) {
            Text(
                text = "Stage $stageNumber: $title",
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 12.sp,
                color = when {
                    isCompleted -> SuccessGreen
                    isCurrent -> NavyDeep
                    else -> Color(0xFF64748B)
                }
            )
            if (isCurrent) {
                Text(
                    text = "● Currently in progress at European Directorate",
                    fontSize = 10.sp,
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
