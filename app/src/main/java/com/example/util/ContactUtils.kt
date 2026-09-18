package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object ContactUtils {
    const val PRIMARY_PHONE = "+918945502983"
    const val WHATSAPP_1 = "+918945502983"
    const val WHATSAPP_2 = "+919775696790"
    const val IMO_NUMBER = "+918945502983"
    const val EMAIL_ID = "arbitravelagency@gmail.com"
    const val FULL_ADDRESS = "Matiari Banpur, Ranaghat, Nadia, Kolkata, West Bengal, India - 741502"

    // Payment details
    const val UPI_ID = "8945502983@okhdfcbank"
    const val UPI_NAME = "Arbi Pori Travel Agency"
    const val BANK_NAME = "HDFC Bank"
    const val BANK_ACCOUNT_NAME = "Arbi Pori Travel Agency"
    const val BANK_ACCOUNT_NUMBER = "50200084729183"
    const val BANK_IFSC = "HDFC0001254"
    const val BANK_BRANCH = "Ranaghat Branch, Nadia, WB"

    fun dialPhone(context: Context, number: String = PRIMARY_PHONE) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${number.replace(" ", "")}")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open dialer: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsApp(context: Context, number: String = WHATSAPP_1, message: String = "") {
        try {
            val cleanNumber = number.replace("+", "").replace(" ", "")
            val encodedMsg = Uri.encode(message.ifBlank { "Hello Arbi Pori Travel Agency, I am interested in European job vacancies." })
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openEmail(context: Context, email: String = EMAIL_ID, subject: String = "Inquiry: European Job Vacancy", body: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                if (body.isNotBlank()) putExtra(Intent.EXTRA_TEXT, body)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open email client: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMapAddress(context: Context, address: String = FULL_ADDRESS) {
        try {
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback to web browser
            val webUri = Uri.parse("https://maps.google.com/?q=" + Uri.encode(address))
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied $label: $text", Toast.LENGTH_SHORT).show()
    }

    fun launchUpiPayment(context: Context, upiId: String, name: String, amount: String, note: String) {
        try {
            val cleanAmount = amount.replace(",", "").replace("₹", "").trim()
            val uriStr = "upi://pay?pa=$upiId&pn=${Uri.encode(name)}&tn=${Uri.encode(note)}" +
                    if (cleanAmount.isNotBlank() && cleanAmount.toDoubleOrNull() != null) "&am=$cleanAmount&cu=INR" else ""
            val uri = Uri.parse(uriStr)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(Intent.createChooser(intent, "Pay using UPI App"))
        } catch (e: Exception) {
            copyToClipboard(context, "UPI ID", upiId)
            Toast.makeText(context, "UPI App not found. UPI ID copied to clipboard: $upiId", Toast.LENGTH_LONG).show()
        }
    }
}
