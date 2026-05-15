package com.kumbarakala.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.GppGood
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kumbarakala.app.data.supabase
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

// ─────────────────────────────────────────────────────────────
// EXACT colors copied from ArtisanSetupScreen.kt
// ─────────────────────────────────────────────────────────────
private val AS_BgColor        = Color(0xFFFAF5EF)   // bgColor
private val AS_HeaderBg       = Color(0xFFF3EAE0)   // headerBg
private val AS_TextDark       = Color(0xFF3E2B23)   // textTitleDark
private val AS_Primary        = Color(0xFFC05D35)   // primaryAction
private val AS_TextLight      = Color(0xFF8A827B)   // textLight
private val AS_BackBtn        = Color(0xFFF5EEE6)   // back button surface
private val AS_BackBtnIcon    = Color(0xFF50342E)   // back button icon
private val AS_FieldBg        = Color(0xFFFAF7F2)   // input field bg
private val AS_CardBg         = Color.White         // card bg

// ─────────────────────────────────────────────────────────────
// ProfileScreen
// ─────────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(navController: NavController) {

    val scrollState    = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val user           = supabase.auth.currentSessionOrNull()?.user

    // ── GUEST MODE ────────────────────────────────────────────
    if (user == null) {
        GuestProfileScreen(onLoginClick = { navController.navigate("auth") })
        return
    }

    // ── REAL USER DATA ────────────────────────────────────────
    fun meta(key: String) = user.userMetadata
        ?.get(key)?.toString()?.removeSurrounding("\"").orEmpty()

    val rawName     = meta("full_name").ifBlank { "Kumbara User" }
    val rawPhone    = meta("phone")
    val rawLocation = meta("location")
    val rawLanguage = meta("language")
    val userEmail   = user.email ?: "No Email"

    // ── EDIT STATE ────────────────────────────────────────────
    var isEditing    by rememberSaveable { mutableStateOf(false) }
    var editName     by rememberSaveable { mutableStateOf(rawName) }
    var editPhone    by rememberSaveable { mutableStateOf(rawPhone) }
    var editLocation by rememberSaveable { mutableStateOf(rawLocation) }
    var editLanguage by rememberSaveable { mutableStateOf(rawLanguage) }
    var isSaving     by remember { mutableStateOf(false) }
    var saveError    by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(rawName, rawPhone, rawLocation, rawLanguage) {
        if (!isEditing) {
            editName     = rawName
            editPhone    = rawPhone
            editLocation = rawLocation
            editLanguage = rawLanguage
        }
    }

    fun onSave() {
        coroutineScope.launch {
            isSaving  = true
            saveError = null
            try {
                supabase.auth.updateUser {
                    data = buildJsonObject {
                        put("full_name", editName.trim())
                        put("phone",     editPhone.trim())
                        put("location",  editLocation.trim())
                        put("language",  editLanguage.trim())
                    }
                }
                isEditing = false
            } catch (e: Exception) {
                saveError = "Failed to save. Please try again."
            } finally {
                isSaving = false
            }
        }
    }

    Scaffold(
        containerColor = AS_BgColor,
        modifier       = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AS_BgColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {

                // ── HEADER — exact ArtisanSetupScreen curve ───
                ProfileHeader(
                    isEditing   = isEditing,
                    onBackClick = { navController.popBackStack() },
                    onEditClick = { if (isEditing) onSave() else isEditing = true }
                )

                // ── CONTENT ───────────────────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .offset(y = (-50).dp)
                ) {

                    // Identity card
                    ProfileIdentityCard(
                        name  = if (isEditing) editName else rawName,
                        email = userEmail
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Error banner
                    AnimatedVisibility(visible = saveError != null) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFEBEB), RoundedCornerShape(18.dp))
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text     = saveError ?: "",
                                    color    = Color(0xFFC0392B),
                                    fontSize = 13.sp,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Personal Information card
                    PersonalInfoSection(
                        isEditing        = isEditing,
                        name             = editName,
                        email            = userEmail,
                        phone            = editPhone,
                        location         = editLocation,
                        language         = editLanguage,
                        onNameChange     = { editName     = it },
                        onPhoneChange    = { editPhone    = it },
                        onLocationChange = { editLocation = it },
                        onLanguageChange = { editLanguage = it }
                    )

                    // Save button (edit mode only) — same style as ArtisanSetupScreen save button
                    AnimatedVisibility(
                        visible = isEditing,
                        enter   = fadeIn() + expandVertically(),
                        exit    = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .background(AS_Primary, RoundedCornerShape(24.dp))
                                    .clip(RoundedCornerShape(24.dp))
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication        = null
                                    ) { onSave() },
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                if (isSaving) {
                                    CircularProgressIndicator(
                                        modifier    = Modifier.size(22.dp),
                                        color       = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Outlined.Check, null,
                                        tint     = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Save Changes",
                                        color      = Color.White,
                                        fontSize   = 16.sp,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AccountSecuritySection()

                    Spacer(modifier = Modifier.height(24.dp))

                    MoreSection(
                        onLogoutClick = {
                            coroutineScope.launch {
                                supabase.auth.signOut()
                                navController.navigate("auth") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// GUEST SCREEN
// ─────────────────────────────────────────────────────────────
@Composable
private fun GuestProfileScreen(onLoginClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AS_BgColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Same curve header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    drawPath(
                        path = Path().apply {
                            moveTo(0f, h * 0.60f)
                            cubicTo(w * 0.35f, h * 0.45f, w * 0.70f, h * 1.10f, w, h * 0.85f)
                            lineTo(w, 0f)
                            lineTo(0f, 0f)
                            close()
                        },
                        color = AS_HeaderBg
                    )
                }
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(top = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "My Profile",
                        fontSize   = 28.sp,
                        fontFamily = FontFamily.Serif,
                        color      = AS_TextDark,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier          = Modifier.padding(vertical = 4.dp)
                    ) {
                        Box(modifier = Modifier.size(4.dp).background(AS_Primary, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.FilterVintage, null, tint = AS_Primary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(modifier = Modifier.size(4.dp).background(AS_Primary, CircleShape))
                    }
                }
            }

            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AS_CardBg, RoundedCornerShape(24.dp))
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(AS_HeaderBg, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Person, null,
                                tint     = AS_Primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Guest User",
                            fontFamily = FontFamily.Serif,
                            fontSize   = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color      = AS_TextDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Login to access your full artisan profile",
                            color     = AS_TextLight,
                            fontSize  = 13.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .background(AS_Primary, RoundedCornerShape(24.dp))
                                .clip(RoundedCornerShape(24.dp))
                                .clickable { onLoginClick() },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Text(
                                "Login to Continue",
                                color      = Color.White,
                                fontSize   = 16.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// HEADER — pixel-perfect copy of ArtisanSetupScreen header
// ─────────────────────────────────────────────────────────────
@Composable
private fun ProfileHeader(
    isEditing:   Boolean,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Adjusted height to reduce unnecessary space
    ) {
        // ── Exact same Canvas curve as ArtisanSetupScreen ────
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val mainPath = Path().apply {
                moveTo(0f, h * 0.60f)
                cubicTo(
                    w * 0.35f, h * 0.45f,
                    w * 0.70f, h * 1.10f,
                    w,          h * 0.85f
                )
                lineTo(w, 0f)
                lineTo(0f, 0f)
                close()
            }
            drawPath(mainPath, color = AS_HeaderBg)
        }

        // 🔥 FIX: Absolute Centered Title Block
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text       = "My Profile",
                fontSize   = 28.sp,
                fontFamily = FontFamily.Serif,
                color      = AS_TextDark,
                fontWeight = FontWeight.Bold
            )
            // Exact ornament dots + FilterVintage icon centered
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.padding(vertical = 4.dp)
            ) {
                Box(modifier = Modifier.size(4.dp).background(AS_Primary, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector        = Icons.Default.FilterVintage,
                    contentDescription = null,
                    tint               = AS_Primary,
                    modifier           = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(4.dp).background(AS_Primary, CircleShape))
            }
        }

        // ── Back button (Floating Left) ──
        Surface(
            onClick          = onBackClick,
            modifier         = Modifier.padding(top = 48.dp, start = 24.dp).size(56.dp).align(Alignment.TopStart),
            shape            = CircleShape,
            color            = AS_BackBtn,
            shadowElevation  = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint               = AS_BackBtnIcon,
                    modifier           = Modifier.size(24.dp)
                )
            }
        }

        // ── Edit button (Floating Right) ──
        Surface(
            onClick         = onEditClick,
            modifier        = Modifier.padding(top = 48.dp, end = 24.dp).size(56.dp).align(Alignment.TopEnd),
            shape           = CircleShape,
            color           = AS_BackBtn,
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector        = if (isEditing) Icons.Outlined.Check else Icons.Outlined.Edit,
                    contentDescription = if (isEditing) "Save" else "Edit",
                    tint               = if (isEditing) AS_Primary else AS_BackBtnIcon,
                    modifier           = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// PROFILE IDENTITY CARD
// ─────────────────────────────────────────────────────────────
@Composable
private fun ProfileIdentityCard(name: String, email: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AS_CardBg, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar circle
        Box {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(AS_HeaderBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Person, null,
                    tint     = AS_Primary,
                    modifier = Modifier.size(40.dp)
                )
            }
            // Camera badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(26.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, AS_HeaderBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.PhotoCamera, "Change photo",
                    tint     = AS_Primary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text       = name,
                fontFamily = FontFamily.Serif,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                color      = AS_TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Email, null,
                    tint     = AS_Primary.copy(alpha = 0.7f),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(email, fontSize = 13.sp, color = AS_TextLight)
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Artisan member badge — same beige pill style
            Box(
                modifier = Modifier
                    .background(AS_HeaderBg, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    "Artisan Member",
                    fontSize   = 11.sp,
                    color      = AS_Primary,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// PERSONAL INFORMATION SECTION
// ─────────────────────────────────────────────────────────────
@Composable
private fun PersonalInfoSection(
    isEditing:       Boolean,
    name:            String,
    email:           String,
    phone:           String,
    location:        String,
    language:        String,
    onNameChange:     (String) -> Unit,
    onPhoneChange:    (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AS_CardBg, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        // Section header — same style as ArtisanSetupScreen form header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.PersonOutline, null,
                tint     = AS_Primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Personal Information",
                    fontSize   = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color      = AS_TextDark
                )
                Text(
                    if (isEditing) "Tap a field to edit" else "Your artisan details",
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color      = AS_TextLight
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = AS_HeaderBg, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))

        if (isEditing) {
            // ── EDIT MODE: exact CustomInputField style ────────
            ProfileInputField(
                label         = "Full Name",
                value         = name,
                placeholder   = "Enter your full name",
                icon          = Icons.Outlined.PersonOutline,
                onValueChange = onNameChange
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInputField(
                label         = "Email Address",
                value         = email,
                placeholder   = "Email",
                icon          = Icons.Outlined.Email,
                enabled       = false,
                onValueChange = {}
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInputField(
                label         = "Phone Number",
                value         = phone,
                placeholder   = "Enter your phone number",
                icon          = Icons.Outlined.Phone,
                onValueChange = onPhoneChange
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInputField(
                label         = "Village / Location",
                value         = location,
                placeholder   = "Enter your village or location",
                icon          = Icons.Outlined.LocationOn,
                onValueChange = onLocationChange
            )
            Spacer(modifier = Modifier.height(20.dp))
            ProfileInputField(
                label         = "Language",
                value         = language,
                placeholder   = "Preferred language",
                icon          = Icons.Outlined.Language,
                onValueChange = onLanguageChange
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Security info box — same as ArtisanSetupScreen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AS_HeaderBg, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Outlined.GppGood, null,
                    tint     = Color(0xFF5A6B47),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "Your information is safe with us",
                        fontSize   = 13.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF5A6B47)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "We don't share your personal details with anyone.",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif,
                        color      = AS_TextLight
                    )
                }
            }
        } else {
            // ── VIEW MODE: info rows ───────────────────────────
            InfoRow(Icons.Outlined.PersonOutline, "Full Name",  name)
            InfoRow(Icons.Outlined.Email,         "Email",      email)
            InfoRow(Icons.Outlined.Phone,         "Phone",      phone.ifBlank { "—" })
            InfoRow(Icons.Outlined.LocationOn,    "Location",   location.ifBlank { "—" })
            InfoRow(Icons.Outlined.Language,      "Language",   language.ifBlank { "—" }, isLast = true)
        }
    }
}

// ─────────────────────────────────────────────────────────────
// PROFILE INPUT FIELD — exact CustomInputField from ArtisanSetupScreen
// ─────────────────────────────────────────────────────────────
@Composable
private fun ProfileInputField(
    label:         String,
    value:         String,
    placeholder:   String,
    icon:          ImageVector,
    enabled:       Boolean = true,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text       = label,
            fontSize   = 13.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color      = AS_TextDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    if (enabled) AS_FieldBg else AS_FieldBg.copy(alpha = 0.6f),
                    RoundedCornerShape(18.dp)
                )
                .border(1.dp, AS_HeaderBg, RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = if (enabled) AS_Primary else AS_Primary.copy(alpha = 0.4f),
                modifier           = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value         = value,
                onValueChange = if (enabled) onValueChange else { _ -> },
                enabled       = enabled,
                textStyle     = TextStyle(
                    fontSize = 14.sp,
                    color    = if (enabled) AS_TextDark else AS_TextLight
                ),
                cursorBrush   = SolidColor(AS_Primary),
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text   = placeholder,
                            color  = Color.Gray.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// ACCOUNT & SECURITY SECTION
// ─────────────────────────────────────────────────────────────
@Composable
private fun AccountSecuritySection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AS_CardBg, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        SectionHeader(icon = Icons.Outlined.Security, title = "Account & Security", subtitle = "Manage your account")
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = AS_HeaderBg, thickness = 1.dp)
        ActionRow(Icons.Outlined.Lock,         "Change Password")
        ActionRow(Icons.Outlined.VerifiedUser, "Privacy & Security", isLast = true)
    }
}

// ─────────────────────────────────────────────────────────────
// MORE SECTION
// ─────────────────────────────────────────────────────────────
@Composable
private fun MoreSection(onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AS_CardBg, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        SectionHeader(icon = Icons.Outlined.MoreHoriz, title = "More", subtitle = "Help and other options")
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = AS_HeaderBg, thickness = 1.dp)
        ActionRow(Icons.Outlined.HelpOutline, "Help & Support")
        ActionRow(
            icon    = Icons.Outlined.Logout,
            label   = "Log Out",
            isLast  = true,
            tint    = Color(0xFFC0392B),
            onClick = onLogoutClick
        )
    }
}

// ─────────────────────────────────────────────────────────────
// SHARED COMPOSABLES
// ─────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(icon: ImageVector, title: String, subtitle: String = "") {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = AS_Primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                title,
                fontSize   = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color      = AS_TextDark
            )
            if (subtitle.isNotBlank()) {
                Text(
                    subtitle,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color      = AS_TextLight
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon:   ImageVector,
    label:  String,
    value:  String,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = AS_Primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(14.dp))
                Text(label, color = AS_TextLight, fontSize = 14.sp, fontFamily = FontFamily.Serif)
            }
            Text(
                value,
                color      = AS_TextDark,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign  = TextAlign.End
            )
        }
        if (!isLast) Divider(color = AS_HeaderBg.copy(alpha = 0.7f), thickness = 0.8.dp)
    }
}

@Composable
private fun ActionRow(
    icon:    ImageVector,
    label:   String,
    isLast:  Boolean = false,
    tint:    Color   = AS_TextDark,
    onClick: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    onClick           = onClick
                )
                .padding(vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon, null,
                    tint     = if (tint == AS_TextDark) AS_Primary else tint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    label,
                    color      = tint,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
            Icon(
                Icons.Outlined.ChevronRight, null,
                tint     = AS_HeaderBg,
                modifier = Modifier.size(20.dp)
            )
        }
        if (!isLast) Divider(color = AS_HeaderBg.copy(alpha = 0.7f), thickness = 0.8.dp)
    }
}