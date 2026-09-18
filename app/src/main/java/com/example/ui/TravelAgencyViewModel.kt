package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Booking
import com.example.data.model.Inquiry
import com.example.data.model.JobApplication
import com.example.data.model.JobVacancy
import com.example.data.model.PaymentRecord
import com.example.data.model.PostedPhoto
import com.example.data.repository.TravelAgencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AgencyTab(val label: String) {
    HOME("Home"),
    JOBS("Vacancies"),
    APPLY("Apply"),
    TRACK("Tracking"),
    BOOK("Book Slot"),
    PAY("Payments"),
    CONTACT("Contact")
}

sealed interface TrackingState {
    data object Idle : TrackingState
    data object Searching : TrackingState
    data class Found(val application: JobApplication) : TrackingState
    data class NotFound(val searchedQuery: String) : TrackingState
}

class TravelAgencyViewModel(
    private val repository: TravelAgencyRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.populateInitialDataIfEmpty()
        }
    }

    val allVacancies: StateFlow<List<JobVacancy>> = repository.allVacancies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allApplications: StateFlow<List<JobApplication>> = repository.allApplications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPayments: StateFlow<List<PaymentRecord>> = repository.allPayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInquiries: StateFlow<List<Inquiry>> = repository.allInquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPostedPhotos: StateFlow<List<PostedPhoto>> = repository.allPostedPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Post Photo Global Sheet State
    private val _showPostPhotoDialog = MutableStateFlow(false)
    val showPostPhotoDialog: StateFlow<Boolean> = _showPostPhotoDialog.asStateFlow()

    private val _prefilledTrackingForPhoto = MutableStateFlow("")
    val prefilledTrackingForPhoto: StateFlow<String> = _prefilledTrackingForPhoto.asStateFlow()

    fun openPostPhotoDialog(prefilledTracking: String = "") {
        _prefilledTrackingForPhoto.value = prefilledTracking
        _showPostPhotoDialog.value = true
    }

    fun closePostPhotoDialog() {
        _showPostPhotoDialog.value = false
        _prefilledTrackingForPhoto.value = ""
    }

    // Navigation & View State
    private val _currentTab = MutableStateFlow(AgencyTab.HOME)
    val currentTab: StateFlow<AgencyTab> = _currentTab.asStateFlow()

    fun switchTab(tab: AgencyTab) {
        _currentTab.value = tab
    }

    // Vacancies filter & search
    val selectedCountryFilter = MutableStateFlow("All")
    val searchQuery = MutableStateFlow("")

    val filteredVacancies: StateFlow<List<JobVacancy>> = combine(
        allVacancies,
        selectedCountryFilter,
        searchQuery
    ) { vacancies, country, query ->
        vacancies.filter { vacancy ->
            val matchesCountry = country == "All" || vacancy.country.equals(country, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    vacancy.title.contains(query, ignoreCase = true) ||
                    vacancy.country.contains(query, ignoreCase = true) ||
                    vacancy.category.contains(query, ignoreCase = true) ||
                    vacancy.city.contains(query, ignoreCase = true)
            matchesCountry && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected vacancy for detail view or apply
    private val _selectedVacancy = MutableStateFlow<JobVacancy?>(null)
    val selectedVacancy: StateFlow<JobVacancy?> = _selectedVacancy.asStateFlow()

    fun selectVacancy(vacancy: JobVacancy?) {
        _selectedVacancy.value = vacancy
    }

    fun applyForJob(vacancy: JobVacancy) {
        _selectedVacancy.value = vacancy
        _currentTab.value = AgencyTab.APPLY
    }

    // Application submission
    private val _lastGeneratedTrackingId = MutableStateFlow<String?>(null)
    val lastGeneratedTrackingId: StateFlow<String?> = _lastGeneratedTrackingId.asStateFlow()

    fun clearLastTrackingId() {
        _lastGeneratedTrackingId.value = null
    }

    fun submitApplication(
        name: String,
        passport: String,
        phone: String,
        whatsapp: String,
        email: String,
        address: String,
        jobTitle: String,
        country: String,
        experience: String,
        education: String,
        photoUri: String? = null,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val trackingId = repository.submitApplication(
                name = name,
                passport = passport,
                phone = phone,
                whatsapp = whatsapp,
                email = email,
                address = address,
                jobTitle = jobTitle,
                country = country,
                experience = experience,
                education = education,
                photoUri = photoUri
            )
            _lastGeneratedTrackingId.value = trackingId
            onSuccess(trackingId)
        }
    }

    // Tracking screen state
    val trackingInput = MutableStateFlow("")
    private val _trackingState = MutableStateFlow<TrackingState>(TrackingState.Idle)
    val trackingState: StateFlow<TrackingState> = _trackingState.asStateFlow()

    fun searchTracking(id: String) {
        val trimmed = id.trim()
        if (trimmed.isBlank()) return
        trackingInput.value = trimmed
        _trackingState.value = TrackingState.Searching
        viewModelScope.launch {
            val app = repository.getApplication(trimmed)
            if (app != null) {
                _trackingState.value = TrackingState.Found(app)
            } else {
                _trackingState.value = TrackingState.NotFound(trimmed)
            }
        }
    }

    fun selectApplicationToTrack(app: JobApplication) {
        trackingInput.value = app.trackingNumber
        _trackingState.value = TrackingState.Found(app)
        _currentTab.value = AgencyTab.TRACK
    }

    // Booking system
    private val _lastBookingId = MutableStateFlow<String?>(null)
    val lastBookingId: StateFlow<String?> = _lastBookingId.asStateFlow()

    fun clearLastBookingId() {
        _lastBookingId.value = null
    }

    fun bookAppointment(
        name: String,
        phone: String,
        email: String,
        date: String,
        timeSlot: String,
        consultationType: String,
        country: String,
        purpose: String,
        notes: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val bookingId = repository.createBooking(
                name = name,
                phone = phone,
                email = email,
                date = date,
                timeSlot = timeSlot,
                consultationType = consultationType,
                country = country,
                purpose = purpose,
                notes = notes
            )
            _lastBookingId.value = bookingId
            onSuccess(bookingId)
        }
    }

    // Payment system
    private val _lastPaymentReceipt = MutableStateFlow<String?>(null)
    val lastPaymentReceipt: StateFlow<String?> = _lastPaymentReceipt.asStateFlow()

    fun clearLastPaymentReceipt() {
        _lastPaymentReceipt.value = null
    }

    fun submitPaymentConfirmation(
        candidateName: String,
        trackingOrBookingId: String,
        amount: String,
        paymentMethod: String,
        utr: String,
        serviceType: String,
        receiptPhotoUri: String? = null,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val receiptId = repository.submitPayment(
                candidateName = candidateName,
                trackingOrBookingId = trackingOrBookingId,
                amount = amount,
                paymentMethod = paymentMethod,
                utr = utr,
                serviceType = serviceType,
                receiptPhotoUri = receiptPhotoUri
            )
            _lastPaymentReceipt.value = receiptId
            onSuccess(receiptId)
        }
    }

    // Photo posting operations
    fun postPhoto(
        candidateName: String,
        trackingNumber: String = "",
        photoType: String,
        photoUri: String,
        notes: String = "",
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val photoId = repository.postPhoto(
                candidateName = candidateName,
                trackingNumber = trackingNumber,
                photoType = photoType,
                photoUri = photoUri,
                notes = notes
            )
            // If the user was viewing this tracking number, re-fetch to reflect photo immediately
            if (trackingNumber.isNotBlank()) {
                val updatedApp = repository.getApplication(trackingNumber)
                if (updatedApp != null) {
                    _trackingState.value = TrackingState.Found(updatedApp)
                }
            }
            onSuccess(photoId)
        }
    }

    fun updateApplicationPhoto(trackingNumber: String, photoUri: String) {
        viewModelScope.launch {
            repository.updateApplicationPhoto(trackingNumber, photoUri)
            val updatedApp = repository.getApplication(trackingNumber)
            if (updatedApp != null) {
                _trackingState.value = TrackingState.Found(updatedApp)
            }
        }
    }

    // Inquiry submission
    private val _inquirySuccess = MutableStateFlow(false)
    val inquirySuccess: StateFlow<Boolean> = _inquirySuccess.asStateFlow()

    fun resetInquirySuccess() {
        _inquirySuccess.value = false
    }

    fun submitInquiry(
        name: String,
        phone: String,
        email: String,
        country: String,
        trade: String,
        message: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.submitInquiry(
                name = name,
                phone = phone,
                email = email,
                country = country,
                trade = trade,
                message = message
            )
            _inquirySuccess.value = true
            onSuccess()
        }
    }
}

class TravelAgencyViewModelFactory(
    private val repository: TravelAgencyRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelAgencyViewModel::class.java)) {
            return TravelAgencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
