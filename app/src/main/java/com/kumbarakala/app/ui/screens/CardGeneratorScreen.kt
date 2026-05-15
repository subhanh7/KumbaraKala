package com.kumbarakala.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kumbarakala.app.R
import com.kumbarakala.app.model.Product
import com.kumbarakala.app.model.productList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.luminance
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.SolidColor
import com.kumbarakala.app.model.CardViewModel
import androidx.compose.ui.platform.LocalContext
import com.kumbarakala.app.utils.SharedPrefHelper
import coil.compose.AsyncImage
// --- 1. THE STATE MODEL ---

// 🔥 1. Add this blueprint so the app knows what a CardBackground is
data class CardBackground(
    val id: String,
    val color: androidx.compose.ui.graphics.Color,
    val hasShadowOverlay: Boolean = false
)

// 🔥 2. Make sure your StoryCardState looks exactly like this!
// (We changed backgroundColor to just 'background' and used our new class)
data class StoryCardState(
    val product: Product, // Assuming you have a Product class elsewhere
    val background: CardBackground = CardBackground("Dark", androidx.compose.ui.graphics.Color(0xFF2A1F1A)),
    val titleFont: androidx.compose.ui.text.font.FontFamily = androidx.compose.ui.text.font.FontFamily.Serif,
    val textColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color(0xFFD4AF37),
    val showHealth: Boolean = true,
    val showEco: Boolean = true,
    val showArtisan: Boolean = true,
    val isGradient: Boolean = true
)
// --- 2. THE MAIN SCREEN ---
@Composable
fun CardGeneratorScreen(navController: NavHostController,
                        product: Product,
                        cardViewModel: CardViewModel) {
    val context = LocalContext.current

    val prefs = SharedPrefHelper(context)

    val artisanName =
        prefs.getString("artisan_name")

    val artisanPhone =
        prefs.getString("artisan_phone")

    val artisanLocation =
        prefs.getString("artisan_location")
    // 🔥 Find the actual product data from your list based on the ID passed

    // Initialize state with the SELECTED product's details
    val existingState = cardViewModel.currentCardState.value

    var cardState by remember {
        mutableStateOf(
            existingState ?: StoryCardState(
                product = product,
                background = CardBackground("Dark", Color(0xFF1A120E))
            )
        )
    }
    // 3. UI State for the bottom editor panel
    var selectedTab by remember { mutableStateOf("Background") }

    // Theme Colors
    val BgColor = Color(0xFFFAF5EF)// 🔥 Warm Cream
    val textTitleDark = Color(0xFF3E2B23) // 🔥 Deep Earthy Brown
    val primaryAction = Color(0xFFC05D35)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BgColor)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(54.dp))
            // --- TOP BAR ---
            TopHeaderBar(navController, textTitleDark)

            Spacer(modifier = Modifier.height(24.dp))

            // --- LIVE PREVIEW ---
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text("Live Preview", fontSize = 16.sp,fontFamily = FontFamily.Serif, color = textTitleDark)
                Spacer(modifier = Modifier.height(16.dp))

                // 🔥 Pass the entire state to the preview card
                StoryCardPreview(state = cardState)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- CUSTOMIZATION EDITOR ---
            CustomizationPanel(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                currentState = cardState,
                onStateChange = { newState ->
                    cardState = newState
                    cardViewModel.saveCard(newState)
                },
                textTitleDark = textTitleDark
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- GENERATE BUTTON ---
            GenerateFinalCardButton(primaryAction) {
                cardViewModel.saveCard(cardState)
                navController.navigate("shareScreen")
            }

            // Padding for bottom nav
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}
// --- 3. THE LIVE PREVIEW CARD ---
@Composable
fun StoryCardPreview(state: StoryCardState) {
    val context = LocalContext.current
    val prefs = SharedPrefHelper(context)

    val artisanName = prefs.getString("artisan_name")
    val artisanPhone = prefs.getString("artisan_phone")
    val artisanLocation = prefs.getString("artisan_location")
    val dynamicAccent = state.textColor
    val gold = Color(0xFFD4AF37)

    Card(
        shape = RoundedCornerShape(28.dp), // 🔥 Slightly rounder for premium look
        elevation = CardDefaults.cardElevation(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(620.dp) // 🔥 Increased height for better proportions
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Cinematic Background Image
            if (state.product.imageUri != null) {
                // 🔥 Shows the photo you uploaded via the app
                coil.compose.AsyncImage(
                    model = state.product.imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Shows your original hardcoded pots
                Image(
                    painter = painterResource(id = state.product.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 2. Premium Gradient Overlay (Protects Top & Bottom text)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (state.isGradient) {
                            Brush.verticalGradient(
                                0.0f to Color.Black.copy(alpha = 0.85f),
                                0.4f to Color.Black.copy(alpha = 0.2f),
                                0.7f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.6f)
                            ) // 🔥 Removed the extra parenthesis that was right here
                        } else {
                            // A flat, dim overlay so text is still readable when the gradient is off
                            SolidColor(Color.Black.copy(alpha = 0.3f))
                        }
                    )
            )
// 🔥 2.5 The Dynamic Canvas Shadow! (Drawn over the background, under the text)
            if (state.background.hasShadowOverlay) {
                LeafShadowOverlay(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 20.dp, y = (-20).dp) // Tweak this if you want the shadow moved!
                )
            }
            // 3. Text Content
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 24.dp,
                    bottom = 24.dp,
                    end = 100.dp
                ) // 🔥 FIX: Pushes text away from the right edge
            ) {
                Text(
                    text = "• TRADITIONAL • PURE • NATURAL •",
                    color = dynamicAccent,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = state.product.name.uppercase(),
                    color = dynamicAccent,
                    fontSize = 36.sp, // 🔥 Balanced size for long names
                    fontFamily = state.titleFont,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 40.sp,
                    maxLines = 2
                )

                Text(
                    text = state.product.subtitle,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Cursive
                )

                // 🔥 Spacer prevents title from overlapping the Benefit icons
                Spacer(modifier = Modifier.height(32.dp))

                if (state.showHealth) {
                    BenefitRow(
                        Icons.Default.AutoAwesome,
                        "HEALTH",
                        "Natural cooling & pH balance.",
                        dynamicAccent
                    )
                }

                if (state.showEco) {
                    Spacer(modifier = Modifier.height(16.dp))
                    BenefitRow(
                        Icons.Default.Eco,
                        "ECO",
                        "100% biodegradable & plastic-free.",
                        dynamicAccent
                    )
                }
            }

            // 4. Eco Badge (Top Right)
            if (state.showEco) {
                Box(
                    modifier = Modifier.align(Alignment.TopEnd).padding(20.dp).size(65.dp)
                        .border(1.dp, state.textColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Eco, null, tint = state.textColor, modifier = Modifier.size(22.dp))
                        Text("ECO", color = state.textColor, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            // 5. Artisan Footer (Fixed: Now populated)
            if (state.showArtisan) {
                val isDarkBg = state.background.color.luminance() < 0.5f // 🔥 Use background.color
                val primaryTextColor = if (isDarkBg) Color.White else Color(0xFF3E2B23)
                val secondaryTextColor =
                    if (isDarkBg) Color.White.copy(alpha = 0.7f) else Color(0xFF5C4033)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .background(state.background.color, RoundedCornerShape(32.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.artisan_ramesh),
                            contentDescription = "Artisan",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(2.dp, primaryTextColor, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Made by",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = secondaryTextColor
                            )
                            Text(
                                text = artisanName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = primaryTextColor
                            )
                            // 🔥 Put BOTH details inside ONE Row
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Location
                                Icon(Icons.Default.LocationOn, contentDescription = null,tint = primaryTextColor, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(artisanLocation, color = secondaryTextColor, fontSize = 12.sp)

                                // Add a little dot separator between them
                                Text("  •  ", color = secondaryTextColor.copy(alpha = 0.5f), fontSize = 12.sp)

                                // Phone
                                Icon(Icons.Default.Phone, contentDescription = null, tint = primaryTextColor, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(artisanPhone, color = secondaryTextColor, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun BenefitRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    accentColor: Color // 🔥 ADD THIS: This allows the function to take the 4th argument
) {
    Row(verticalAlignment = androidx.compose.ui.Alignment.Top) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .border(1.dp, accentColor, androidx.compose.foundation.shape.CircleShape),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
        }
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.fillMaxWidth(0.9f)) {
            androidx.compose.material3.Text(
                text = title,
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
            androidx.compose.material3.Text(
                text = desc,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}
// --- 4. THE CUSTOMIZATION EDITOR ---
@Composable
fun CustomizationPanel(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    currentState: StoryCardState,
    onStateChange: (StoryCardState) -> Unit,
    textTitleDark: Color
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(text = "Customize Your Card", fontSize = 16.sp, fontFamily = FontFamily.Serif,color = textTitleDark)
        Spacer(modifier = Modifier.height(16.dp))

        // Tab Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val tabs = listOf(
                Triple("Background", Icons.Outlined.Image, "Background"),
                Triple("Product", Icons.Outlined.Inventory2, "Product"),
                Triple("Text Style", Icons.Outlined.FontDownload, "Text Style"),
                Triple("Colors", Icons.Outlined.Palette, "Colors"),
                Triple("Elements", Icons.Outlined.Tune, "Elements")
            )

            tabs.forEach { (label, icon, tabName) ->
                EditorTab(label, icon, selectedTab == tabName) { onTabSelected(tabName) }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dynamic Options based on selected tab
        when (selectedTab) {
            "Background" -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val themes = listOf(
                        // --- 1. The Original 10 Earthy Tones ---
                        CardBackground("Espresso", Color(0xFF2A1F1A)),
                        CardBackground("Deep Brown", Color(0xFF4E342E)),
                        CardBackground("Clay", Color(0xFF8B4513)),
                        CardBackground("Terracotta", Color(0xFFCD853F)),
                        CardBackground("Parchment", Color(0xFFE2C792)),
                        CardBackground("Sand", Color(0xFFF5DEB3)),
                        CardBackground("Olive", Color(0xFF556B2F)),
                        CardBackground("Sage", Color(0xFF8FBC8F)),
                        CardBackground("Slate", Color(0xFF2F4F4F)),
                        CardBackground("Ocean", Color(0xFF455A64)),

                        // --- 2. The 5 Light & Bright Tones ---
                        CardBackground("Warm Ivory", Color(0xFFFDF6E3)),
                        CardBackground("Light Sand", Color(0xFFF5E6D3)),
                        CardBackground("Blush", Color(0xFFEEDCD3)),
                        CardBackground("Soft Mist", Color(0xFFE4E9E1)),
                        CardBackground("Alabaster", Color(0xFFE6E2DD)),

                        // --- 3. The NEW Premium Shadowed Tones ---
                        CardBackground(
                            "Espresso Shadow",
                            Color(0xFF2A1F1A),
                            hasShadowOverlay = true
                        ),
                        CardBackground("Clay Shadow", Color(0xFF8B4513), hasShadowOverlay = true),
                        CardBackground("Sand Shadow", Color(0xFFF5DEB3), hasShadowOverlay = true),
                        CardBackground("Olive Shadow", Color(0xFF556B2F), hasShadowOverlay = true),
                        CardBackground("Ivory Shadow", Color(0xFFFDF6E3), hasShadowOverlay = true)
                    )
                    // 🔥 FIX: Loop through 'themes', not 'colors'
                    items(themes) { theme ->
                        // 🔥 Swap ThemeSwatch back to your existing ColorSwatch!
                        ColorSwatch(
                            color = theme.color,
                            isSelected = currentState.background.id == theme.id
                        ) {
                            onStateChange(currentState.copy(background = theme))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Bonus: Gradient Toggle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = currentState.isGradient, onCheckedChange = { onStateChange(currentState.copy(isGradient = it)) })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enable Gradient Depth", fontSize = 14.sp,fontFamily = FontFamily.Serif)
                }
            }

            "Product" -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(com.kumbarakala.app.model.globalProductList) { product ->
                        ProductSelector(product, isSelected = currentState.product.id == product.id) {
                            onStateChange(currentState.copy(product = product))
                        }
                    }
                }
            }

            "Text Style" -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val fonts = listOf(
                        Pair("Serif", FontFamily.Serif),
                        Pair("Modern", FontFamily.SansSerif),
                        Pair("Typewriter", FontFamily.Monospace),
                        Pair("Elegant", FontFamily.Cursive),
                        Pair("Clean", FontFamily.Default),
                    )
                    items(fonts) { fontPair ->
                        FontStyleChip(fontPair.first, fontPair.second, isSelected = currentState.titleFont == fontPair.second) {
                            onStateChange(currentState.copy(titleFont = fontPair.second))
                        }
                    }
                }
            }

            "Colors" -> {
                Text("Accent Color (Text & Icons)", fontSize = 12.sp, color = textTitleDark)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val textColors = listOf(Color(0xFFD4AF37), Color.White, Color(0xFFE2C792), Color(0xFFA8E6CF), Color(0xFFFFD3B6),Color(0xFFFF8B94), // Soft Rose Pink
                        Color(0xFF6C8EBF),
                        Color(0xFF557C55),
                        Color(0xFF8B7355),
                        Color(0xFF333333))
                    items(textColors) { color ->
                        ColorSwatch(color, isSelected = currentState.textColor == color) {
                            onStateChange(currentState.copy(textColor = color))
                        }
                    }
                }
            }

            "Elements" -> {
                Column {
                    ElementToggle("Show Health Benefit", currentState.showHealth) { onStateChange(currentState.copy(showHealth = it)) }
                    ElementToggle("Show Eco Benefit & Badge", currentState.showEco) { onStateChange(currentState.copy(showEco = it)) }
                    ElementToggle("Show Artisan Profile", currentState.showArtisan) { onStateChange(currentState.copy(showArtisan = it)) }
                }
            }
        }
    }
}

// --- HELPER COMPONENTS ---

@Composable
fun EditorTab(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    // 🔥 Define brand-matching colors
    val brandOrange = Color(0xFFC05D35)
    val color = if (isSelected) brandOrange else Color(0xFF8A827B)

    // 🔥 Use a soft tinted background (10% opacity) instead of pure white
    val bgColor = if (isSelected) brandOrange.copy(alpha = 0.1f) else Color.Transparent

    // 🔥 Make the border thicker (2.dp) when selected to feel more "active"
    val borderWeight = if (isSelected) 2.dp else 1.dp
    val borderColor = if (isSelected) brandOrange else Color(0xFFE5DCD3)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(bgColor, RoundedCornerShape(12.dp))
                .border(borderWeight, borderColor, RoundedCornerShape(12.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = color,
            fontFamily = FontFamily.Serif, // Matches your home screen style
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ColorSwatch(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(color, RoundedCornerShape(12.dp))
            .border(if (isSelected) 2.dp else 0.dp, if (isSelected) Color(0xFFC05D35) else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun ProductSelector(product: Product, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) Color(0xFFC05D35) else Color(0xFFE5DCD3),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            // 🔥 THE FIX: Check if it's a custom gallery image or a hardcoded app image
            if (product.imageUri != null) {
                coil.compose.AsyncImage(
                    model = product.imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = product.name,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFFC05D35) else Color(0xFF3E2B23)
        )
    }
}

@Composable
fun FontStyleChip(label: String, font: FontFamily, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) Color(0xFFC05D35) else Color.White
    val textColor = if (isSelected) Color.White else Color(0xFF3E2B23)

    // 🔥 This logic creates the visual difference between the styles
    val weight = when(label) {
        "Headline", "Bold" -> FontWeight.ExtraBold
        "Minimal" -> FontWeight.Light
        else -> FontWeight.Normal
    }

    val style = if (label == "Antique") FontStyle.Italic else FontStyle.Normal

    Box(
        modifier = Modifier
            .height(56.dp)
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5DCD3), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = font,
            fontWeight = weight, // 🔥 Apply the weight
            fontStyle = style,   // 🔥 Apply the italic style
            fontSize = 16.sp,
            color = textColor
        )
    }
}

@Composable
fun ElementToggle(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = Color(0xFF3E2B23))
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun TopHeaderBar(navController: NavHostController, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp), // Adjusted padding for larger buttons
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            onClick = { navController.popBackStack() },
            modifier = Modifier.size(56.dp), // 🔥 Increased size to match Share Screen
            shape = CircleShape,
            color = Color(0xFFF5EEE6), // The soft beige liquid-glass color
            shadowElevation = 4.dp // Adds that professional depth
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
        // Title Column
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Create Story Card",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                color = color, // 🔥 Matches homescreen textTitleDark
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Design your story",
                fontSize = 12.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic, // Optional: matches your "Made by" style
                color = color.copy(alpha = 0.6f) // 🔥 Soft version of homescreen brown
            )
        }

        // Spacer to keep the title perfectly centered
        Box(modifier = Modifier.size(44.dp))
    }
}

