package com.example.data.repository

import com.example.data.local.TravelAgencyDao
import com.example.data.model.Booking
import com.example.data.model.Inquiry
import com.example.data.model.JobApplication
import com.example.data.model.JobVacancy
import com.example.data.model.PaymentRecord
import com.example.data.model.PostedPhoto
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class TravelAgencyRepository(private val dao: TravelAgencyDao) {

    val allVacancies: Flow<List<JobVacancy>> = dao.getAllVacancies()
    val allApplications: Flow<List<JobApplication>> = dao.getAllApplications()
    val allBookings: Flow<List<Booking>> = dao.getAllBookings()
    val allPayments: Flow<List<PaymentRecord>> = dao.getAllPayments()
    val allInquiries: Flow<List<Inquiry>> = dao.getAllInquiries()
    val allPostedPhotos: Flow<List<PostedPhoto>> = dao.getAllPostedPhotos()

    suspend fun populateInitialDataIfEmpty() {
        if (dao.getVacanciesCount() == 0) {
            dao.insertVacancies(getInitialVacancies())

            // Seed a couple of realistic active tracking records
            val sampleApp1 = JobApplication(
                trackingNumber = "APTA-EU-1082",
                applicantName = "Subhasish Roy",
                passportNumber = "V9842109",
                phone = "+919830123456",
                whatsappNumber = "+919830123456",
                email = "subhasish.roy@gmail.com",
                address = "Ranaghat, Nadia, West Bengal",
                jobTitle = "Warehouse Logistics Associate",
                country = "Poland",
                experienceYears = "3 Years",
                education = "Higher Secondary (12th)",
                status = "Work Permit Approved",
                stageIndex = 4,
                remarks = "Official Polish Type-A Work Permit (Zezwolenie na pracę) approved by Mazovian Voivodeship office. Preparing VFS document checklist.",
                appliedDate = "2024-05-12",
                targetSubmissionDate = "Visa Appointment scheduled for 28th",
                assignedOfficer = "Priya Sharma (Senior European Visa Officer)"
            )

            val sampleApp2 = JobApplication(
                trackingNumber = "APTA-EU-2490",
                applicantName = "Ariful Mondal",
                passportNumber = "W1039485",
                phone = "+918765432109",
                whatsappNumber = "+918765432109",
                email = "ariful.mondal@gmail.com",
                address = "Kolkata, West Bengal",
                jobTitle = "Construction Steel Fixer",
                country = "Romania",
                experienceYears = "5 Years",
                education = "ITI Diploma",
                status = "Work Permit In Process",
                stageIndex = 3,
                remarks = "Application submitted to General Inspectorate for Immigration (IGI) Bucharest. Police clearance verification cleared.",
                appliedDate = "2024-06-04",
                targetSubmissionDate = "Estimated permit release in 3 weeks",
                assignedOfficer = "Bikram Das (Document Specialist)"
            )

            val sampleApp3 = JobApplication(
                trackingNumber = "APTA-EU-3914",
                applicantName = "Rahim Mondal",
                passportNumber = "T4729103",
                phone = "+919434012389",
                whatsappNumber = "+919434012389",
                email = "rahim.mondal@outlook.com",
                address = "Nadia, West Bengal",
                jobTitle = "Food Factory Packaging Worker",
                country = "Malta",
                experienceYears = "2 Years",
                education = "Secondary (10th)",
                status = "Visa Stamping in VFS",
                stageIndex = 5,
                remarks = "Identity Malta approval received. Passport submitted to VFS Kolkata for National D-Visa sticker endorsement.",
                appliedDate = "2024-04-18",
                targetSubmissionDate = "Ready for Flight booking shortly",
                assignedOfficer = "Animesh Roy (Operations Lead)"
            )

            dao.insertApplication(sampleApp1)
            dao.insertApplication(sampleApp2)
            dao.insertApplication(sampleApp3)
        }
    }

    suspend fun getApplication(trackingNumber: String): JobApplication? {
        return dao.getApplicationByTrackingNumber(trackingNumber.trim().uppercase())
    }

    suspend fun submitApplication(
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
        photoUri: String? = null
    ): String {
        val randomDigits = Random.nextInt(1000, 9999)
        val trackingId = "APTA-EU-$randomDigits"
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val app = JobApplication(
            trackingNumber = trackingId,
            applicantName = name,
            passportNumber = passport.uppercase(),
            phone = phone,
            whatsappNumber = whatsapp.ifBlank { phone },
            email = email,
            address = address,
            jobTitle = jobTitle,
            country = country,
            experienceYears = experience,
            education = education,
            status = "Application Submitted",
            stageIndex = 1,
            remarks = "Application received at Arbi Pori Travel Agency. Counselor will verify documentation and contact on WhatsApp within 24 hours.",
            appliedDate = currentDate,
            photoUri = photoUri
        )
        dao.insertApplication(app)
        if (!photoUri.isNullOrBlank()) {
            postPhoto(
                candidateName = name,
                trackingNumber = trackingId,
                photoType = "Candidate White Background Photo",
                photoUri = photoUri,
                notes = "Attached with initial European Work Permit application"
            )
        }
        return trackingId
    }

    suspend fun updateApplicationPhoto(trackingNumber: String, photoUri: String) {
        dao.updateApplicationPhoto(trackingNumber.uppercase(), photoUri)
    }

    suspend fun postPhoto(
        candidateName: String,
        trackingNumber: String = "",
        photoType: String,
        photoUri: String,
        notes: String = ""
    ): String {
        val randomNum = Random.nextInt(1000, 9999)
        val photoId = "PHO-$randomNum"
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val postedPhoto = PostedPhoto(
            id = photoId,
            candidateName = candidateName,
            trackingNumber = trackingNumber.uppercase(),
            photoType = photoType,
            photoUri = photoUri,
            notes = notes,
            datePosted = dateStr
        )
        dao.insertPostedPhoto(postedPhoto)

        if (trackingNumber.isNotBlank()) {
            dao.updateApplicationPhoto(trackingNumber.uppercase(), photoUri)
        }
        return photoId
    }

    fun getPhotosForTracking(trackingNumber: String): Flow<List<PostedPhoto>> {
        return dao.getPhotosForTracking(trackingNumber.uppercase())
    }

    suspend fun createBooking(
        name: String,
        phone: String,
        email: String,
        date: String,
        timeSlot: String,
        consultationType: String,
        country: String,
        purpose: String,
        notes: String
    ): String {
        val randomNum = Random.nextInt(1000, 9999)
        val bookingId = "BK-APTA-$randomNum"
        val booking = Booking(
            bookingId = bookingId,
            clientName = name,
            phone = phone,
            email = email,
            bookingDate = date,
            timeSlot = timeSlot,
            consultationType = consultationType,
            countryInterest = country,
            purpose = purpose,
            notes = notes
        )
        dao.insertBooking(booking)
        return bookingId
    }

    suspend fun submitPayment(
        candidateName: String,
        trackingOrBookingId: String,
        amount: String,
        paymentMethod: String,
        utr: String,
        serviceType: String,
        receiptPhotoUri: String? = null
    ): String {
        val randomNum = Random.nextInt(1000, 9999)
        val receiptId = "RCP-${paymentMethod.take(3).uppercase()}-$randomNum"
        val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

        val record = PaymentRecord(
            receiptId = receiptId,
            candidateName = candidateName,
            trackingOrBookingId = trackingOrBookingId,
            amount = amount,
            paymentMethod = paymentMethod,
            transactionUtr = utr,
            serviceType = serviceType,
            paymentDate = dateStr,
            receiptPhotoUri = receiptPhotoUri
        )
        dao.insertPayment(record)

        if (!receiptPhotoUri.isNullOrBlank()) {
            postPhoto(
                candidateName = candidateName,
                trackingNumber = trackingOrBookingId,
                photoType = "Payment Receipt / Proof",
                photoUri = receiptPhotoUri,
                notes = "UTR: $utr • Method: $paymentMethod • Amount: $amount"
            )
        }

        return receiptId
    }

    suspend fun submitInquiry(
        name: String,
        phone: String,
        email: String,
        country: String,
        trade: String,
        message: String
    ) {
        val inquiry = Inquiry(
            name = name,
            phone = phone,
            email = email,
            countryInterest = country,
            preferredTrade = trade,
            message = message
        )
        dao.insertInquiry(inquiry)
    }

    private fun getInitialVacancies(): List<JobVacancy> {
        return listOf(
            JobVacancy(
                id = 1,
                title = "Warehouse Logistics & Packing Staff",
                country = "Poland",
                countryFlag = "🇵🇱",
                city = "Warsaw / Wrocław",
                salaryEur = "€1,350 - €1,650 / month",
                salaryInr = "₹1,20,000 - ₹1,48,000 / mo",
                category = "Logistics & Warehousing",
                vacanciesCount = 45,
                contractDuration = "2 Years (Renewable)",
                processingTime = "60 - 75 Days",
                requirements = "Age: 20-45 | 10th Pass | Basic English or willingness to learn | Physical fitness",
                benefits = "Free Accommodation provided | Transportation to warehouse | Work Permit Type A | Health Insurance covered",
                description = "Packing, scanning, sorting packages, pallet management in major European distribution hubs. Overtime allowance available up to 40 hours/month.",
                isHotVacancy = true
            ),
            JobVacancy(
                id = 2,
                title = "Factory Food Processing & Assembly Worker",
                country = "Malta",
                countryFlag = "🇲🇹",
                city = "Birkirkara / Valletta",
                salaryEur = "€1,200 - €1,500 / month",
                salaryInr = "₹1,08,000 - ₹1,35,000 / mo",
                category = "Food Processing & Manufacturing",
                vacanciesCount = 30,
                contractDuration = "1 Year (Renewable)",
                processingTime = "45 - 60 Days",
                requirements = "Age: 21-42 | Secondary School | Food safety basic hygiene | English speaking preferred",
                benefits = "Shared Company Accommodation | Subsidized meals on shift | Identity Malta Single Permit | Paid annual leave 24 days",
                description = "Operating packaging machinery, sorting bakery products, quality inspection, and hygiene compliance in modern HACCP certified facilities.",
                isHotVacancy = true
            ),
            JobVacancy(
                id = 3,
                title = "Civil Construction Worker & Mason / Steel Fixer",
                country = "Romania",
                countryFlag = "🇷🇴",
                city = "Bucharest / Cluj-Napoca",
                salaryEur = "€1,100 - €1,450 / month",
                salaryInr = "₹99,000 - ₹1,30,000 / mo",
                category = "Construction & Infrastructure",
                vacanciesCount = 60,
                contractDuration = "2 Years (Renewable)",
                processingTime = "50 - 65 Days",
                requirements = "Age: 20-48 | Prior construction trade experience | Physical stamina | Trade certificate appreciated",
                benefits = "Free lodging in worker apartments | 3 times food or daily food allowance | Medical insurance | Fast visa clearance",
                description = "Formwork, shuttering, bricklaying, rebar placement, concrete finishing for commercial buildings and residential highway infrastructure projects.",
                isHotVacancy = true
            ),
            JobVacancy(
                id = 4,
                title = "Heavy Truck & Trailer Driver (CE License)",
                country = "Lithuania",
                countryFlag = "🇱🇹",
                city = "Vilnius / Kaunas",
                salaryEur = "€2,100 - €2,800 / month",
                salaryInr = "₹1,88,000 - ₹2,50,000 / mo",
                category = "Transportation & Driving",
                vacanciesCount = 25,
                contractDuration = "2 Years (Renewable)",
                processingTime = "60 - 90 Days",
                requirements = "Valid Indian Heavy Driving License + Minimum 3 years driving experience | Code 95 training provided | Age: 25-50",
                benefits = "Modern Euro 6 Mercedes/Scania trucks | Per diem euro trip allowance | Code 95 certification assistance | Schengen transit permit",
                description = "International long-haul freight transport across Poland, Germany, France, and Netherlands. High monthly earnings with guaranteed trip allowances.",
                isHotVacancy = true
            ),
            JobVacancy(
                id = 5,
                title = "Hotel Housekeeping & Kitchen Steward",
                country = "Croatia",
                countryFlag = "🇭🇷",
                city = "Dubrovnik / Split / Zagreb",
                salaryEur = "€1,250 - €1,600 / month",
                salaryInr = "₹1,12,000 - ₹1,44,000 / mo",
                category = "Hospitality & Tourism",
                vacanciesCount = 35,
                contractDuration = "1 Year / Seasonal",
                processingTime = "45 - 60 Days",
                requirements = "Age: 20-40 | Pleasant personality | Basic English communication | Hotel / restaurant experience preferred",
                benefits = "Free room near seaside resort | All 3 meals provided free by hotel | High tip income | Croatian work & residence permit",
                description = "Room cleaning, linen change, kitchen preparation, dishwashing and hospitality support in premier 4-star and 5-star coastal European hotels.",
                isHotVacancy = false
            ),
            JobVacancy(
                id = 6,
                title = "Electrician & Maintenance Technician",
                country = "Germany",
                countryFlag = "🇩🇪",
                city = "Hamburg / Frankfurt",
                salaryEur = "€2,200 - €2,900 / month",
                salaryInr = "₹1,97,000 - ₹2,60,000 / mo",
                category = "Technical & Engineering",
                vacanciesCount = 18,
                contractDuration = "2 Years",
                processingTime = "90 - 120 Days",
                requirements = "ITI / Diploma in Electrical | 3+ years experience | Basic German (A2 level assistance offered) | Skilled worker qualification",
                benefits = "German Opportunity Card / Skilled Immigration Act route | Social security | Family reunion eligibility after 1 year | Pension contribution",
                description = "Installation of commercial electrical wiring, circuit breaker boards, solar PV arrays, industrial machine repair, and automation panels.",
                isHotVacancy = true
            ),
            JobVacancy(
                id = 7,
                title = "Agricultural Greenhouse Worker & Fruit Harvester",
                country = "Hungary",
                countryFlag = "🇭🇺",
                city = "Budapest / Szeged",
                salaryEur = "€1,050 - €1,350 / month",
                salaryInr = "₹94,000 - ₹1,21,000 / mo",
                category = "Agriculture & Farming",
                vacanciesCount = 50,
                contractDuration = "2 Years",
                processingTime = "45 - 60 Days",
                requirements = "Age: 20-45 | Hardworking | No prior qualification required | Physical endurance",
                benefits = "Free farm cottage accommodation | Cooking facilities & fresh vegetable supply | Simplified European employment permit",
                description = "Cultivating, pruning, greenhouse climate monitoring, harvesting berries, apples, and tomatoes, sorting and crate loading.",
                isHotVacancy = false
            ),
            JobVacancy(
                id = 8,
                title = "Forklift Operator & Reach Truck Driver",
                country = "Czech Republic",
                countryFlag = "🇨🇿",
                city = "Prague / Brno",
                salaryEur = "€1,400 - €1,800 / month",
                salaryInr = "₹1,25,000 - ₹1,62,000 / mo",
                category = "Logistics & Warehousing",
                vacanciesCount = 20,
                contractDuration = "2 Years",
                processingTime = "60 - 80 Days",
                requirements = "Forklift operating experience | Age 22-46 | Safe driving certificate or Indian forklift license",
                benefits = "Company flat provided | Overtime bonus + attendance bonus | Work clothes & PPE provided | Czech Employee Card",
                description = "Driving reach trucks, counterbalanced forklifts, loading container trucks, pallet stacking up to 10 meters height in automotive parts depot.",
                isHotVacancy = false
            )
        )
    }
}
