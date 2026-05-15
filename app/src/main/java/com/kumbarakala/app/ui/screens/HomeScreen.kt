package com.kumbarakala.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kumbarakala.app.R
import com.kumbarakala.app.model.Product
import com.kumbarakala.app.model.FavoritesManager
import com.kumbarakala.app.data.supabase
import io.github.jan.supabase.gotrue.auth
import androidx.compose.foundation.border
import coil.compose.AsyncImage

val BgColor        = Color(0xFFFAF5EF)
val HeaderBg       = Color(0xFFF3EAE0)
val TextTitleDark  = Color(0xFF3E2B23)
val TextSubtitle   = Color(0xFFB55D3D)
val PrimaryAction  = Color(0xFFC05D35)
val TextLight      = Color(0xFF8A827B)
val CardBg         = Color(0xFFFFFFFF)
val ChipInactiveBg = Color(0xFFF0E5D9)

@Composable
fun KumbaraKalaScreen(
    navController: androidx.navigation.NavHostController,
    onNavigateToGenerator: () -> Unit
) {
    var showCreateSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    // 🔥 Dynamic List
    val allProducts = com.kumbarakala.app.model.globalProductList
    val isUserLoggedIn = remember {
        supabase.auth.currentUserOrNull() != null
    }

    // Create the filtered list based on the state
    val filteredList = allProducts.filter { product ->
        val matchesCategory = selectedCategory == "All" || product.category == selectedCategory
        val matchesSearch = product.name.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize().background(BgColor)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item { TopHeaderSection() }

            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    TextAndSearchSection(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it }
                    )
                }
            }

            item {
                CategoriesSection(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            items(filteredList.chunked(2).size) { index ->
                val rowProducts = filteredList.chunked(2)[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (product in rowProducts) {
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(
                                product = product,
                                navController = navController
                            )
                        }
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        CustomBottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController,
            isLoggedIn = isUserLoggedIn,
            onFabClick = { showCreateSheet = true }
        )

        // 🔥 Custom Floating Menu Overlay
        // 🔥 Custom Floating Menu Overlay
        if (showCreateSheet) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)) // The dark background dim
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showCreateSheet = false },
                contentAlignment = Alignment.BottomCenter
            ) {
                // The main container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 40.dp), // Perfect hover height above the nav bar
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // 🔥 THE NEW TRANSPARENT GLASS BOX OVERLAY
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White.copy(alpha = 0.25f)) // The glassy transparent tint
                            .border(
                                1.dp,
                                Color.White.copy(alpha = 0.4f),
                                RoundedCornerShape(32.dp)
                            ) // Glass edge reflection
                            .padding(16.dp), // Inner padding so cards don't touch the edges
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Option 1: Select Existing
                        FloatingMenuCard(
                            icon = Icons.Default.List,
                            title = "Select Existing Product",
                            subtitle = "Choose from your current handcrafted collection",
                            onClick = {
                                navController.navigate("cardGenerator/default") // 🔥 MUST NAVIGATE FIRST
                                showCreateSheet = false                         // 🔥 THEN CLOSE
                            }
                        )

                        // Option 2: Add New
                        FloatingMenuCard(
                            icon = Icons.Default.AddCircle,
                            title = "Add New Product",
                            subtitle = "Add a new handcrafted item and generate a story",
                            onClick = {
                                navController.navigate("addProduct") // 🔥 MUST NAVIGATE FIRST
                                showCreateSheet = false              // 🔥 THEN CLOSE
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp)) // Space between the glass box and the 'X'

                    // The Circular Close Button (Orange 'X')
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .shadow(8.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.5f))
                            .background(Color(0xFFC05D35), CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { showCreateSheet = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES
// -------------------------------------------------------------

@Composable
fun TopHeaderSection() {
    val headerHeight = 190.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
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
            drawPath(mainPath, color = HeaderBg)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 0.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kumbara-Kala",
                        fontSize = 30.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Medium,
                        color = TextTitleDark,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "❖ Tradition meets storytelling ❖",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Serif,
                        color = TextSubtitle,
                        modifier = Modifier.offset(y = (-6).dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.header_pot),
            contentDescription = "Decorative Pot",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-8).dp, y = 105.dp)
                .size(150.dp)
        )
    }
}