@Composable
fun GenerateFinalCardButton(color: Color, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 20.dp)) {

        // 🔥 We use a Box here instead of a Row to control exact placement
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(color, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                )
        ) {

            // 1. The Centered Content (Wand + "Generate Story Card")
            Row(
                modifier = Modifier.align(Alignment.Center), // 🔥 Forces it dead center!
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoFixHigh,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Generate Story Card",
                    color = Color.White,
                    fontSize = 18.sp, // Made slightly larger since subtitle is gone
                    fontFamily = FontFamily.Serif
                )
            }

            // 2. The Arrow Button (Docked to the Right)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd) // 🔥 Keeps it on the far right
                    .padding(end = 12.dp)
                    .size(40.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
@Composable
fun LeafShadowOverlay(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .blur(radius = 24.dp)
    ) {
        val width = size.width
        val height = size.height

        val leafPath = Path().apply {
            moveTo(0f, 0f)
            quadraticTo(-40f, 60f, 0f, 120f)
            quadraticTo(40f, 60f, 0f, 0f)
            close()
        }

        val shadowColor = Color(0xFF1A120E).copy(alpha = 0.45f)

        val stemPath = Path().apply {
            moveTo(width + 50f, -50f)
            quadraticTo(width * 0.7f, height * 0.2f, width * 0.4f, height * 0.5f)
        }
        drawPath(
            path = stemPath,
            color = shadowColor,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12f)
        )

        fun drawLeaf(x: Float, y: Float, rotation: Float, scale: Float = 1f) {
            withTransform({
                translate(left = x, top = y)
                rotate(degrees = rotation)
                scale(scaleX = scale, scaleY = scale)
            }) {
                drawPath(path = leafPath, color = shadowColor)
            }
        }

        drawLeaf(width * 0.9f, -20f, rotation = 45f, scale = 1.2f)
        drawLeaf(width * 0.85f, height * 0.05f, rotation = 110f, scale = 1.0f)
        drawLeaf(width * 0.75f, height * 0.1f, rotation = 30f, scale = 0.9f)
        drawLeaf(width * 0.65f, height * 0.18f, rotation = 120f, scale = 1.1f)
        drawLeaf(width * 0.55f, height * 0.25f, rotation = 40f, scale = 0.8f)
        drawLeaf(width * 0.45f, height * 0.35f, rotation = 135f, scale = 0.9f)
    }
}