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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.TravelAgencyViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ContactUtils

@Composable
fun ContactScreen(
    viewModel: TravelAgencyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var inquiryName by remember { mutableStateOf("") }
    var inquiryPhone by remember { mutableStateOf("") }
    var inquiryEmail by remember { mutableStateOf("") }
    var inquiryCountry by remember { mutableStateOf("Poland") }
    var inquiryTrade by remember { mutableStateOf("Warehouse Logistics") }
    var inquiryMessage by remember { mutableStateOf("") }
    var inquiryError by remember { mutableStateOf<String?>(null) }
    var showInquirySuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contact & Office Information",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Connect directly with our European visa specialists or visit our registered office in Ranaghat, Nadia, West Bengal.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Contact Directory
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Official Communication Channels",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ContactDirectoryItem(
                        title = "Primary Phone Helpline",
                        value = ContactUtils.PRIMARY_PHONE,
                        icon = Icons.Default.Call,
                        iconBg = NavyRoyal,
                        actionLabel = "Call Now",
                        onAction = { ContactUtils.dialPhone(context, ContactUtils.PRIMARY_PHONE) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ContactDirectoryItem(
                        title = "WhatsApp Helpline 1 (Primary)",
                        value = ContactUtils.WHATSAPP_1,
                        icon = Icons.Default.Share,
                        iconBg = WhatsAppGreen,
                        actionLabel = "Chat",
                        onAction = { ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ContactDirectoryItem(
                        title = "WhatsApp Helpline 2 (Support)",
                        value = ContactUtils.WHATSAPP_2,
                        icon = Icons.Default.Share,
                        iconBg = WhatsAppGreen,
                        actionLabel = "Chat",
                        onAction = { ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_2) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ContactDirectoryItem(
                        title = "IMO International Number",
                        value = ContactUtils.IMO_NUMBER,
                        icon = Icons.Default.Phone,
                        iconBg = Color(0xFF0284C7),
                        actionLabel = "Copy / Call",
                        onAction = {
                            ContactUtils.copyToClipboard(context, "IMO Number", ContactUtils.IMO_NUMBER)
                            ContactUtils.dialPhone(context, ContactUtils.IMO_NUMBER)
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ContactDirectoryItem(
                        title = "Official Email ID",
                        value = ContactUtils.EMAIL_ID,
                        icon = Icons.Default.Email,
                        iconBg = Color(0xFFDC2626),
                        actionLabel = "Compose",
                        onAction = { ContactUtils.openEmail(context) }
                    )
                }
            }
        }

        // Address & Working Hours Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = NavyRoyal, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Registered Office Address",
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Arbi Pori Travel Agency",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = NavyDeep
                    )
                    Text(
                        text = ContactUtils.FULL_ADDRESS,
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Working Hours: Mon - Sat: 9:30 AM to 7:00 PM (Sunday by Appointment)",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { ContactUtils.openMapAddress(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open in Maps", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { ContactUtils.copyToClipboard(context, "Agency Address", ContactUtils.FULL_ADDRESS) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy Address", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Inquiry Option Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.QuestionAnswer, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit Visa / Job Inquiry",
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "Have questions regarding European job eligibility, costs, or embassy process? Send your inquiry below.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = inquiryName,
                        onValueChange = { inquiryName = it },
                        label = { Text("Your Name *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inquiry_name_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inquiryPhone,
                        onValueChange = { inquiryPhone = it },
                        label = { Text("Phone / WhatsApp Number *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inquiry_phone_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = inquiryCountry,
                            onValueChange = { inquiryCountry = it },
                            label = { Text("Target Country") },
                            placeholder = { Text("e.g. Poland, Malta") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = inquiryTrade,
                            onValueChange = { inquiryTrade = it },
                            label = { Text("Preferred Trade") },
                            placeholder = { Text("e.g. Driver, Packer") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inquiryMessage,
                        onValueChange = { inquiryMessage = it },
                        label = { Text("Your Question / Inquiry Details *") },
                        placeholder = { Text("Ask about visa requirements, fees, processing duration...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inquiry_message_input"),
                        shape = RoundedCornerShape(8.dp),
                        maxLines = 3
                    )

                    if (inquiryError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = inquiryError!!, color = Color(0xFFDC2626), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (inquiryName.isBlank()) {
                                inquiryError = "Please enter your name"
                                return@Button
                            }
                            if (inquiryPhone.isBlank() || inquiryPhone.length < 8) {
                                inquiryError = "Please enter your valid phone number"
                                return@Button
                            }
                            if (inquiryMessage.isBlank()) {
                                inquiryError = "Please enter your inquiry details"
                                return@Button
                            }

                            inquiryError = null
                            viewModel.submitInquiry(
                                name = inquiryName.trim(),
                                phone = inquiryPhone.trim(),
                                email = inquiryEmail.trim(),
                                country = inquiryCountry.trim(),
                                trade = inquiryTrade.trim(),
                                message = inquiryMessage.trim(),
                                onSuccess = {
                                    showInquirySuccess = true
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("inquiry_submit_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Inquiry to Agency", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // Success Dialog
    if (showInquirySuccess) {
        AlertDialog(
            onDismissRequest = {
                showInquirySuccess = false
                viewModel.resetInquirySuccess()
            },
            confirmButton = {
                Button(
                    onClick = {
                        val msg = "Hello Arbi Pori Travel Agency, I submitted an inquiry: Name: $inquiryName, Phone: $inquiryPhone, Country: $inquiryCountry, Trade: $inquiryTrade. Message: $inquiryMessage"
                        ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1, msg)
                        showInquirySuccess = false
                        viewModel.resetInquirySuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat on WhatsApp", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showInquirySuccess = false
                        viewModel.resetInquirySuccess()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Close")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Inquiry Received!", fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            },
            text = {
                Text(
                    text = "Thank you $inquiryName! Your inquiry regarding European job opportunities has been submitted to Arbi Pori Travel Agency. Our counselor will respond shortly via phone or WhatsApp.",
                    fontSize = 12.sp,
                    color = Color(0xFF334155),
                    lineHeight = 16.sp
                )
            }
        )
    }
}

@Composable
fun ContactDirectoryItem(
    title: String,
    value: String,
    icon: ImageVector,
    iconBg: Color,
    actionLabel: String,
    onAction: () -> Unit
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = title, fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            }

            Button(
                onClick = onAction,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(text = actionLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
