package com.santheconnect.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SantheConnectApp(repository = SantheRepository(), auth = SantheAuth())
        }
    }
}

enum class PlaceType(val label: String) {
    Food("Food"),
    Market("Santhe"),
    Craft("Craft"),
    Stay("Stay")
}

enum class PlaceFilter(val label: String) {
    Today("Today"),
    Food("Food"),
    Market("Santhe"),
    Craft("Craft"),
    Stay("Stay")
}

enum class AppLanguage {
    English,
    Kannada
}

enum class LoginMode {
    SignIn,
    SignUp
}

data class UiStrings(
    val tagline: String,
    val addLocation: String,
    val language: String,
    val localPicks: String,
    val openSanthesToday: String,
    val todaysSanthe: String,
    val weeklyMarketNote: String,
    val openOnlyToday: String,
    val placesMatched: String,
    val localSpots: String,
    val popup: String,
    val addLocalSpot: String,
    val placeName: String,
    val specialtyTag: String,
    val villageOrArea: String,
    val captureGps: String,
    val saveLocation: String,
    val gpsBeforeSaving: String,
    val gpsDenied: String,
    val gpsCaptured: String,
    val newlyAdded: String,
    val reviewWall: String,
    val reviewWallSubtitle: String,
    val yourName: String,
    val placeVisited: String,
    val voiceNote: String,
    val photoUrl: String,
    val postReview: String,
    val by: String,
    val signIn: String,
    val signUp: String,
    val signOut: String,
    val loginRequired: String,
    val loginToContinue: String,
    val loginToAddLocation: String,
    val loginToPostReview: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val createAccount: String,
    val alreadyHaveAccount: String,
    val newUser: String,
    val signedInAs: String,
    val passwordMismatch: String,
    val passwordTooShort: String,
    val enterEmailPassword: String
)

