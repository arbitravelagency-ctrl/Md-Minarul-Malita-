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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PaymentRecord
import com.example.ui.TravelAgencyViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.PaymentGPayBlue
import com.example.ui.theme.PaymentPaytmBlue
import com.example.ui.theme.PaymentUpiPurple
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ContactUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: TravelAgencyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val payments by viewModel.allPayments.collectAsState()

    val paymentOptions = listOf("PhonePe", "Google Pay", "Paytm", "Bank Account")
    var selectedMethod by remember { mutableStateOf("PhonePe") }

    val commonPackages = listOf(
        "₹5,000 (File Opening & Registration)",
        "₹3,500 (Document Verification & PCC)",
        "₹25,000 (Work Permit Advance)",
        "₹10,000 (VFS Appointment Booking)",
        "Other Custom Amount"
    )
    var selectedPackage by remember { mutableStateOf(commonPackages[0]) }

    var candidateName by remember { mutableStateOf("") }
    var trackingOrBookingId by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("5000") }
    var utrNumber by remember { mutableStateOf("") }
    var receiptPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val receiptPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            receiptPhotoUri = uri
        }
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var confirmedReceiptId by remember { mutableStateOf<String?>(null) }

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
                        Icon(Icons.Default.Payment, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Official Payment System",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Pay your visa application fees securely via PhonePe, Google Pay, Paytm, or direct HDFC Bank transfer.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Method Selector Cards
        item {
            Text(
                text = "Select Payment Channel:",
                fontWeight = FontWeight.Bold,
                color = NavyDeep,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentMethodSelectable(
                    name = "PhonePe",
                    brandColor = PaymentUpiPurple,
                    isSelected = selectedMethod == "PhonePe",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedMethod = "PhonePe" }
                )
                PaymentMethodSelectable(
                    name = "Google Pay",
                    brandColor = PaymentGPayBlue,
                    isSelected = selectedMethod == "Google Pay",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedMethod = "Google Pay" }
                )
                PaymentMethodSelectable(
                    name = "Paytm",
                    brandColor = PaymentPaytmBlue,
                    isSelected = selectedMethod == "Paytm",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedMethod = "Paytm" }
                )
                PaymentMethodSelectable(
                    name = "Bank A/C",
                    brandColor = NavyDeep,
                    isSelected = selectedMethod == "Bank Account",
                    modifier = Modifier.weight(1f),
                    onClick = { selectedMethod = "Bank Account" }
                )
            }
        }

        // Active Payment Details Display Card
        item {
            if (selectedMethod == "Bank Account") {
                // Bank Details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Official Bank Account Details", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyDeep)
                                Text("NEFT / RTGS / IMPS Bank Transfer", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        BankDetailRow("Account Holder", ContactUtils.BANK_ACCOUNT_NAME)
                        BankDetailRow("Account Number", ContactUtils.BANK_ACCOUNT_NUMBER)
                        BankDetailRow("IFSC Code", ContactUtils.BANK_IFSC)
                        BankDetailRow("Bank Name", ContactUtils.BANK_NAME)
                        BankDetailRow("Branch", ContactUtils.BANK_BRANCH)
                        BankDetailRow("Account Type", "Current Account")

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val bankText = """
                                    Bank: ${ContactUtils.BANK_NAME}
                                    Account Name: ${ContactUtils.BANK_ACCOUNT_NAME}
                                    Account Number: ${ContactUtils.BANK_ACCOUNT_NUMBER}
                                    IFSC: ${ContactUtils.BANK_IFSC}
                                    Branch: ${ContactUtils.BANK_BRANCH}
                                """.trimIndent()
                                ContactUtils.copyToClipboard(context, "Bank Details", bankText)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Full Bank Account Details", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // UPI Apps (PhonePe / Google Pay / Paytm)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val brandColor = when (selectedMethod) {
                            "PhonePe" -> PaymentUpiPurple
                            "Google Pay" -> PaymentGPayBlue
                            else -> PaymentPaytmBlue
                        }

                        Surface(
                            color = brandColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "PAY VIA $selectedMethod",
                                color = brandColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = ContactUtils.UPI_ID,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = NavyDeep
                        )
                        Text(
                            text = "Name: ${ContactUtils.UPI_NAME}",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    ContactUtils.launchUpiPayment(
                                        context = context,
                                        upiId = ContactUtils.UPI_ID,
                                        name = ContactUtils.UPI_NAME,
                                        amount = amount,
                                        note = "Arbi Pori Travel Agency Fee"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = brandColor),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pay_launch_upi_btn")
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pay with $selectedMethod", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { ContactUtils.copyToClipboard(context, "UPI ID", ContactUtils.UPI_ID) },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy UPI")
                            }
                        }
                    }
                }
            }
        }

        // Quick Fee Select
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Choose Service Package or Amount:", fontWeight = FontWeight.Bold, color = NavyDeep, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    commonPackages.forEach { pkg ->
                        val isSelected = selectedPackage == pkg
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    selectedPackage = pkg
                                    when {
                                        pkg.contains("5,000") -> amount = "5000"
                                        pkg.contains("3,500") -> amount = "3500"
                                        pkg.contains("25,000") -> amount = "25000"
                                        pkg.contains("10,000") -> amount = "10000"
                                    }
                                }
                                .background(if (isSelected) Color(0xFFEFF6FF) else Color.Transparent)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, if (isSelected) NavyRoyal else Color(0xFF94A3B8), CircleShape)
                                    .background(if (isSelected) NavyRoyal else Color.Transparent)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = pkg,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NavyDeep else Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }

        // Submit UTR Confirmation Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Submit UTR / Transaction Proof",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "After transferring, enter your 12-digit UTR/Ref ID for instant verification.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = candidateName,
                        onValueChange = { candidateName = it },
                        label = { Text("Candidate Name *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("payment_candidate_name"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = trackingOrBookingId,
                        onValueChange = { trackingOrBookingId = it.uppercase() },
                        label = { Text("Tracking Number or Booking ID (Optional)") },
                        placeholder = { Text("e.g. APTA-EU-1082") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount (₹) *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("payment_amount_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = utrNumber,
                            onValueChange = { utrNumber = it },
                            label = { Text("12-Digit UTR / Ref No. *") },
                            placeholder = { Text("e.g. 423984019283") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("payment_utr_input"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Receipt Screenshot Upload Box
                    Text(
                        text = "Attach Payment Screenshot / Receipt (Optional)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = NavyDeep
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (receiptPhotoUri != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFE2E8F0))
                            ) {
                                AsyncImage(
                                    model = receiptPhotoUri,
                                    contentDescription = "Receipt Screenshot",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Receipt Image Attached", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                                Text("Will be verified with HDFC statement", fontSize = 10.sp, color = Color(0xFF64748B))
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                OutlinedButton(
                                    onClick = {
                                        receiptPhotoLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Change", fontSize = 10.sp)
                                }

                                OutlinedButton(
                                    onClick = { receiptPhotoUri = null },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Delete", fontSize = 10.sp)
                                }
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    receiptPhotoLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Upload Receipt",
                                    tint = NavyRoyal,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Post / Upload Payment Slip Photo",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NavyDeep
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMessage!!, color = Color(0xFFDC2626), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (candidateName.isBlank()) {
                                errorMessage = "Please enter candidate name"
                                return@Button
                            }
                            if (amount.isBlank()) {
                                errorMessage = "Please enter amount paid"
                                return@Button
                            }
                            if (utrNumber.isBlank() || utrNumber.length < 6) {
                                errorMessage = "Please enter valid UTR / Transaction Reference Number"
                                return@Button
                            }

                            errorMessage = null
                            viewModel.submitPaymentConfirmation(
                                candidateName = candidateName.trim(),
                                trackingOrBookingId = trackingOrBookingId.trim(),
                                amount = "₹$amount",
                                paymentMethod = selectedMethod,
                                utr = utrNumber.trim(),
                                serviceType = selectedPackage,
                                receiptPhotoUri = receiptPhotoUri?.toString(),
                                onSuccess = { receiptId ->
                                    confirmedReceiptId = receiptId
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("payment_submit_button")
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Payment Confirmation", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Payment History
        if (payments.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Payment Submissions (${payments.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep
                    )
                )
            }

            items(payments) { p ->
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
                            Text(text = p.receiptId, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDeep)
                            Text(text = p.amount, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = SuccessGreen)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Method: ${p.paymentMethod} • UTR: ${p.transactionUtr}", fontSize = 11.sp, color = Color(0xFF475569))
                        Text(text = "Candidate: ${p.candidateName} • ${p.paymentDate}", fontSize = 10.sp, color = Color(0xFF64748B))
                    }
                }
            }
        }
    }

    // Success Payment Dialog
    confirmedReceiptId?.let { rId ->
        AlertDialog(
            onDismissRequest = {
                confirmedReceiptId = null
                viewModel.clearLastPaymentReceipt()
            },
            confirmButton = {
                Button(
                    onClick = {
                        val msg = "Hello Arbi Pori Travel Agency, I have submitted payment for European Visa processing. Receipt ID: $rId, Candidate: $candidateName, Amount: ₹$amount, Method: $selectedMethod, UTR: $utrNumber."
                        ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1, msg)
                        confirmedReceiptId = null
                        viewModel.clearLastPaymentReceipt()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Send Slip on WhatsApp", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        ContactUtils.copyToClipboard(context, "Receipt ID", rId)
                        confirmedReceiptId = null
                        viewModel.clearLastPaymentReceipt()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy Receipt ID")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Payment Recorded!", fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Your payment transaction has been registered with Arbi Pori Travel Agency accounts desk.", fontSize = 12.sp)
                    Surface(
                        color = NavyDeep,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "ACKNOWLEDGMENT RECEIPT", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(text = rId, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            Text(text = "Amount: ₹$amount ($selectedMethod)", color = Color(0xFFE2E8F0), fontSize = 12.sp)
                            Text(text = "UTR: $utrNumber", color = Color(0xFF94A3B8), fontSize = 11.sp)
                        }
                    }

                    if (receiptPhotoUri != null) {
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
                                    model = receiptPhotoUri,
                                    contentDescription = "Receipt Slip",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(45.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Payment Slip Attached", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                                Text("Forwarded to Arbi Pori accounts department", fontSize = 10.sp, color = Color(0xFF64748B))
                            }
                        }
                    }

                    Text(
                        text = "Our accounts team will verify with HDFC bank statement and issue the signed voucher within 2-4 hours.",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )
                }
            }
        )
    }
}

@Composable
fun PaymentMethodSelectable(
    name: String,
    brandColor: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) brandColor else Color(0xFFF1F5F9),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) brandColor else Color(0xFFCBD5E1))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White else brandColor)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else NavyDeep,
                maxLines = 1
            )
        }
    }
}

@Composable
fun BankDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
    }
}
