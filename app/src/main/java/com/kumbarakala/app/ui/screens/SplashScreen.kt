package com.kumbarakala.app.ui.screens

// 🔥 NEW IMPORTS FOR ANIMATION
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kumbarakala.app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {

    // 🔥 1. Set up the fade state (starts invisible at 0f, fades to solid 1f)
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800), // Takes 800ms to fade in
        label = "fade_in"
    )

    // 🔥 2. Trigger the animation and wait to navigate
    LaunchedEffect(Unit) {
        startAnimation = true // Start the fade immediately
        delay(2000)           // Wait your standard 2 seconds
        onNavigateToHome()    // Go to home screen
    }

    // 🔥 3. Apply the 'alphaAnim' to your main Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alphaAnim) // This makes everything inside fade in smoothly
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_clay),
            contentDescription = "Splash Screen",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}