object SantheStrings {
    fun of(language: AppLanguage): UiStrings = when (language) {
        AppLanguage.English -> UiStrings(
            tagline = "Local flavor discovery for Karnataka",
            addLocation = "Add location",
            language = "Language",
            localPicks = "Local Picks",
            openSanthesToday = "Open Santhes Today",
            todaysSanthe = "Today's Santhe",
            weeklyMarketNote = "Weekly markets open only on their scheduled day",
            openOnlyToday = "Open today only",
            placesMatched = "places matched for",
            localSpots = "local spots",
            popup = "Pop-up",
            addLocalSpot = "Add Local Spot",
            placeName = "Place name",
            specialtyTag = "Specialty tag",
            villageOrArea = "Village or area",
            captureGps = "Capture Accurate GPS",
            saveLocation = "Save Location",
            gpsBeforeSaving = "Capture GPS before saving",
            gpsDenied = "Location permission was denied",
            gpsCaptured = "GPS captured",
            newlyAdded = "Newly added by a local host",
            reviewWall = "Review Wall",
            reviewWallSubtitle = "Tourists can share photos and voice-note style stories",
            yourName = "Your name",
            placeVisited = "Place visited",
            voiceNote = "Voice note or review",
            photoUrl = "Photo URL",
            postReview = "Post Review",
            by = "by",
            signIn = "Sign in",
            signUp = "Sign up",
            signOut = "Sign out",
            loginRequired = "Login required",
            loginToContinue = "Sign in or create an account to continue",
            loginToAddLocation = "Please sign in to add a local spot.",
            loginToPostReview = "Please sign in to post a review.",
            email = "Email",
            password = "Password",
            confirmPassword = "Confirm password",
            createAccount = "Create account",
            alreadyHaveAccount = "Already have an account?",
            newUser = "New user?",
            signedInAs = "Signed in as",
            passwordMismatch = "Passwords do not match",
            passwordTooShort = "Password must be at least 6 characters",
            enterEmailPassword = "Enter email and password"
        )

        AppLanguage.Kannada -> UiStrings(
            tagline = "ಕರ್ನಾಟಕದ ಸ್ಥಳೀಯ ರುಚಿ ಮತ್ತು ಸಂಸ್ಕೃತಿ",
            addLocation = "ಸ್ಥಳ ಸೇರಿಸಿ",
            language = "ಭಾಷೆ",
            localPicks = "ಸ್ಥಳೀಯ ಆಯ್ಕೆಗಳು",
            openSanthesToday = "ಇಂದು ತೆರೆದಿರುವ ಸಂತೇಗಳು",
            todaysSanthe = "ಇಂದಿನ ಸಂತೆ",
            weeklyMarketNote = "ವಾರದ ಸಂತೆಗಳು ನಿಗದಿತ ದಿನ ಮಾತ್ರ ತೆರೆದಿರುತ್ತವೆ",
            openOnlyToday = "ಇಂದು ಮಾತ್ರ ತೆರೆದಿದೆ",
            placesMatched = "ಸ್ಥಳಗಳು ಹೊಂದಿವೆ",
            localSpots = "ಸ್ಥಳೀಯ ಸ್ಥಳಗಳು",
            popup = "ತಾತ್ಕಾಲಿಕ ಅಂಗಡಿ",
            addLocalSpot = "ಸ್ಥಳೀಯ ಸ್ಥಳ ಸೇರಿಸಿ",
            placeName = "ಸ್ಥಳದ ಹೆಸರು",
            specialtyTag = "ವಿಶೇಷತೆ",
            villageOrArea = "ಗ್ರಾಮ ಅಥವಾ ಪ್ರದೇಶ",
            captureGps = "ನಿಖರ GPS ಹಿಡಿಯಿರಿ",
            saveLocation = "ಸ್ಥಳ ಉಳಿಸಿ",
            gpsBeforeSaving = "ಉಳಿಸುವ ಮೊದಲು GPS ಹಿಡಿಯಿರಿ",
            gpsDenied = "ಸ್ಥಳ ಅನುಮತಿ ನಿರಾಕರಿಸಲಾಗಿದೆ",
            gpsCaptured = "GPS ಹಿಡಿಯಲಾಗಿದೆ",
            newlyAdded = "ಸ್ಥಳೀಯ ಆತಿಥೇಯರಿಂದ ಹೊಸದಾಗಿ ಸೇರಿಸಲಾಗಿದೆ",
            reviewWall = "ಅನುಭವ ಗೋಡೆ",
            reviewWallSubtitle = "ಪ್ರವಾಸಿಗರು ಫೋಟೋ ಮತ್ತು ಧ್ವನಿ ಟಿಪ್ಪಣಿ ಶೈಲಿಯ ಅನುಭವ ಹಂಚಬಹುದು",
            yourName = "ನಿಮ್ಮ ಹೆಸರು",
            placeVisited = "ಭೇಟಿ ನೀಡಿದ ಸ್ಥಳ",
            voiceNote = "ಧ್ವನಿ ಟಿಪ್ಪಣಿ ಅಥವಾ ವಿಮರ್ಶೆ",
            photoUrl = "ಫೋಟೋ URL",
            postReview = "ವಿಮರ್ಶೆ ಪೋಸ್ಟ್ ಮಾಡಿ",
            by = "ಇವರಿಂದ",
            signIn = "ಲಾಗಿನ್",
            signUp = "ಸೈನ್ ಅಪ್",
            signOut = "ಲಾಗ್ ಔಟ್",
            loginRequired = "ಲಾಗಿನ್ ಅಗತ್ಯ",
            loginToContinue = "ಮುಂದುವರಿಸಲು ಲಾಗಿನ್ ಮಾಡಿ ಅಥವಾ ಖಾತೆ ರಚಿಸಿ",
            loginToAddLocation = "ಸ್ಥಳೀಯ ಸ್ಥಳ ಸೇರಿಸಲು ದಯವಿಟ್ಟು ಲಾಗಿನ್ ಮಾಡಿ.",
            loginToPostReview = "ವಿಮರ್ಶೆ ಪೋಸ್ಟ್ ಮಾಡಲು ದಯವಿಟ್ಟು ಲಾಗಿನ್ ಮಾಡಿ.",
            email = "ಇಮೇಲ್",
            password = "ಪಾಸ್‌ವರ್ಡ್",
            confirmPassword = "ಪಾಸ್‌ವರ್ಡ್ ದೃಢೀಕರಿಸಿ",
            createAccount = "ಖಾತೆ ರಚಿಸಿ",
            alreadyHaveAccount = "ಈಗಾಗಲೇ ಖಾತೆ ಇದೆಯೇ?",
            newUser = "ಹೊಸ ಬಳಕೆದಾರರೇ?",
            signedInAs = "ಲಾಗಿನ್ ಆಗಿರುವವರು",
            passwordMismatch = "ಪಾಸ್‌ವರ್ಡ್‌ಗಳು ಹೊಂದಿಕೆಯಾಗುತ್ತಿಲ್ಲ",
            passwordTooShort = "ಪಾಸ್‌ವರ್ಡ್ ಕನಿಷ್ಠ 6 ಅಕ್ಷರಗಳಿರಬೇಕು",
            enterEmailPassword = "ಇಮೇಲ್ ಮತ್ತು ಪಾಸ್‌ವರ್ಡ್ ನಮೂದಿಸಿ"
        )
    }
}

data class LocalPlace(
    val id: String,
    val name: String,
    val nameKn: String = "",
    val type: PlaceType,
    val specialty: String,
    val specialtyKn: String = "",
    val village: String,
    val villageKn: String = "",
    val latitude: Double,
    val longitude: Double,
    val openDays: Set<DayOfWeek>,
    val imageUrl: String,
    val reviewNote: String,
    val isPopup: Boolean = false
) {
    fun isVisibleFor(filter: PlaceFilter, today: DayOfWeek = LocalDate.now().dayOfWeek): Boolean {
        return when (filter) {
            PlaceFilter.Today -> type != PlaceType.Market || openDays.contains(today)
            PlaceFilter.Market -> type == PlaceType.Market && openDays.contains(today)
            PlaceFilter.Food -> type == PlaceType.Food
            PlaceFilter.Craft -> type == PlaceType.Craft
            PlaceFilter.Stay -> type == PlaceType.Stay
        }
    }
}

data class ExperienceReview(
    val author: String,
    val placeName: String,
    val placeNameKn: String = "",
    val note: String,
    val noteKn: String = "",
    val imageUrl: String,
    val userEmail: String = "",
    val createdAtMillis: Long = 0L
)