@Composable
fun TextAndSearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(modifier = Modifier.offset(y = (-12).dp)) {
        Text(
            text = "Our Collection",
            fontSize = 18.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Medium,
            color = TextTitleDark
        )
        Text(
            text = "Handcrafted with love. Rooted in tradition.",
            fontSize = 11.sp,
            color = TextLight,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.offset(y = (-6).dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFFF4EBE1), RoundedCornerShape(26.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = TextTitleDark,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                if (searchQuery.isEmpty()) {
                    Text("Search products", color = TextLight, fontSize = 14.sp, fontFamily = FontFamily.Serif)
                }
                androidx.compose.foundation.text.BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = TextTitleDark,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

data class CategoryData(val name: String, val emoji: String)

@Composable
fun CategoriesSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf(
        CategoryData("All", ""),
        CategoryData("Pots", "🏺"),
        CategoryData("Lamps", "🪔"),
        CategoryData("Utensils", "🥣"),
        CategoryData("Clays", "🪨")
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 4.dp)
            .offset(y = (-12).dp)
    ) {
        items(categories.size) { i ->
            val cat = categories[i]
            val isSelected = cat.name == selectedCategory

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) PrimaryAction else ChipInactiveBg)
                    .clickable { onCategorySelected(cat.name) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (cat.emoji.isNotEmpty()) {
                        Text(
                            text = cat.emoji,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    Text(
                        text = cat.name,
                        color = if (isSelected) Color.White else TextTitleDark,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    navController: androidx.navigation.NavHostController
) {
    val isFavorite = FavoritesManager.isFavorite(product.id)
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .clickable {
                navController.navigate("productDetail/${product.id}")
            }
    ) {
        Column {
            Box(modifier = Modifier.height(140.dp)) {
                // 🔥 THE FIX: Check if we have a Gallery URI or a Local Resource
                if (product.imageUri != null) {
                    coil.compose.AsyncImage(
                        model = product.imageUri,
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Keep your favorite heart button exactly as it is below
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.White, CircleShape)
                        .clickable {
                            FavoritesManager.toggleFavorite(product.id)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isFavorite) Color(0xFFC05D35) else TextTitleDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextTitleDark
                    )
                    Text(
                        text = product.subtitle,
                        fontSize = 10.sp,
                        color = TextLight,
                        maxLines = 1
                    )
                }
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(BgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go",
                        tint = TextTitleDark,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CustomBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: androidx.navigation.NavController,
    isLoggedIn: Boolean,
    onFabClick: () -> Unit,
    currentRoute: String? = "home"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp)
                .height(68.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(50.dp))
                .background(color = Color(0xFFF4EBE1), shape = RoundedCornerShape(percent = 50)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                BottomNavItem(Icons.Default.Home, "Home", currentRoute == "home", onClick = {
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                })

                BottomNavItem(Icons.Default.FavoriteBorder, "Favorites", currentRoute == "favorites", onClick = {
                    navController.navigate("favorites") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                })

                BottomNavItem(Icons.Default.AutoFixHigh, "Create \nStory", false, onClick = onFabClick)

                BottomNavItem(Icons.Default.Groups, "Artisans", currentRoute == "artisanSetup", onClick = {
                    navController.navigate("artisanSetup") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                })

                BottomNavItem(
                    icon = Icons.Default.PersonOutline,
                    label = "Profile",
                    isSelected = currentRoute == "profile" || currentRoute == "auth",
                    onClick = {
                        if (isLoggedIn) {
                            navController.navigate("profile") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate("auth") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tint = if (isSelected) PrimaryAction else TextLight
    val indicatorColor = if (isSelected) Color(0xFFF4EBE1) else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(top = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(32.dp)
                .width(64.dp)
                .clip(CircleShape)
                .background(indicatorColor)
        ) {
            Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(24.dp))
        }

        Text(
            text = label,
            color = tint,
            fontSize = 10.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}

@Composable
fun FloatingMenuCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.15f)
            )
            .background(Color.White, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFFF3EAE0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFFC05D35), modifier = Modifier.size(22.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2B23),
                fontFamily = FontFamily.Serif
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF8A827B),
                lineHeight = 14.sp
            )
        }
    }
}