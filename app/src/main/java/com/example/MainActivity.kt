package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.TravelAgencyDatabase
import com.example.data.repository.TravelAgencyRepository
import com.example.ui.AgencyTab
import com.example.ui.TravelAgencyViewModel
import com.example.ui.TravelAgencyViewModelFactory
import com.example.ui.components.AgencyBottomNav
import com.example.ui.components.AgencyTopBar
import com.example.ui.components.PostPhotoDialog
import com.example.ui.screens.ApplyScreen
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.screens.VacanciesScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: TravelAgencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = TravelAgencyDatabase.getInstance(applicationContext)
        val repository = TravelAgencyRepository(database.agencyDao())
        val factory = TravelAgencyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelAgencyViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainTravelAgencyApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainTravelAgencyApp(viewModel: TravelAgencyViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val showPostPhotoDialog by viewModel.showPostPhotoDialog.collectAsState()

    if (showPostPhotoDialog) {
        PostPhotoDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.closePostPhotoDialog() }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AgencyTopBar(
                onContactClick = { viewModel.switchTab(AgencyTab.CONTACT) }
            )
        },
        bottomBar = {
            AgencyBottomNav(
                currentTab = currentTab,
                onTabSelected = { viewModel.switchTab(it) }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AgencyTab.HOME -> HomeScreen(viewModel = viewModel)
                AgencyTab.JOBS -> VacanciesScreen(viewModel = viewModel)
                AgencyTab.APPLY -> ApplyScreen(viewModel = viewModel)
                AgencyTab.TRACK -> TrackingScreen(viewModel = viewModel)
                AgencyTab.BOOK -> BookingScreen(viewModel = viewModel)
                AgencyTab.PAY -> PaymentScreen(viewModel = viewModel)
                AgencyTab.CONTACT -> ContactScreen(viewModel = viewModel)
            }
        }
    }
}

// Retained for test compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