class SantheRepository {
    private val databaseId = BuildConfig.FIRESTORE_DATABASE_ID
    private val firestore: FirebaseFirestore? = runCatching {
        if (databaseId == "(default)") FirebaseFirestore.getInstance() else FirebaseFirestore.getInstance(databaseId)
    }.getOrNull()

    fun observePlaces(onUpdate: (List<LocalPlace>, String) -> Unit): ListenerRegistration? {
        val db = firestore ?: run {
            onUpdate(SampleData.places, "Firebase is not configured for database '$databaseId'. Showing sample places.")
            return null
        }

        return db.collection("places").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                onUpdate(SampleData.places, "Firestore '$databaseId' read failed: ${error?.localizedMessage ?: "Unknown error"}. Showing sample places.")
                return@addSnapshotListener
            }

            val remotePlaces = snapshot.documents.mapNotNull { doc ->
                runCatching {
                    val days = parseOpenDays(doc.get("openDays"))
                    val type = parsePlaceType(doc.getString("type"))

                    LocalPlace(
                        id = doc.id,
                        name = doc.getString("name").orEmpty(),
                        nameKn = doc.getString("nameKn").orEmpty(),
                        type = type,
                        specialty = doc.getString("specialty").orEmpty(),
                        specialtyKn = doc.getString("specialtyKn").orEmpty(),
                        village = doc.getString("village").orEmpty(),
                        villageKn = doc.getString("villageKn").orEmpty(),
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        openDays = days,
                        imageUrl = doc.getString("imageUrl").orEmpty(),
                        reviewNote = doc.getString("reviewNote").orEmpty(),
                        isPopup = doc.getBoolean("popup") ?: false
                    )
                }.getOrNull()
            }

            val status = when {
                snapshot.isEmpty -> "Firestore '$databaseId' connected, but collection 'places' has no documents. Showing sample places."
                remotePlaces.isEmpty() -> "Firestore '$databaseId' connected, but no valid place documents were found. Check field names and type values."
                else -> "Firestore '$databaseId' connected: ${remotePlaces.size} live places loaded."
            }
            onUpdate(remotePlaces.ifEmpty { SampleData.places }, status)
        }
    }

    fun addPlace(place: LocalPlace) {
        firestore?.collection("places")?.add(place.toFirestoreMap())
    }

    fun observeReviews(onUpdate: (List<ExperienceReview>) -> Unit): ListenerRegistration? {
        val db = firestore ?: run {
            onUpdate(SampleData.reviews)
            return null
        }

        return db.collection("reviews").addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                onUpdate(SampleData.reviews)
                return@addSnapshotListener
            }

            val remoteReviews = snapshot.documents.mapNotNull { doc ->
                runCatching {
                    ExperienceReview(
                        author = doc.getString("author").orEmpty(),
                        placeName = doc.getString("placeName").orEmpty(),
                        placeNameKn = doc.getString("placeNameKn").orEmpty(),
                        note = doc.getString("note").orEmpty(),
                        noteKn = doc.getString("noteKn").orEmpty(),
                        imageUrl = doc.getString("imageUrl").orEmpty(),
                        userEmail = doc.getString("userEmail").orEmpty(),
                        createdAtMillis = doc.getTimestamp("createdAt")?.toDate()?.time ?: 0L
                    )
                }.getOrNull()
            }.sortedByDescending { it.createdAtMillis }

            onUpdate(remoteReviews.ifEmpty { SampleData.reviews })
        }
    }

    fun addReview(review: ExperienceReview, userEmail: String) {
        firestore?.collection("reviews")?.add(review.toFirestoreMap(userEmail))
    }

    private fun LocalPlace.toFirestoreMap(): Map<String, Any> = mapOf(
        "name" to name,
        "nameKn" to nameKn,
        "type" to type.name,
        "specialty" to specialty,
        "specialtyKn" to specialtyKn,
        "village" to village,
        "villageKn" to villageKn,
        "latitude" to latitude,
        "longitude" to longitude,
        "openDays" to openDays.map { it.name },
        "imageUrl" to imageUrl,
        "reviewNote" to reviewNote,
        "popup" to isPopup
    )

    private fun ExperienceReview.toFirestoreMap(email: String): Map<String, Any> = mapOf(
        "author" to author,
        "placeName" to placeName,
        "placeNameKn" to placeNameKn,
        "note" to note,
        "noteKn" to noteKn,
        "imageUrl" to imageUrl,
        "userEmail" to email,
        "createdAt" to FieldValue.serverTimestamp()
    )
}

fun parsePlaceType(value: String?): PlaceType {
    return PlaceType.values().firstOrNull { it.name.equals(value?.trim(), ignoreCase = true) } ?: PlaceType.Food
}

fun parseOpenDays(value: Any?): Set<DayOfWeek> {
    val rawDays = when (value) {
        is List<*> -> value.mapNotNull { it?.toString() }
        is String -> value.split(",")
        else -> emptyList()
    }

    val days = rawDays.mapNotNull { raw ->
        DayOfWeek.values().firstOrNull { it.name.equals(raw.trim(), ignoreCase = true) }
    }.toSet()

    return days.ifEmpty { DayOfWeek.values().toSet() }
}

class SantheAuth {
    private val auth: FirebaseAuth? = runCatching { FirebaseAuth.getInstance() }.getOrNull()

    fun currentUser(): FirebaseUser? = auth?.currentUser

