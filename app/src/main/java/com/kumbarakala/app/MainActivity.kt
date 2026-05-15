package com.kumbarakala.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Corrected Imports
import com.kumbarakala.app.model.productList
import com.kumbarakala.app.ui.screens.KumbaraKalaScreen
import com.kumbarakala.app.ui.screens.SplashScreen
import com.kumbarakala.app.ui.theme.KumbaraKalaTheme
import com.kumbarakala.app.ui.screens.ProductDetailScreen
import com.kumbarakala.app.ui.screens.ArtisanProfileScreen
import com.kumbarakala.app.ui.screens.ArtisanSetupScreen
import com.kumbarakala.app.ui.screens.CardGeneratorScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kumbarakala.app.model.CardViewModel
import com.kumbarakala.app.ui.screens.ShareStoryScreen
import com.kumbarakala.app.ui.screens.FavoritesScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kumbarakala.app.ui.screens.AuthScreen
import com.kumbarakala.app.ui.screens.AuthViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import com.kumbarakala.app.ui.screens.ProfileScreen
import io.github.jan.supabase.gotrue.auth
import com.kumbarakala.app.data.supabase
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.kumbarakala.app.ui.screens.AddProductScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.kumbarakala.app.model.loadProductsFromDisk(this)
        setContent {
            KumbaraKalaTheme {
                // 🔥 We add this solid Surface to act as the "floor"
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFAF5EF) // Your Kumbara-Kala beige
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cardViewModel: CardViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = "splash" // Always start at splash
    ) {
        composable(route = "splash") {
            // 🔥 1. Observe the live authentication status from Supabase
            val sessionStatus by supabase.auth.sessionStatus.collectAsState()

            // Keep track of when your SplashScreen UI/timer actually finishes
            var splashFinished by remember { mutableStateOf(false) }

            SplashScreen(
                onNavigateToHome = {
                    splashFinished = true // Mark splash as done, trigger the evaluation
                }
            )

            // 🔥 2. React Safely: Wait for BOTH the splash to finish AND Supabase to stop loading
            LaunchedEffect(splashFinished, sessionStatus) {
                if (splashFinished) {
                    when (sessionStatus) {
                        is io.github.jan.supabase.gotrue.SessionStatus.LoadingFromStorage -> {
                            // Do nothing yet! Supabase is still fetching the token from disk.
                        }
                        is io.github.jan.supabase.gotrue.SessionStatus.Authenticated -> {
                            // Token found and valid! Go straight to Home.
                            navController.navigate("home") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                        else -> {
                            // NotAuthenticated or network issue. Go to Auth.
                            navController.navigate("auth") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
        // 2. Home Gallery Screen
        composable(route = "home") {
            KumbaraKalaScreen(
                navController = navController, // 🔥 ADD THIS LINE!
                onNavigateToGenerator = {
                    navController.navigate("cardGenerator/1")
                }
            )
        }

        /// 3. Product Detail Screen (with ID argument)
        composable(
            route = "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")

            // 🔥 FIX: Search in globalProductList so it finds the items you added via the app!
            val product = com.kumbarakala.app.model.globalProductList.find { it.id == productId }

            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    navController = navController
                )
            } else {
                // Optional: helps you see if an ID failed to match
                androidx.compose.material3.Text("Product not found")
            }
        }
        composable("artisanProfile") {
            // Make sure to import this at the top!
            ArtisanProfileScreen(navController = navController)
        }
        // Add this inside your NavHost, below artisanProfile
        composable("artisanSetup") {
            ArtisanSetupScreen(navController = navController)
        }
        // 4. Card Generator Screen
        composable("cardGenerator/{productId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")

            // 🔥 Search in the global list so it finds your new products
            val product = com.kumbarakala.app.model.globalProductList.find { it.id == id }
                ?: com.kumbarakala.app.model.globalProductList.first()

            if (product != null) {
                // Pass the actual product object to your screen
                CardGeneratorScreen(navController, product, cardViewModel)
            }
        }
        composable("shareScreen") {
            // 🔥 3. Pass the same 'sharedViewModel' here
            ShareStoryScreen(navController, cardViewModel)
        }
        composable("favorites") {
            FavoritesScreen(
                navController = navController,
                // 🔥 Now it takes the actual ID from the screen and passes it along!
                onNavigateToGenerator = { productId ->
                    navController.navigate("cardGenerator/$productId")
                }
            )
        }
        composable(route = "auth") {
            val authViewModel: AuthViewModel = viewModel()
            AuthScreen(
                viewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate("home") // Currently goes home after login
                },
                onGuestClick = {
                    navController.navigate("home")
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                navController = navController
            )
        }
        composable("addProduct") {
            AddProductScreen(navController = navController)
        }
    }
}