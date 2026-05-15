package com.kumbarakala.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

private val AS_BgColor = Color(0xFFFAF5EF)
private val AS_HeaderBg = Color(0xFFF3EAE0)
private val AS_TextDark = Color(0xFF3E2B23)
private val AS_Primary = Color(0xFFC05D35)
private val AS_TextLight = Color(0xFF8A827B)
private val AS_CardBg = Color.White

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddProductScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val permanentUri = saveImageToInternalStorage(context, uri)
                selectedImageUri = permanentUri?.toString()
            }
        }
    )
    val scrollState = rememberScrollState()

    // Form States
    var productName by remember { mutableStateOf("") }
    var productDesc by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Pots") }
    var selectedFeatures by remember { mutableStateOf(setOf<String>()) }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    var isEcoFriendly by remember { mutableStateOf(true) }
    var isHandmade by remember { mutableStateOf(true) }

    val categories = listOf("Pots", "Lamps", "Utensils", "Clay Glass", "Decorative", "Plates")
    val tags = listOf(
        "Rustic Charm",
        "Minimalist",
        "Cultural Heritage",
        "Unglazed Finish",
        "Gift Idea",
        "Daily Use",
        "Festive Decor",
        "Heirloom Quality"
    )

    val featureList = listOf(
        Pair("Keeps Water Naturally Cool", Icons.Outlined.WaterDrop),
        Pair("100% Natural & Eco-friendly", Icons.Outlined.Eco),
        Pair("Healthy for You & Family", Icons.Outlined.FavoriteBorder),
        Pair("Handcrafted by Artisans", Icons.Outlined.Handshake),
        Pair("Traditional Village Craft", Icons.Outlined.Home),
        Pair("Sustainable Material", Icons.Outlined.Recycling)
    )

    Scaffold(
        containerColor = AS_BgColor,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            AddProductHeader(onBackClick = { navController.popBackStack() })

            Column(modifier = Modifier.padding(horizontal = 24.dp).offset(y = (-40).dp)) {

                // --- IMAGE UPLOAD ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .height(200.dp)
                        // 🔥 FIX 1: Clip -> Clickable -> Background (Removes square ripple)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .background(AS_HeaderBg)
                        .border(1.dp, AS_Primary.copy(alpha = 0.3f), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.FileUpload, null, tint = AS_Primary, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Upload Product Image", fontWeight = FontWeight.Bold, color = AS_TextDark, fontFamily = FontFamily.Serif)
                            Text("Tap to upload from gallery", fontSize = 12.sp, color = AS_TextLight, fontFamily = FontFamily.Serif)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- PRODUCT INFO ---
                PremiumTextField(
                    "Product Name *",
                    productName,
                    "e.g. Clay Water Pot",
                    leadingIcon = Icons.Outlined.Inventory2
                ) { productName = it }
                Spacer(modifier = Modifier.height(16.dp))
                PremiumTextField(
                    "Product Description *",
                    productDesc,
                    "Describe your product...",
                    isMultiLine = true,
                    leadingIcon = Icons.Outlined.Description
                ) { productDesc = it }

                Spacer(modifier = Modifier.height(24.dp))

                // --- CATEGORIES ---
                Text("Category", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AS_TextDark, fontFamily = FontFamily.Serif)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        SelectableChip(text = cat, isSelected = selectedCategory == cat) { selectedCategory = cat }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                // --- FEATURES ---
                Text("Product Features", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AS_TextDark, fontFamily = FontFamily.Serif)
                Spacer(modifier = Modifier.height(12.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(260.dp),
                    userScrollEnabled = false,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(featureList) { feature ->
                        FeatureCard(
                            text = feature.first,
                            icon = feature.second,
                            isSelected = selectedFeatures.contains(feature.first)
                        ) {
                            if (selectedFeatures.contains(feature.first)) selectedFeatures -= feature.first
                            else selectedFeatures += feature.first
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- TAGS ---
                Text("Product Tags", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AS_TextDark, fontFamily = FontFamily.Serif)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach { tag ->
                        SelectableChip(text = tag, isSelected = selectedTags.contains(tag)) {
                            if (selectedTags.contains(tag)) selectedTags -= tag else selectedTags += tag
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- TOGGLES ---
                PremiumToggle("Eco-Friendly", isEcoFriendly) { isEcoFriendly = it }
                Spacer(modifier = Modifier.height(16.dp))
                PremiumToggle("Handmade by Artisan", isHandmade) { isHandmade = it }

                Spacer(modifier = Modifier.height(32.dp))

                // --- CONTINUE BUTTON ---
                Button(
                    onClick = {
                        if (productName.isNotEmpty()) {
                            com.kumbarakala.app.model.addNewProductToGlobalList(
                                name = productName,
                                category = selectedCategory,
                                description = productDesc,
                                imageUri = selectedImageUri
                            )
                            com.kumbarakala.app.model.saveProductsToDisk(context)
                            navController.navigate("artisanSetup")
                        } else {
                            Toast.makeText(context, "Please enter a product name", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AS_Primary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Continue to Artisan Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Serif // 🔥 MATCHING FONT
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Continue",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // 🔥 FIX 3: Removed the massive 100.dp space, changed to 24.dp
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AddProductHeader(onBackClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {

        // 🔥 THE FIX: Copied the exact wave path from your HomeScreen
        Canvas(modifier = Modifier.fillMaxSize()) {
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
            drawPath(mainPath, color = AS_HeaderBg)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 16.dp, start = 20.dp, end = 20.dp)
        ) {
            // BACK BUTTON
            Surface(
                onClick = onBackClick,
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterStart),
                shape = CircleShape,
                color = Color(0xFFF5EEE6),
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = AS_TextDark)
                }
            }

            // TITLE & DECORATION
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add New Product",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Serif,
                    color = AS_TextDark,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(modifier = Modifier.size(5.dp).background(AS_Primary, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.FilterVintage, null, tint = AS_Primary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(5.dp).background(AS_Primary, CircleShape))
                }
            }
        }
    }
}

@Composable
fun PremiumTextField(
    label: String,
    value: String,
    hint: String,
    isMultiLine: Boolean = false,
    leadingIcon: ImageVector? = null,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AS_TextDark, fontFamily = FontFamily.Serif)
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 15.sp, color = AS_TextDark, fontFamily = FontFamily.Serif),
            cursorBrush = SolidColor(AS_Primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMultiLine) 120.dp else 56.dp)
                // 🔥 CHANGED THIS LINE TO PURE WHITE
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE8DFD5), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = if (isMultiLine) 16.dp else 0.dp),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = if (isMultiLine) Alignment.Top else Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = AS_Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(hint, color = AS_TextLight, fontSize = 15.sp, fontFamily = FontFamily.Serif)
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun SelectableChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            // 🔥 FIX 1: Clip -> Clickable -> Background
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .background(if (isSelected) AS_Primary else Color.Transparent)
            .border(1.dp, if (isSelected) AS_Primary else AS_HeaderBg, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 🔥 FIX 2: Added Serif font
        Text(text, color = if (isSelected) Color.White else AS_TextDark, fontSize = 13.sp, fontWeight = FontWeight.Medium, fontFamily = FontFamily.Serif)
    }
}

@Composable
fun FeatureCard(text: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            // 🔥 FIX 1: Clip -> Clickable -> Background
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(if (isSelected) AS_HeaderBg else AS_CardBg)
            .border(1.dp, if (isSelected) AS_Primary else AS_HeaderBg, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, null, tint = AS_Primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        // 🔥 FIX 2: Added Serif font
        Text(text, fontSize = 11.sp, color = AS_TextDark, textAlign = TextAlign.Center, lineHeight = 14.sp, fontFamily = FontFamily.Serif)
    }
}

@Composable
fun PremiumToggle(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AS_TextDark, fontFamily = FontFamily.Serif)
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AS_Primary, uncheckedThumbColor = AS_TextLight, uncheckedTrackColor = AS_HeaderBg)
        )
    }
}
fun saveImageToInternalStorage(context: android.content.Context, uri: android.net.Uri): android.net.Uri? {
    val fileName = "product_${java.util.UUID.randomUUID()}.jpg"
    val file = java.io.File(context.filesDir, fileName)

    return try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        android.net.Uri.fromFile(file)
    } catch (e: Exception) {
        null
    }
}