    fun observeUser(onUpdate: (FirebaseUser?) -> Unit): FirebaseAuth.AuthStateListener? {
        val firebaseAuth = auth ?: run {
            onUpdate(null)
            return null
        }

        val listener = FirebaseAuth.AuthStateListener { onUpdate(it.currentUser) }
        firebaseAuth.addAuthStateListener(listener)
        onUpdate(firebaseAuth.currentUser)
        return listener
    }

    fun removeListener(listener: FirebaseAuth.AuthStateListener?) {
        val firebaseAuth = auth ?: return
        if (listener != null) firebaseAuth.removeAuthStateListener(listener)
    }

    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val firebaseAuth = auth ?: return onResult(false, "Firebase is not configured")
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.localizedMessage) }
    }

    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val firebaseAuth = auth ?: return onResult(false, "Firebase is not configured")
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.localizedMessage) }
    }

    fun signOut() {
        auth?.signOut()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SantheConnectApp(repository: SantheRepository, auth: SantheAuth) {
    val context = LocalContext.current
    val today = LocalDate.now().dayOfWeek
    var language by remember {
        mutableStateOf(
            if (context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                    .getString("language", AppLanguage.English.name) == AppLanguage.Kannada.name
            ) AppLanguage.Kannada else AppLanguage.English
        )
    }
    val strings = SantheStrings.of(language)
    var places by remember { mutableStateOf(SampleData.places) }
    var selectedFilter by remember { mutableStateOf(PlaceFilter.Today) }
    var selectedPlace by remember { mutableStateOf<LocalPlace?>(null) }
    var showAddLocation by remember { mutableStateOf(false) }
    var showLogin by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf<String?>(null) }
    var currentUser by remember { mutableStateOf(auth.currentUser()) }
    var reviews by remember { mutableStateOf(SampleData.reviews) }

    DisposableEffect(repository) {
        val registration = repository.observePlaces { livePlaces, _ ->
            places = livePlaces
        }
        onDispose { registration?.remove() }
    }

    DisposableEffect(repository) {
        val registration = repository.observeReviews { liveReviews ->
            reviews = liveReviews
        }
        onDispose { registration?.remove() }
    }

    DisposableEffect(auth) {
        val listener = auth.observeUser { currentUser = it }
        onDispose { auth.removeListener(listener) }
    }

    val visiblePlaces = places
        .filter { it.isVisibleFor(selectedFilter, today) }
        .let { matchedPlaces ->
            if (selectedFilter == PlaceFilter.Today) {
                matchedPlaces.sortedWith(
                    compareByDescending<LocalPlace> { it.type == PlaceType.Market && it.openDays.contains(today) }
                        .thenBy { it.type.ordinal }
                        .thenBy { it.name }
                )
            } else {
                matchedPlaces
            }
        }

    SantheTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Santhe-Connect", fontWeight = FontWeight.Bold)
                            Text(
                                strings.tagline,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (currentUser == null) {
                                loginMessage = strings.loginToAddLocation
                                showLogin = true
                            } else {
                                showAddLocation = true
                            }
                        }) {
                            Icon(Icons.Default.AddLocationAlt, contentDescription = strings.addLocation)
                        }
                        if (currentUser == null) {
                            IconButton(onClick = {
                                loginMessage = null
                                showLogin = true
                            }) {
                                Icon(Icons.Default.AccountCircle, contentDescription = strings.signIn)
                            }
                        } else {
                            IconButton(onClick = { auth.signOut() }) {
                                Icon(Icons.Default.Logout, contentDescription = strings.signOut)
                            }
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AccountStatus(user = currentUser, strings = strings)
                }
                item {
                    LanguageToggle(
                        language = language,
                        strings = strings,
                        onLanguageChange = {
                            language = it
                            context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                                .edit()
                                .putString("language", it.name)
                                .apply()
                        }
                    )
                }
                item {
                    FilterBar(
                        selected = selectedFilter,
                        language = language,
                        onSelected = {
                            selectedFilter = it
                            selectedPlace = null
                        }
                    )
                }
                item {
                    SantheMap(
                        places = visiblePlaces,
                        selectedPlace = selectedPlace,
                        language = language,
                        strings = strings,
                        onPlaceSelected = { selectedPlace = it }
                    )
                }
                item {
                    SectionTitle(
                        title = if (selectedFilter == PlaceFilter.Market) strings.openSanthesToday else strings.localPicks,
                        subtitle = "${visiblePlaces.size} ${strings.placesMatched} ${today.displayName(language)}"
                    )
                }
                items(visiblePlaces) { place ->
                    if (selectedFilter == PlaceFilter.Today && place.type == PlaceType.Market) {
                        SantheEventCard(
                            place = place,
                            selected = place.id == selectedPlace?.id,
                            language = language,
                            strings = strings,
                            onClick = { selectedPlace = place }
                        )
                    } else {
                        PlaceCard(
                            place = place,
                            selected = place.id == selectedPlace?.id,
                            language = language,
                            strings = strings,
                            onClick = { selectedPlace = place }
                        )
                    }
                }
                item {
                    ReviewWall(
                        reviews = reviews,
                        language = language,
                        strings = strings,
                        isSignedIn = currentUser != null,
                        onRequireLogin = {
                            loginMessage = strings.loginToPostReview
                            showLogin = true
                        },
                        onAddReview = {
                            reviews = listOf(it) + reviews
                            repository.addReview(it, currentUser?.email.orEmpty())
                        }
                    )
                }
            }
        }

        if (showAddLocation) {
            AddLocationSheet(
                language = language,
                strings = strings,
                onDismiss = { showAddLocation = false },
                onSave = {
                    places = listOf(it) + places
                    repository.addPlace(it)
                    showAddLocation = false
                }
            )
        }

        if (showLogin) {
            LoginSheet(
                auth = auth,
                strings = strings,
                message = loginMessage,
                onDismiss = { showLogin = false },
                onSuccess = {
                    loginMessage = null
                    showLogin = false
                }
            )
        }
    }
}

