package com.kumbarakala.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kumbarakala.app.R
import com.kumbarakala.app.model.productList

@Composable
fun ArtisanProfileScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFAF7F2))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // --- 1. TOP BAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButtonCircleProfile(Icons.AutoMirrored.Filled.ArrowBack) { navController.popBackStack() }

                // Center Title
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Artisan Profile",
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF3E2B23),
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(4.dp).background(Color(0xFF8B5A2B), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.FilterVintage, contentDescription = null, tint = Color(0xFF8B5A2B), modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(modifier = Modifier.size(4.dp).background(Color(0xFF8B5A2B), CircleShape))
                    }
                    Text(
                        text = "Meet the hands behind the art",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                }

                // Heart Button
                IconButtonCircleProfile(Icons.Default.FavoriteBorder) {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. HERO CARD ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                // Artisan Image
                Image(
                    painter = painterResource(id = R.drawable.artisan_ramesh), // TODO: Replace with R.drawable.artisan_ramesh
                    contentDescription = "Ramesh Prajapati",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(0.9f)
                        .aspectRatio(0.75f)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Details
                Column(modifier = Modifier.weight(1.1f)) {
                    Text(
                        text = "Ramesh\nPrajapati",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF3E2B23),
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF8B5A2B), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Kutch, Gujarat", fontSize = 13.sp, color = Color(0xFF5A6B47))
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    // Role
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ColorLens, contentDescription = null, tint = Color(0xFFC2613B), modifier = Modifier.size(16.dp)) // Using palette as placeholder for pot
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Clay Artisan", fontSize = 13.sp, color = Color(0xFFC2613B), fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Experience Chip
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF4EBE1), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Eco, contentDescription = null, tint = Color(0xFF5A6B47), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("18+ Years of Experience", fontSize = 11.sp, color = Color(0xFF5A6B47))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quote
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.FormatQuote, contentDescription = null, tint = Color(0xFFC2613B), modifier = Modifier.size(20.dp).offset(x = (-4).dp, y = (-4).dp))
                        Text(
                            text = "I believe in keeping our traditions alive through every piece I create.",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF3E2B23),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. STATS ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(Icons.Default.Inventory2, "350+", "Products Made")
                VerticalDivider()
                StatItem(Icons.Default.StarBorder, "4.9", "Customer Rating")
                VerticalDivider()
                StatItem(Icons.Default.PeopleOutline, "120+", "Stories Shared")
                VerticalDivider()
                StatItem(Icons.Default.Eco, "100%", "Natural & Eco-friendly")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 4. ABOUT SECTION ---
            Text(
                text = "About Ramesh",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2B23)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Rameshji comes from a family of traditional potters from Kutch, Gujarat. For generations, his family has been creating beautiful and functional clay products using age-old techniques.",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "His passion lies in blending traditional craftsmanship with modern needs while staying true to nature and sustainability.",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Decorative Sketch Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4EBE1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FilterVintage, contentDescription = null, tint = Color(0xFFC2613B).copy(alpha = 0.3f), modifier = Modifier.size(50.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 5. HIS CREATIONS ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "His Creations",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E2B23)
                )
                Text(
                    text = "View All >",
                    fontSize = 12.sp,
                    color = Color(0xFF8B5A2B),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontal Scrolling Products
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Taking first 4 products from your existing list
                items(productList.take(4)) { product ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF3E2B23)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 6. MESSAGE BUTTON ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(Color(0xFFC2613B), RoundedCornerShape(24.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Message Artisan", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            }

            // Padding at the bottom so it doesn't get covered by the bottom navigation bar
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
        Icon(icon, contentDescription = null, tint = Color(0xFFC2613B), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3E2B23))
        Text(label, fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center, lineHeight = 12.sp)
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(1.dp)
            .background(Color.LightGray.copy(alpha = 0.5f))
    )
}

@Composable
fun IconButtonCircleProfile(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(Color(0xFFF4EBE1), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF8B5A2B),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}