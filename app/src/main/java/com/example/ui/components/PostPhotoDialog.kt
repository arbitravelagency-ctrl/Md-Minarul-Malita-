package com.example.ui.components

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.ui.TravelAgencyViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ContactUtils

val PHOTO_CATEGORIES = listOf(
    "Candidate White Background Photo (35x45mm)",
    "Passport Front Page Scan",
    "Passport Back Page Scan",
    "Police Clearance Certificate (PCC)",
    "Trade / Experience Certificate",
    "Payment Receipt / UPI Proof",
    "Other Visa Document"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostPhotoDialog(
    viewModel: TravelAgencyViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val prefilledTracking by viewModel.prefilledTrackingForPhoto.collectAsState()

    var candidateName by remember { mutableStateOf("") }
    var trackingNumber by remember(prefilledTracking) { mutableStateOf(prefilledTracking) }
    var selectedCategory by remember { mutableStateOf(PHOTO_CATEGORIES[0]) }
    var isCategoryDropdownOpen by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successPhotoId by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            errorMessage = null
        }
    }

    if (successPhotoId != null) {
        AlertDialog(
            onDismissRequest = {
                successPhotoId = null
                onDismiss()
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareText = "Hello Arbi Pori Travel Agency, I have posted a photo for European Visa.\n" +
                                "Photo ID: $successPhotoId\n" +
                                "Candidate: $candidateName\n" +
                                (if (trackingNumber.isNotBlank()) "Tracking No: $trackingNumber\n" else "") +
                                "Category: $selectedCategory\n" +
                                (if (notes.isNotBlank()) "Notes: $notes\n" else "")
                        ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1, shareText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Notify Agency on WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        successPhotoId = null
                        onDismiss()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Done")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Photo Posted Successfully!", fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Your photo has been saved to your European visa dossier at Arbi Pori Travel Agency.",
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("PHOTO REFERENCE ID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyRoyal)
                            Text(text = successPhotoId!!, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = NavyDeep)
                            Text("Category: $selectedCategory", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        )
        return
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(16.dp)),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(NavyDeep, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Post Photo / Document",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = NavyDeep
                            )
                            Text(
                                text = "Upload to candidate visa file",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_post_photo_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                    }
                }

                // Photo Selection Area
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("photo_selection_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (selectedImageUri != null) NavyRoyal else Color(0xFFCBD5E1)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (selectedImageUri != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Selected Photo Preview",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyRoyal),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Change Photo", fontSize = 11.sp)
                                }

                                OutlinedButton(
                                    onClick = { selectedImageUri = null },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Remove", fontSize = 11.sp)
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = NavyRoyal,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Select Photo to Post",
                                    fontWeight = FontWeight.Bold,
                                    color = NavyDeep,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Supports JPEG, PNG • Passport photo, PCC, Scan or Receipt",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("post_photo_choose_btn")
                                ) {
                                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Choose Photo from Gallery", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = isCategoryDropdownOpen,
                    onExpandedChange = { isCategoryDropdownOpen = !isCategoryDropdownOpen },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Photo Category *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownOpen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("photo_category_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = isCategoryDropdownOpen,
                        onDismissRequest = { isCategoryDropdownOpen = false }
                    ) {
                        PHOTO_CATEGORIES.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category, fontSize = 12.sp) },
                                onClick = {
                                    selectedCategory = category
                                    isCategoryDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                // Candidate Name
                OutlinedTextField(
                    value = candidateName,
                    onValueChange = { candidateName = it },
                    label = { Text("Candidate Full Name *") },
                    placeholder = { Text("e.g. Subhasish Roy") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("photo_candidate_name_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                // Tracking ID (Optional)
                OutlinedTextField(
                    value = trackingNumber,
                    onValueChange = { trackingNumber = it.uppercase() },
                    label = { Text("Tracking Number (Optional)") },
                    placeholder = { Text("e.g. APTA-EU-1082") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("photo_tracking_number_input"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Description / Notes (Optional)") },
                    placeholder = { Text("e.g. 35x45mm white background studio photo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("photo_notes_input"),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 2
                )

                if (errorMessage != null) {
                    Surface(
                        color = Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(errorMessage!!, color = Color(0xFFDC2626), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Submit Button
                Button(
                    onClick = {
                        if (selectedImageUri == null) {
                            errorMessage = "Please choose a photo to post"
                            return@Button
                        }
                        if (candidateName.isBlank()) {
                            errorMessage = "Please enter candidate name"
                            return@Button
                        }

                        errorMessage = null
                        viewModel.postPhoto(
                            candidateName = candidateName.trim(),
                            trackingNumber = trackingNumber.trim(),
                            photoType = selectedCategory,
                            photoUri = selectedImageUri.toString(),
                            notes = notes.trim(),
                            onSuccess = { generatedId ->
                                successPhotoId = generatedId
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_post_photo_btn")
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Post Photo to Application File", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