@Composable
fun SantheTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        primary = Color(0xFFF4A62A),
        secondary = Color(0xFF49B982),
        tertiary = Color(0xFFEF6F5E),
        background = Color(0xFF151611),
        surface = Color(0xFF202219),
        surfaceVariant = Color(0xFF303324),
        onPrimary = Color(0xFF2E1B00),
        onSecondary = Color(0xFF061F12),
        onTertiary = Color(0xFF300804),
        onBackground = Color(0xFFFFF7ED),
        onSurface = Color(0xFFFFF7ED),
        onSurfaceVariant = Color(0xFFD9CDBE)
    )

    MaterialTheme(colorScheme = colors, content = content)
}

@Composable
fun AccountStatus(user: FirebaseUser?, strings: UiStrings) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (user == null) strings.loginToContinue else "${strings.signedInAs} ${user.email.orEmpty()}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun LanguageToggle(language: AppLanguage, strings: UiStrings, onLanguageChange: (AppLanguage) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(strings.language, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = language == AppLanguage.English,
                onClick = { onLanguageChange(AppLanguage.English) },
                label = { Text("English") }
            )
            FilterChip(
                selected = language == AppLanguage.Kannada,
                onClick = { onLanguageChange(AppLanguage.Kannada) },
                label = { Text("ಕನ್ನಡ") }
            )
        }
    }
}

@Composable
fun FilterBar(selected: PlaceFilter, language: AppLanguage, onSelected: (PlaceFilter) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(PlaceFilter.entries) { filter ->
            FilterChip(
                selected = selected == filter,
                onClick = { onSelected(filter) },
                label = { Text(filter.label(language)) },
                leadingIcon = {
                    Icon(
                        imageVector = when (filter) {
                            PlaceFilter.Today -> Icons.Default.CalendarMonth
                            PlaceFilter.Food -> Icons.Default.Restaurant
                            PlaceFilter.Market -> Icons.Default.Storefront
                            PlaceFilter.Craft -> Icons.Default.RateReview
                            PlaceFilter.Stay -> Icons.Default.HomeWork
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun SantheMap(
    places: List<LocalPlace>,
    selectedPlace: LocalPlace?,
    language: AppLanguage,
    strings: UiStrings,
    onPlaceSelected: (LocalPlace) -> Unit
) {
    val context = LocalContext.current
    val hasLocationPermission = context.hasLocationPermission()
    val mysuru = LatLng(12.2958, 76.6394)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mysuru, 8.4f)
    }

    LaunchedEffect(selectedPlace?.id) {
        selectedPlace?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 13f),
                700
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(310.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = hasLocationPermission,
                    zoomControlsEnabled = false
                )
            ) {
                places.forEach { place ->
                    Marker(
                        state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                        title = place.displayName(language),
                        snippet = place.displaySpecialty(language),
                        icon = BitmapDescriptorFactory.defaultMarker(place.markerHue()),
                        onClick = {
                            onPlaceSelected(place)
                            false
                        }
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("${places.size} ${strings.localSpots}", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Column {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun PlaceCard(
    place: LocalPlace,
    selected: Boolean,
    language: AppLanguage,
    strings: UiStrings,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.displayName(language),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(86.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(place.typeColor())
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        place.displayName(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    TypeDot(place.typeColor())
                }
                Text(place.displaySpecialty(language), color = MaterialTheme.colorScheme.primary)
                Text(place.displayVillage(language), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text(place.type.label(language)) })
                    if (place.isPopup) AssistChip(onClick = {}, label = { Text(strings.popup) })
                }
            }
        }
    }
}

@Composable
fun SantheEventCard(
    place: LocalPlace,
    selected: Boolean,
    language: AppLanguage,
    strings: UiStrings,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.34f)
            else Color(0xFF332A17)
        )
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Storefront,
                        contentDescription = null,
                        modifier = Modifier.padding(9.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        place.displayName(language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        strings.openOnlyToday,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(place.displaySpecialty(language), color = MaterialTheme.colorScheme.onSurface)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(place.displayVillage(language)) })
                AssistChip(onClick = {}, label = { Text(todaySantheDay(place, language)) })
                if (place.isPopup) AssistChip(onClick = {}, label = { Text(strings.popup) })
            }
        }
    }
}

