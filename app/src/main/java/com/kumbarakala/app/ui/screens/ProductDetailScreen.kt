package com.kumbarakala.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kumbarakala.app.model.Product
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.navigation.NavHostController
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import com.kumbarakala.app.R
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import coil.compose.AsyncImage

@Composable
fun ProductDetailScreen(
    product: Product,
    navController: NavHostController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    // We use a Box so the bottom white content area can seamlessly overlap the image
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFAF7F2))) {

        // --- 1. HERO IMAGE & TOP BAR ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.48f)
        ) {

            // 🔥 UPDATED IMAGE BLOCK
            if (product.imageUri != null) {
                // This displays the new product photos you pick from the gallery
                coil.compose.AsyncImage(
                    model = product.imageUri,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // This keeps your original hardcoded products working exactly as they are
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // BACK BUTTON
                Surface(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color(0xFFF5EEE6), // Liquid Glass Beige
                    shadowElevation = 4.dp,    // Professional depth
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF50342E) // Deep Earthy Brown
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    // 🔥 NEW: Delete Button (Only shows for your custom products)
                    if (product.imageUri != null) {
                        Surface(
                            onClick = {
                                // Call the delete function we added to Product.kt
                                com.kumbarakala.app.model.deleteProduct(product.id, context)
                                navController.popBackStack() // Go back home
                            },
                            modifier = Modifier.size(56.dp),
                            shape = CircleShape,
                            color = Color(0xFFF5EEE6),
                            shadowElevation = 4.dp,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = Color.Red.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // 🔥 UPDATED: Your Favorite Button (Now fully functional!)
                    Surface(
                        onClick = { com.kumbarakala.app.model.FavoritesManager.toggleFavorite(product.id) },
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = Color(0xFFF5EEE6),
                        shadowElevation = 4.dp,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        val isFav = com.kumbarakala.app.model.FavoritesManager.isFavorite(product.id)
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFav) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFav) Color(0xFFC05D35) else Color(0xFF50342E)
                            )
                        }
                    }
                }
            }
        }
        // --- 2. THE OVERLAPPING CONTENT SHEET ---
        // This is the main body with the rounded top corners
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.58f) // Overlaps the image slightly to create that layered effect
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color(0xFFFAF7F2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 24.dp)
            ) {
                // --- TITLE & ECO CHIP ROW ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Title and Subtitle
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF3E2B23) // TextTitleDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = product.subtitle,
                            fontSize = 14.sp,
                            color = Color(0xFF5A6B47) // Earthy green/olive
                        )
                    }

                    // Eco-friendly Chip
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFEAE3D2), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa, // Using standard Spa leaf icon
                            contentDescription = null,
                            tint = Color(0xFF5A6B47),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = product.ecoChipText,
                            fontSize = 12.sp,
                            color = Color(0xFF5A6B47),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- RATING & STORIES ROW ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFF2A93B), // Warm star yellow
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.rating,
                        fontSize = 15.sp,
                        color = Color(0xFF3E2B23),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Small vertical divider
                    Box(
                        modifier = Modifier
                            .height(14.dp)
                            .width(1.dp)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "${product.storyCount} Stories",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- DESCRIPTION ---
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = Color(0xFF3E2B23).copy(alpha = 0.8f),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- 4 FEATURE CARDS GRID ---
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Top Row of Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.AcUnit,
                            text = "Keeps Water\nNaturally Cool"
                        )
                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Spa,
                            text = "100% Natural\n& Eco-friendly"
                        )
                    }
                    // Bottom Row of Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.FavoriteBorder,
                            text = "Healthy for\nYou & Family"
                        )
                        FeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.PanTool, // Represents "Handcrafted"
                            text = "Handcrafted\nby Artisans"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- ARTISAN PROFILE BOX ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF4EBE1), RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Artisan Avatar Placeholder
                    Image(
                        painter = painterResource(id = R.drawable.artisan_ramesh), // Ensure this matches your image file name
                        contentDescription = "Artisan Profile",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop // 🔥 CRITICAL: Forces the image to fill the circle beautifully
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Artisan", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            text = "Ramesh Prajapati",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF3E2B23)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.offset(y = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF8B5A2B), // Warm copper
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Kutch, Gujarat",
                                fontSize = 12.sp,
                                color = Color(0xFF8B5A2B)
                            )
                        }
                    }

                    // View Profile Button
                    Row(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { navController.navigate("artisanProfile") }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View Profile",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Serif,
                            color = Color(0xFF8B5A2B),
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF8B5A2B),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(Color(0xFFC2613B), RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .clickable {
                            navController.navigate("cardGenerator/${product.id}")
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generate Story Card",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(16.dp)) // Pushes the button up slightly from the very bottom edge
            }
        }
    }
}

@Composable
fun FeatureCard(modifier: Modifier = Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(
        modifier = modifier
            .background(Color(0xFFF4EBE1), RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF8B5A2B), // Copper/Brown color matching your design
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = Color(0xFF3E2B23),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun IconButtonCircle(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(Color(0xFFF4EBE1), CircleShape) // MATCHED
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF8B5A2B), // MATCHED
            modifier = Modifier.size(20.dp)
        )
    }
}