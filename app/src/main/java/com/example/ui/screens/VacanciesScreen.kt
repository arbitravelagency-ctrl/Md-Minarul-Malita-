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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobVacancy
import com.example.ui.TravelAgencyViewModel
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WhatsAppGreen
import com.example.util.ContactUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacanciesScreen(
    viewModel: TravelAgencyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vacancies by viewModel.filteredVacancies.collectAsState()
    val selectedCountry by viewModel.selectedCountryFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedVacancy by viewModel.selectedVacancy.collectAsState()

    val countries = listOf("All", "Poland", "Malta", "Romania", "Germany", "Lithuania", "Croatia", "Hungary", "Czech Republic")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Search & Filter Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "European Job Vacancies",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep
                    )
                )
                Text(
                    text = "Approved European work permit positions for Indian citizens",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Search by trade, country or city...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NavyRoyal) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vacancies_search_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Country Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(countries) { country ->
                        val isSelected = selectedCountry.equals(country, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectedCountryFilter.value = country },
                            label = { Text(country, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyDeep,
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFFF1F5F9),
                                labelColor = Color(0xFF334155)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("filter_chip_$country")
                        )
                    }
                }
            }
        }

        // List of Vacancies
        if (vacancies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No vacancies found",
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Try clearing filters or check other European countries.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.selectedCountryFilter.value = "All"
                            viewModel.searchQuery.value = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyDeep)
                    ) {
                        Text("Reset Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "Showing ${vacancies.size} Available European Vacancies",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                }

                items(vacancies, key = { it.id }) { vacancy ->
                    VacancyDetailedCard(
                        vacancy = vacancy,
                        onApply = { viewModel.applyForJob(vacancy) },
                        onDetails = { viewModel.selectVacancy(vacancy) }
                    )
                }
            }
        }
    }

    // Detail Dialog
    selectedVacancy?.let { vacancy ->
        VacancyDetailDialog(
            vacancy = vacancy,
            onDismiss = { viewModel.selectVacancy(null) },
            onApply = {
                viewModel.selectVacancy(null)
                viewModel.applyForJob(vacancy)
            }
        )
    }
}

@Composable
fun VacancyDetailedCard(
    vacancy: JobVacancy,
    onApply: () -> Unit,
    onDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDetails)
            .testTag("vacancy_card_${vacancy.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = vacancy.countryFlag, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${vacancy.country} • ${vacancy.city}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = NavyRoyal
                        )
                        Text(
                            text = vacancy.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = NavyDeep
                        )
                    }
                }

                Surface(
                    color = SuccessGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${vacancy.vacanciesCount} Vacancies",
                        color = SuccessGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Highlight bar: Salary & Processing Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Monthly Earnings", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(
                        text = vacancy.salaryEur,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFFB45309)
                    )
                    Text(
                        text = vacancy.salaryInr,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Contract & Process", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(
                        text = vacancy.contractDuration,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NavyDeep
                    )
                    Text(
                        text = "⏳ ${vacancy.processingTime}",
                        fontSize = 10.sp,
                        color = Color(0xFF2563EB)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Benefits preview
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = vacancy.benefits,
                    fontSize = 11.sp,
                    color = Color(0xFF334155),
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDetails,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Details", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyDeep)
                ) {
                    Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Apply Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun VacancyDetailDialog(
    vacancy: JobVacancy,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Proceed to Apply", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Close")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = vacancy.countryFlag, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = vacancy.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NavyDeep
                    )
                    Text(
                        text = "${vacancy.country} • ${vacancy.city}",
                        fontSize = 12.sp,
                        color = NavyRoyal
                    )
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "Salary & Contract", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                            Text(text = "EUR: ${vacancy.salaryEur}", fontWeight = FontWeight.Bold, color = Color(0xFFB45309), fontSize = 12.sp)
                            Text(text = "INR Equivalent: ${vacancy.salaryInr}", fontSize = 11.sp, color = Color(0xFF475569))
                            Text(text = "Duration: ${vacancy.contractDuration}", fontSize = 11.sp)
                            Text(text = "Visa Processing Time: ${vacancy.processingTime}", fontSize = 11.sp)
                            Text(text = "Open Vacancies: ${vacancy.vacanciesCount} Positions", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item {
                    Text(text = "Job Responsibilities & Scope", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                    Text(text = vacancy.description, fontSize = 11.sp, color = Color(0xFF334155), lineHeight = 16.sp)
                }

                item {
                    Text(text = "Requirements & Eligibility", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                    Text(text = vacancy.requirements, fontSize = 11.sp, color = Color(0xFF334155), lineHeight = 16.sp)
                }

                item {
                    Text(text = "Benefits & Facilities", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NavyDeep)
                    Text(text = vacancy.benefits, fontSize = 11.sp, color = Color(0xFF334155), lineHeight = 16.sp)
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            ContactUtils.openWhatsApp(
                                context,
                                ContactUtils.WHATSAPP_1,
                                "Hello Arbi Pori Travel Agency, I want more information regarding Job Vacancy: ${vacancy.title} in ${vacancy.country}."
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Inquire on WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    )
}