@Composable
fun TypeDot(color: Color) {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationSheet(
    language: AppLanguage,
    strings: UiStrings,
    onDismiss: () -> Unit,
    onSave: (LocalPlace) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var name by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(PlaceType.Food) }
    var latLng by remember { mutableStateOf<LatLng?>(null) }
    var status by remember { mutableStateOf(strings.gpsBeforeSaving) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) {
            captureCurrentLocation(context) {
                latLng = it
                status = "${strings.gpsCaptured}: %.5f, %.5f".format(Locale.US, it.latitude, it.longitude)
            }
        } else {
            status = strings.gpsDenied
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(strings.addLocalSpot, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(PlaceType.entries) { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.label(language)) }
                    )
                }
            }

            OutlinedTextField(name, { name = it }, label = { Text(strings.placeName) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(specialty, { specialty = it }, label = { Text(strings.specialtyTag) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(village, { village = it }, label = { Text(strings.villageOrArea) }, modifier = Modifier.fillMaxWidth())

            FilledTonalButton(
                onClick = {
                    if (context.hasLocationPermission()) {
                        captureCurrentLocation(context) {
                            latLng = it
                            status = "${strings.gpsCaptured}: %.5f, %.5f".format(Locale.US, it.latitude, it.longitude)
                        }
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(strings.captureGps)
            }

            Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Button(
                enabled = name.isNotBlank() && specialty.isNotBlank() && village.isNotBlank() && latLng != null,
                onClick = {
                    val point = latLng ?: return@Button
                    onSave(
                        LocalPlace(
                            id = "local-${System.currentTimeMillis()}",
                            name = name.trim(),
                            nameKn = if (language == AppLanguage.Kannada) name.trim() else "",
                            type = selectedType,
                            specialty = specialty.trim(),
                            specialtyKn = if (language == AppLanguage.Kannada) specialty.trim() else "",
                            village = village.trim(),
                            villageKn = if (language == AppLanguage.Kannada) village.trim() else "",
                            latitude = point.latitude,
                            longitude = point.longitude,
                            openDays = setOf(LocalDate.now().dayOfWeek),
                            imageUrl = "",
                            reviewNote = strings.newlyAdded,
                            isPopup = true
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(strings.saveLocation)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSheet(
    auth: SantheAuth,
    strings: UiStrings,
    message: String?,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mode by remember { mutableStateOf(LoginMode.SignIn) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(message.orEmpty()) }
    var isLoading by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (mode == LoginMode.SignIn) strings.signIn else strings.signUp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            if (status.isNotBlank()) {
                Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() },
                label = { Text(strings.email) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(strings.password) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            if (mode == LoginMode.SignUp) {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(strings.confirmPassword) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Button(
                enabled = !isLoading,
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() -> status = strings.enterEmailPassword
                        password.length < 6 -> status = strings.passwordTooShort
                        mode == LoginMode.SignUp && password != confirmPassword -> status = strings.passwordMismatch
                        else -> {
                            isLoading = true
                            status = ""
                            val onResult: (Boolean, String?) -> Unit = { success, error ->
                                isLoading = false
                                if (success) onSuccess() else status = error ?: strings.loginRequired
                            }
                            if (mode == LoginMode.SignIn) {
                                auth.signIn(email, password, onResult)
                            } else {
                                auth.signUp(email, password, onResult)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (mode == LoginMode.SignIn) strings.signIn else strings.createAccount)
                }
            }

            TextButton(
                onClick = {
                    mode = if (mode == LoginMode.SignIn) LoginMode.SignUp else LoginMode.SignIn
                    status = message.orEmpty()
                    password = ""
                    confirmPassword = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (mode == LoginMode.SignIn) {
                        "${strings.newUser} ${strings.signUp}"
                    } else {
                        "${strings.alreadyHaveAccount} ${strings.signIn}"
                    }
                )
            }
        }
    }
}

@Composable
fun ReviewWall(
    reviews: List<ExperienceReview>,
    language: AppLanguage,
    strings: UiStrings,
    isSignedIn: Boolean,
    onRequireLogin: () -> Unit,
    onAddReview: (ExperienceReview) -> Unit
) {
    var author by remember { mutableStateOf("") }
    var placeName by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(strings.reviewWall, strings.reviewWallSubtitle)
        Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(author, { author = it }, label = { Text(strings.yourName) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(placeName, { placeName = it }, label = { Text(strings.placeVisited) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(note, { note = it }, label = { Text(strings.voiceNote) }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text(strings.photoUrl) }, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        if (!isSignedIn) {
                            onRequireLogin()
                            return@Button
                        }

                        onAddReview(
                            ExperienceReview(
                                author = author,
                                placeName = placeName,
                                placeNameKn = if (language == AppLanguage.Kannada) placeName else "",
                                note = note,
                                noteKn = if (language == AppLanguage.Kannada) note else "",
                                imageUrl = imageUrl
                            )
                        )
                        author = ""
                        placeName = ""
                        note = ""
                        imageUrl = ""
                    },
                    enabled = author.isNotBlank() && placeName.isNotBlank() && note.isNotBlank()
                ) {
                    Icon(Icons.Default.RateReview, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(strings.postReview)
                }
            }
        }

        reviews.forEach { review ->
            Card(shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = review.imageUrl,
                        contentDescription = review.displayPlaceName(language),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(review.displayPlaceName(language), fontWeight = FontWeight.Bold)
                        Text(review.displayNote(language), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${strings.by} ${review.author}", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

fun captureCurrentLocation(context: Context, onLocation: (LatLng) -> Unit) {
    if (!context.hasLocationPermission()) return

    val client = LocationServices.getFusedLocationProviderClient(context)
    val token = CancellationTokenSource()
    client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
        .addOnSuccessListener { location ->
            location?.let { onLocation(LatLng(it.latitude, it.longitude)) }
        }
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun LocalPlace.markerHue(): Float = when (type) {
    PlaceType.Food -> BitmapDescriptorFactory.HUE_ORANGE
    PlaceType.Market -> BitmapDescriptorFactory.HUE_GREEN
    PlaceType.Craft -> BitmapDescriptorFactory.HUE_AZURE
    PlaceType.Stay -> BitmapDescriptorFactory.HUE_VIOLET
}

fun LocalPlace.typeColor(): Color = when (type) {
    PlaceType.Food -> Color(0xFFF4A62A)
    PlaceType.Market -> Color(0xFF49B982)
    PlaceType.Craft -> Color(0xFF47A6E8)
    PlaceType.Stay -> Color(0xFF8A2BE2)
}

fun LocalPlace.displayName(language: AppLanguage): String =
    if (language == AppLanguage.Kannada && nameKn.isNotBlank()) nameKn else name

fun LocalPlace.displaySpecialty(language: AppLanguage): String =
    if (language == AppLanguage.Kannada && specialtyKn.isNotBlank()) specialtyKn else specialty

fun LocalPlace.displayVillage(language: AppLanguage): String =
    if (language == AppLanguage.Kannada && villageKn.isNotBlank()) villageKn else village

fun ExperienceReview.displayPlaceName(language: AppLanguage): String =
    if (language == AppLanguage.Kannada && placeNameKn.isNotBlank()) placeNameKn else placeName

fun ExperienceReview.displayNote(language: AppLanguage): String =
    if (language == AppLanguage.Kannada && noteKn.isNotBlank()) noteKn else note

fun todaySantheDay(place: LocalPlace, language: AppLanguage): String =
    place.openDays.firstOrNull()?.displayName(language).orEmpty()

fun PlaceType.label(language: AppLanguage): String = when (language) {
    AppLanguage.English -> label
    AppLanguage.Kannada -> when (this) {
        PlaceType.Food -> "ಆಹಾರ"
        PlaceType.Market -> "ಸಂತೆ"
        PlaceType.Craft -> "ಕೈಗಾರಿಕೆ"
        PlaceType.Stay -> "ವಸತಿ"
    }
}

fun PlaceFilter.label(language: AppLanguage): String = when (language) {
    AppLanguage.English -> label
    AppLanguage.Kannada -> when (this) {
        PlaceFilter.Today -> "ಇಂದು"
        PlaceFilter.Food -> "ಆಹಾರ"
        PlaceFilter.Market -> "ಸಂತೆ"
        PlaceFilter.Craft -> "ಕೈಗಾರಿಕೆ"
        PlaceFilter.Stay -> "ವಸತಿ"
    }
}

fun DayOfWeek.displayName(language: AppLanguage): String = when (language) {
    AppLanguage.English -> name.lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }
    AppLanguage.Kannada -> when (this) {
        DayOfWeek.MONDAY -> "ಸೋಮವಾರ"
        DayOfWeek.TUESDAY -> "ಮಂಗಳವಾರ"
        DayOfWeek.WEDNESDAY -> "ಬುಧವಾರ"
        DayOfWeek.THURSDAY -> "ಗುರುವಾರ"
        DayOfWeek.FRIDAY -> "ಶುಕ್ರವಾರ"
        DayOfWeek.SATURDAY -> "ಶನಿವಾರ"
        DayOfWeek.SUNDAY -> "ಭಾನುವಾರ"
    }
}

object SampleData {
    val places = listOf(
        LocalPlace(
            id = "mysuru-rotti",
            name = "Basava Jolada Rotti Mane",
            nameKn = "ಬಸವ ಜೋಳದ ರೊಟ್ಟಿ ಮನೆ",
            type = PlaceType.Food,
            specialty = "Jolada rotti meals with ennegayi",
            specialtyKn = "ಎಣ್ಣೆಗಾಯಿ ಜೊತೆಗೆ ಜೋಳದ ರೊಟ್ಟಿ ಊಟ",
            village = "Mysuru old market road",
            villageKn = "ಮೈಸೂರು ಹಳೆಯ ಮಾರುಕಟ್ಟೆ ರಸ್ತೆ",
            latitude = 12.3101,
            longitude = 76.6547,
            openDays = DayOfWeek.values().toSet(),
            imageUrl = "https://images.unsplash.com/photo-1627308595229-7830a5c91f9f?q=80&w=900",
            reviewNote = "Simple banana-leaf meals and warm local hosting"
        ),
        LocalPlace(
            id = "mandya-santhe",
            name = "Mandya Monday Santhe",
            nameKn = "ಮಂಡ್ಯ ಸೋಮವಾರ ಸಂತೆ",
            type = PlaceType.Market,
            specialty = "Sugarcane jaggery, flowers, village snacks",
            specialtyKn = "ಕಬ್ಬಿನ ಬೆಲ್ಲ, ಹೂವುಗಳು, ಗ್ರಾಮೀಣ ತಿಂಡಿಗಳು",
            village = "Mandya",
            villageKn = "ಮಂಡ್ಯ",
            latitude = 12.5242,
            longitude = 76.8958,
            openDays = setOf(DayOfWeek.MONDAY),
            imageUrl = "https://images.unsplash.com/photo-1518843875459-f738682238a6?q=80&w=900",
            reviewNote = "Best morning visit before the crowd builds"
        ),
        LocalPlace(
            id = "srirangapatna-idli",
            name = "Amma Thatte Idli Point",
            nameKn = "ಅಮ್ಮ ತಟ್ಟೆ ಇಡ್ಲಿ ಪಾಯಿಂಟ್",
            type = PlaceType.Food,
            specialty = "Thatte idli, chutney, filter coffee",
            specialtyKn = "ತಟ್ಟೆ ಇಡ್ಲಿ, ಚಟ್ನಿ, ಫಿಲ್ಟರ್ ಕಾಫಿ",
            village = "Srirangapatna",
            villageKn = "ಶ್ರೀರಂಗಪಟ್ಟಣ",
            latitude = 12.4226,
            longitude = 76.6847,
            openDays = DayOfWeek.values().toSet(),
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?q=80&w=900",
            reviewNote = "Soft idlis near the temple street"
        ),
        LocalPlace(
            id = "melukote-towels",
            name = "Melukote Handloom Corner",
            nameKn = "ಮೇಲುಕೋಟೆ ಕೈಮಗ್ಗ ಅಂಗಡಿ",
            type = PlaceType.Craft,
            specialty = "Hand-woven towels and cotton angavastra",
            specialtyKn = "ಕೈಯಿಂದ ನೇಯ್ದ ಟವಲ್‌ಗಳು ಮತ್ತು ಹತ್ತಿ ಅಂಗವಸ್ತ್ರ",
            village = "Melukote",
            villageKn = "ಮೇಲುಕೋಟೆ",
            latitude = 12.6622,
            longitude = 76.6486,
            openDays = setOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
            imageUrl = "https://images.unsplash.com/photo-1604719312566-8912e9227c6a?q=80&w=900",
            reviewNote = "Meet the weaver family behind each piece"
        ),
        LocalPlace(
            id = "coorg-honey",
            name = "Kodagu Honey Home",
            nameKn = "ಕೊಡಗು ಜೇನು ಮನೆ",
            type = PlaceType.Stay,
            specialty = "Homestay with authentic forest honey",
            specialtyKn = "ನೈಜ ಕಾಡು ಜೇನು ಸಿಗುವ ಹೋಂಸ್ಟೇ",
            village = "Madikeri outskirts",
            villageKn = "ಮಡಿಕೇರಿ ಹೊರವಲಯ",
            latitude = 12.4244,
            longitude = 75.7382,
            openDays = DayOfWeek.values().toSet(),
            imageUrl = "https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?q=80&w=900",
            reviewNote = "Family stay with coffee estate breakfast"
        ),
        LocalPlace(
            id = "ramanagara-santhe",
            name = "Ramanagara Friday Santhe",
            nameKn = "ರಾಮನಗರ ಶುಕ್ರವಾರ ಸಂತೆ",
            type = PlaceType.Market,
            specialty = "Silk cocoons, local greens, clay pots",
            specialtyKn = "ರೇಷ್ಮೆ ಗೂಡುಗಳು, ಸ್ಥಳೀಯ ಸೊಪ್ಪುಗಳು, ಮಣ್ಣಿನ ಪಾತ್ರೆಗಳು",
            village = "Ramanagara",
            villageKn = "ರಾಮನಗರ",
            latitude = 12.7219,
            longitude = 77.2829,
            openDays = setOf(DayOfWeek.FRIDAY),
            imageUrl = "https://images.unsplash.com/photo-1488459716781-31db52582fe9?q=80&w=900",
            reviewNote = "Strong craft and farm produce mix"
        )
    )

    val reviews = listOf(
        ExperienceReview(
            author = "Ananya",
            placeName = "Basava Jolada Rotti Mane",
            placeNameKn = "ಬಸವ ಜೋಳದ ರೊಟ್ಟಿ ಮನೆ",
            note = "The rotti was hot, smoky, and exactly what we hoped to find outside normal travel apps.",
            noteKn = "ರೊಟ್ಟಿ ಬಿಸಿ ಮತ್ತು ರುಚಿಯಾಗಿತ್ತು. ಸಾಮಾನ್ಯ ಟ್ರಾವೆಲ್ ಆಪ್‌ಗಳಲ್ಲಿ ಸಿಗದ ನಿಜವಾದ ಸ್ಥಳೀಯ ಅನುಭವ.",
            imageUrl = "https://images.unsplash.com/photo-1627308595229-7830a5c91f9f?q=80&w=600"
        ),
        ExperienceReview(
            author = "Rohan",
            placeName = "Melukote Handloom Corner",
            placeNameKn = "ಮೇಲುಕೋಟೆ ಕೈಮಗ್ಗ ಅಂಗಡಿ",
            note = "The owner explained the weaving process and helped us buy directly from the family.",
            noteKn = "ಮಾಲೀಕರು ನೇಯುವ ವಿಧಾನವನ್ನು ವಿವರಿಸಿದರು ಮತ್ತು ಕುಟುಂಬದಿಂದಲೇ ಖರೀದಿಸಲು ಸಹಾಯ ಮಾಡಿದರು.",
            imageUrl = "https://images.unsplash.com/photo-1604719312566-8912e9227c6a?q=80&w=600"
        )
    )
}
