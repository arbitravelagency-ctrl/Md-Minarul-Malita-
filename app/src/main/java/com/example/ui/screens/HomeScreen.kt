package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.JobVacancy
import com.example.ui.AgencyTab
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

@Composable
fun HomeScreen(
    viewModel: TravelAgencyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vacancies by viewModel.allVacancies.collectAsState()
    val postedPhotos by viewModel.allPostedPhotos.collectAsState()
    val hotVacancies = vacancies.filter { it.isHotVacancy }.take(4)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_hero_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = NavyDeep),
                elevation = CardDefaults.elevatedCardElevation(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NavyDeep, NavyRoyal, Color(0xFF172554))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(2.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_agency_logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Surface(
                                    color = GoldAccent.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "EUROPE RECRUITMENT DESK",
                                        color = GoldAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "ARBI PORI TRAVEL AGENCY",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Poland • Malta • Romania • Germany • Croatia",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFE2E8F0),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Trusted overseas employment and visa consultancy. Complete assistance with Work Permits, Embassy Submission, VFS Stamping, and Pre-Departure Guidance.",
                            color = Color(0xFFCBD5E1),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Contact row inside hero
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.switchTab(AgencyTab.JOBS) },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("hero_explore_jobs_button")
                            ) {
                                Icon(Icons.Default.Work, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("View Jobs", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.switchTab(AgencyTab.APPLY) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("hero_apply_now_button")
                            ) {
                                Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp), tint = NavyDeep)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Apply Now", color = NavyDeep, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Direct Contact Strip
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Instant Contact & Support Options",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        QuickContactButton(
                            label = "Call Us",
                            subtext = "8945502983",
                            icon = Icons.Default.Call,
                            iconBg = NavyRoyal,
                            onClick = { ContactUtils.dialPhone(context, ContactUtils.PRIMARY_PHONE) },
                            testTag = "contact_call_btn"
                        )
                        QuickContactButton(
                            label = "WhatsApp 1",
                            subtext = "8945502983",
                            icon = Icons.Default.Share,
                            iconBg = WhatsAppGreen,
                            onClick = { ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_1) },
                            testTag = "contact_wa1_btn"
                        )
                        QuickContactButton(
                            label = "WhatsApp 2",
                            subtext = "9775696790",
                            icon = Icons.Default.Share,
                            iconBg = WhatsAppGreen,
                            onClick = { ContactUtils.openWhatsApp(context, ContactUtils.WHATSAPP_2) },
                            testTag = "contact_wa2_btn"
                        )
                        QuickContactButton(
                            label = "IMO Call",
                            subtext = "8945502983",
                            icon = Icons.Default.Phone,
                            iconBg = Color(0xFF0284C7),
                            onClick = { ContactUtils.copyToClipboard(context, "IMO Number", ContactUtils.IMO_NUMBER) },
                            testTag = "contact_imo_btn"
                        )
                    }
                }
            }
        }

        // Core App Features Grid
        item {
            Text(
                text = "Services & Management Portals",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ServiceFeatureCard(
                        title = "European Vacancies",
                        desc = "8+ Countries • High Salary",
                        icon = Icons.Default.Work,
                        badge = "Updated",
                        badgeColor = SuccessGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AgencyTab.JOBS) },
                        testTag = "service_vacancies_card"
                    )
                    ServiceFeatureCard(
                        title = "Apply Online",
                        desc = "Get Tracking ID instantly",
                        icon = Icons.Default.Assignment,
                        badge = "Fast Visa",
                        badgeColor = GoldAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AgencyTab.APPLY) },
                        testTag = "service_apply_card"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ServiceFeatureCard(
                        title = "Track Application",
                        desc = "Live 6-Stage Visa Tracker",
                        icon = Icons.Default.TrackChanges,
                        badge = "Live Status",
                        badgeColor = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AgencyTab.TRACK) },
                        testTag = "service_track_card"
                    )
                    ServiceFeatureCard(
                        title = "Book Appointment",
                        desc = "Ranaghat Office / Video",
                        icon = Icons.Default.CalendarMonth,
                        badge = "Schedule",
                        badgeColor = Color(0xFF9333EA),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AgencyTab.BOOK) },
                        testTag = "service_book_card"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ServiceFeatureCard(
                        title = "Payment Portal",
                        desc = "PhonePe, GPay, Paytm, Bank",
                        icon = Icons.Default.Payment,
                        badge = "Secure UPI",
                        badgeColor = PaymentUpiPurple,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AgencyTab.PAY) },
                        testTag = "service_pay_card"
                    )
                    ServiceFeatureCard(
                        title = "Agency Inquiry",
                        desc = "Ask questions & get advice",
                        icon = Icons.Default.QuestionAnswer,
                        badge = "24/7 Help",
                        badgeColor = Color(0xFF0D9488),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AgencyTab.CONTACT) },
                        testTag = "service_inquiry_card"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ServiceFeatureCard(
                        title = "Post Photo",
                        desc = "Upload passport, visa docs",
                        icon = Icons.Default.AddPhotoAlternate,
                        badge = "Upload",
                        badgeColor = GoldAccent,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openPostPhotoDialog() },
                        testTag = "service_post_photo_card"
                    )
                    ServiceFeatureCard(
                        title = "Contact Desk",
                        desc = "WhatsApp, Call & Address",
                        icon = Icons.Default.Call,
                        badge = "Matiari Banpur",
                        badgeColor = Color(0xFF2563EB),
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.switchTab(AgencyTab.CONTACT) },
                        testTag = "service_contact_desk_card"
                    )
                }
            }
        }

        // Uploaded Photos & Documents Section (if any)
        if (postedPhotos.isNotEmpty()) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Uploaded Photos & Documents (${postedPhotos.size})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep
                            )
                        )
                        OutlinedButton(
                            onClick = { viewModel.openPostPhotoDialog() },
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add More", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(postedPhotos) { photo ->
                            Card(
                                modifier = Modifier
                                    .width(160.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .background(Color(0xFFE2E8F0))
                                    ) {
                                        AsyncImage(
                                            model = photo.photoUri,
                                            contentDescription = photo.candidateName,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Surface(
                                            color = NavyDeep.copy(alpha = 0.85f),
                                            shape = RoundedCornerShape(bottomEnd = 6.dp),
                                            modifier = Modifier.align(Alignment.TopStart)
                                        ) {
                                            Text(
                                                text = photo.photoType,
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = photo.candidateName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = NavyDeep,
                                            maxLines = 1
                                        )
                                        if (photo.trackingNumber.isNotBlank()) {
                                            Text(
                                                text = "ID: ${photo.trackingNumber}",
                                                fontSize = 9.sp,
                                                color = NavyRoyal,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Text(
                                            text = photo.datePosted,
                                            fontSize = 9.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Payment Options Banner Preview
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.switchTab(AgencyTab.PAY) }
                    .testTag("home_payment_options_banner"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Accepted Payment Methods",
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep,
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Pay Online",
                                color = NavyRoyal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = NavyRoyal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaymentBrandChip("PhonePe", PaymentUpiPurple, Modifier.weight(1f))
                        PaymentBrandChip("Google Pay", PaymentGPayBlue, Modifier.weight(1f))
                        PaymentBrandChip("Paytm", PaymentPaytmBlue, Modifier.weight(1f))
                        PaymentBrandChip("Bank A/C", NavyDeep, Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Official UPI: ${ContactUtils.UPI_ID} | HDFC Bank Account available with receipt confirmation",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // Featured Hot Vacancies Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Urgent European Job Openings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep
                        )
                    )
                    Text(
                        text = "Work permit process open with guaranteed interview",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                    )
                }
                Text(
                    text = "See All (${vacancies.size})",
                    color = NavyRoyal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { viewModel.switchTab(AgencyTab.JOBS) }
                )
            }
        }

        // Hot Vacancies List
        items(hotVacancies) { vacancy ->
            HomeVacancyItem(
                vacancy = vacancy,
                onApply = { viewModel.applyForJob(vacancy) },
                onDetails = {
                    viewModel.selectVacancy(vacancy)
                    viewModel.switchTab(AgencyTab.JOBS)
                }
            )
        }

        // European Destinations row
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Top European Destinations",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val destinations = listOf(
                        Triple("Poland", "🇵🇱", "60-75 Days"),
                        Triple("Malta", "🇲🇹", "45-60 Days"),
                        Triple("Romania", "🇷🇴", "50-65 Days"),
                        Triple("Germany", "🇩🇪", "90-120 Days"),
                        Triple("Lithuania", "🇱🇹", "60-90 Days"),
                        Triple("Croatia", "🇭🇷", "45-60 Days")
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(destinations) { (country, flag, time) ->
                            Surface(
                                color = Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .clickable {
                                        viewModel.selectedCountryFilter.value = country
                                        viewModel.switchTab(AgencyTab.JOBS)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = flag, fontSize = 24.sp)
                                    Text(text = country, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                                    Text(text = time, fontSize = 10.sp, color = Color(0xFF64748B))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Office Address Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Main Registered Office",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = ContactUtils.FULL_ADDRESS,
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { ContactUtils.openMapAddress(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Open in Maps", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.switchTab(AgencyTab.BOOK) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Book Visit", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickContactButton(
    label: String,
    subtext: String,
    icon: ImageVector,
    iconBg: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = NavyDeep)
        Text(text = subtext, fontSize = 9.sp, color = Color(0xFF64748B))
    }
}

@Composable
fun ServiceFeatureCard(
    title: String,
    desc: String,
    icon: ImageVector,
    badge: String,
    badgeColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavyRoyal.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = NavyRoyal, modifier = Modifier.size(18.dp))
                }

                Surface(
                    color = badgeColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = badge,
                        color = badgeColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyDeep)
            Text(text = desc, fontSize = 10.sp, color = Color(0xFF64748B), maxLines = 1)
        }
    }
}

@Composable
fun PaymentBrandChip(
    name: String,
    brandColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = brandColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, brandColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(brandColor)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
        }
    }
}

@Composable
fun HomeVacancyItem(
    vacancy: JobVacancy,
    onApply: () -> Unit,
    onDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDetails),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = vacancy.countryFlag, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${vacancy.country} • ${vacancy.city}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = NavyRoyal
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = vacancy.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyDeep
                    )
                }

                Surface(
                    color = SuccessGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${vacancy.vacanciesCount} Open",
                        color = SuccessGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Salary & processing tags
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Salary (Euro / INR)", fontSize = 9.sp, color = Color(0xFF64748B))
                    Text(
                        text = "${vacancy.salaryEur} (${vacancy.salaryInr})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Processing", fontSize = 9.sp, color = Color(0xFF64748B))
                    Text(
                        text = vacancy.processingTime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NavyDeep
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDetails,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Text("Details", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Text("Apply Now", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
