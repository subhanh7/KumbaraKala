package com.kumbarakala.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kumbarakala.app.R
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.kumbarakala.app.utils.SharedPrefHelper
import androidx.compose.material3.Surface
@Composable
fun ArtisanSetupScreen(navController: NavHostController) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    // 🔥 Exact Theme Colors from your HomeScreen 🔥
    val bgColor = Color(0xFFFAF5EF)
    val headerBg = Color(0xFFF3EAE0)
    val textTitleDark = Color(0xFF3E2B23)
    val primaryAction = Color(0xFFC05D35)
    val textLight = Color(0xFF8A827B)

    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // --- 1. HEADER SECTION (Curve + Pot + Title) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                // Exact Canvas Curve from HomeScreen
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val mainPath = Path().apply {
                        moveTo(0f, h * 0.60f)
                        cubicTo(
                            w * 0.35f, h * 0.45f,
                            w * 0.70f, h * 1.10f,
                            w, h * 0.85f
                        )
                        lineTo(w, 0f)
                        lineTo(0f, 0f)
                        close()
                    }
                    drawPath(mainPath, color = headerBg)
                }

                // Exact Pot Image from HomeScreen
                Image(
                    painter = painterResource(id = R.drawable.header_pot), // Uses the same pot!
                    contentDescription = "Decorative Pot",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(
                            x = (-8).dp,
                            y = 70.dp
                        ) // Adjusted Y so it sits nicely with the title
                        .size(150.dp)
                )

                // Top Bar Content (Back button & Title)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 56.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Back Button
                    Surface(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = Color(0xFFF5EEE6), // Soft glass beige
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF50342E), // Deep brown
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Center Title
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .offset(
                                x = (-22).dp,
                                y = 12.dp
                            ), // Offset by half the back button to center perfectly
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Artisan Setup",
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Serif,
                            color = textTitleDark,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(4.dp)
                                    .background(primaryAction, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.FilterVintage,
                                contentDescription = null,
                                tint = primaryAction,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier.size(4.dp)
                                    .background(primaryAction, CircleShape)
                            )
                        }
                        Text(
                            text = "Tell us about yourself",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Serif,
                            color = textLight
                        )
                    }
                }
            }

            // --- 2. SCROLLABLE FORM CONTENT ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // "WHY THIS MATTERS" CARD
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(primaryAction, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterVintage, // Floral icon
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Why this matters?",
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = textTitleDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your details will appear on the story cards, helping customers connect with you.",
                            fontSize = 12.sp,
                            color = textLight,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // MAIN FORM CARD
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    // Form Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.PersonOutline,
                            contentDescription = null,
                            tint = primaryAction,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Artisan Information",
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = textTitleDark
                            )
                            Text(
                                text = "Enter your details below",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = textLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Input Fields
                    CustomInputField(
                        "Full Name",
                        fullName,
                        "Enter your full name",
                        Icons.Outlined.PersonOutline
                    ) { fullName = it }
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomInputField(
                        "Phone Number",
                        phoneNumber,
                        "Enter your phone number",
                        Icons.Outlined.Phone
                    ) { phoneNumber = it }
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomInputField(
                        "Village / Location",
                        location,
                        "Enter your village or location",
                        Icons.Outlined.LocationOn
                    ) { location = it }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Security Info Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBg, RoundedCornerShape(18.dp)) // Uses exact HeaderBg
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.GppGood, // Shield icon
                            contentDescription = null,
                            tint = Color(0xFF5A6B47),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Your information is safe with us",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5A6B47)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "We don't share your personal details with anyone.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.Serif,
                                color = textLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // SAVE BUTTON
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(primaryAction, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {

                            // 1. Save the data to SharedPreferences
                            val prefs = SharedPrefHelper(context)
                            prefs.saveString("artisan_name", fullName)
                            prefs.saveString("artisan_phone", phoneNumber)
                            prefs.saveString("artisan_location", location)

                            // 2. Show the success message
                            Toast.makeText(
                                context,
                                "Details Saved",
                                Toast.LENGTH_SHORT
                            ).show()

                            // 🔥 3. Navigate back to Home and clear this setup screen from history
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save Details",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Bottom padding
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


// --- CUSTOM INPUT FIELD COMPONENT ---
@Composable
fun CustomInputField(
    label: String,
    value: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onValueChange: (String) -> Unit
) {
    val textTitleDark = Color(0xFF3E2B23)
    val primaryAction = Color(0xFFC05D35)
    val headerBg = Color(0xFFF3EAE0)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = textTitleDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(Color(0xFFFAF7F2), RoundedCornerShape(18.dp))
                .border(1.dp, headerBg, RoundedCornerShape(18.dp)) // Border matches HeaderBg
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = primaryAction,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 14.sp, color = textTitleDark),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = Color.Gray.copy(alpha = 0.8f), fontSize = 14.sp)
                    }
                    innerTextField()
                }
            )
        }
    }
}