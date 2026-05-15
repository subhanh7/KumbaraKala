package com.kumbarakala.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.sp
import com.kumbarakala.app.model.FavoritesManager
import androidx.compose.foundation.border
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    onNavigateToGenerator: (String) -> Unit // Now it expects a String!
){
    // 1. State for Search
    var searchQuery by remember { mutableStateOf("") }

    // 🔥 ADDED: State to control the floating menu
    var showCreateSheet by remember { mutableStateOf(false) }

    // 2. Filter the list to ONLY show favorites AND match the search query
    val favoriteList = com.kumbarakala.app.model.globalProductList.filter { product ->
        FavoritesManager.isFavorite(product.id) && product.name.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize().background(BgColor)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Reuse the beautiful curve header
            item { TopHeaderSection() }

            // Reuse the search bar section
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    TextAndSearchSection(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Saved Items",
                    modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 16.dp),
                    color = TextTitleDark,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Display the cards exactly like the home screen
            items(favoriteList.chunked(2)) { rowProducts ->
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

            // If empty, show a message
            if (favoriteList.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text("No favorites yet. Go like some items!", color = TextLight)
                    }
                }
            }
        }

        // 3. Add the Bottom Navigation Bar
        CustomBottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController,
            isLoggedIn = true,
            // 🔥 FIXED: Instead of navigating immediately, open the menu!
            onFabClick = { showCreateSheet = true },
            currentRoute = "favorites"
        )

        // 🔥 ADDED: The Custom Floating Menu Overlay
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

                    // 🔥 THE TRANSPARENT GLASS BOX OVERLAY
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
                        // Option 1: Select Existing (Now grabs the first favorite)
                        FloatingMenuCard(
                            icon = Icons.Default.List,
                            title = "Select Existing Product",
                            subtitle = "Choose from your current handcrafted collection",
                            onClick = {
                                val fallbackId = favoriteList.firstOrNull()?.id ?: com.kumbarakala.app.model.globalProductList.first().id
                                onNavigateToGenerator(fallbackId) // 🔥 Navigate to Generator
                                showCreateSheet = false           // 🔥 Close Menu
                            }
                        )

                        // Option 2: Add New
                        FloatingMenuCard(
                            icon = Icons.Default.AddCircle,
                            title = "Add New Product",
                            subtitle = "Add a new handcrafted item and generate a story",
                            onClick = {
                                navController.navigate("addProduct") // 🔥 Navigate to Add
                                showCreateSheet = false              // 🔥 Close Menu
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