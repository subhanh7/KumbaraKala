package com.kumbarakala.app.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kumbarakala.app.model.CardViewModel
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.Facebook
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Print
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import com.kumbarakala.app.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.size
import android.graphics.Bitmap
import android.view.View
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalView
import android.net.Uri
import android.provider.MediaStore
import com.kumbarakala.app.utils.ImageUtils
import androidx.compose.ui.graphics.graphicsLayer
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
val ShareBg = Color(0xFFFAF5EF)
val ShareBrown = Color(0xFF3E2B23)
val ShareOrange = Color(0xFFC05D35)
val ShareSoft = Color(0xFFF4EBE1)
val ShareGreen = Color(0xFF6B7A1F)

@Composable
fun ShareStoryScreen(navController: NavHostController, viewModel: CardViewModel) {
    // 🔥 Add this at the top with your other variables
    var captureMode by remember { mutableStateOf("SAVE") }
    val captureController = rememberCaptureController()
    val context = LocalContext.current
    val cardState = viewModel.currentCardState.value

    if (cardState == null) {
        Box(modifier = Modifier.fillMaxSize().background(ShareBg), contentAlignment = Alignment.Center) {
            Text(text = "No Story Card Found", color = ShareBrown, fontSize = 18.sp)
        }
        return
    }

    // Single Box to hold the background
    Box(modifier = Modifier.fillMaxSize().background(ShareBg)) {
        // Everything MUST stay inside this Column to stack correctly
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            // 1. TOP BAR
            ShareHeader(navController)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. SUCCESS BANNER
            SuccessBanner()

            Spacer(modifier = Modifier.height(24.dp))

            // 3. CARD PREVIEW (Wrapped in Capturable)
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Capturable(
                    controller = captureController,
                    onCaptured = { bitmap, error ->
                        if (bitmap != null) {
                            val androidBitmap = bitmap.asAndroidBitmap()

                            if (captureMode == "SHARE") {
                                // 🔥 ONLY share, don't save to gallery
                                shareToWhatsApp(context, androidBitmap)
                            } else {
                                // 🔥 ONLY save, don't open WhatsApp
                                ImageUtils.saveBitmapToGallery(context, androidBitmap)
                                Toast.makeText(context, "Card Saved to Gallery", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    StoryCardPreview(state = cardState)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 4. ACTION TITLE
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("Share your story", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = ShareBrown)
                Text("Choose an option to save or share your card", fontSize = 13.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 5. MAIN BUTTONS
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                LargeShareButton(
                    title = "Save to Gallery",
                    subtitle = "Save the card to your device",
                    backgroundColor = ShareSoft,
                    iconColor = ShareOrange,
                    textColor = ShareBrown,
                    icon = Icons.Outlined.FileDownload
                ) {
                    captureMode = "SAVE" // 🔥 Set mode to save
                    captureController.capture()
                }

                Spacer(modifier = Modifier.height(16.dp))

                LargeShareButton(
                    title = "Share on WhatsApp",
                    subtitle = "Share directly with your customers",
                    backgroundColor = ShareGreen,
                    iconColor = Color.Transparent,
                    textColor = Color.White,
                    icon = R.drawable.ic_whatsapp
                ) {
                    // 🔥 Trigger the capture process
                    captureMode = "SHARE" // 🔥 Set mode to share
                    captureController.capture()
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 6. MORE OPTIONS (Now properly stacked at the bottom)
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text("More sharing options", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ShareBrown)
                Spacer(modifier = Modifier.height(18.dp))
                MoreOptionsGrid()
            }
        } // End of Column
    } // End of Box
}
@Composable
fun SuccessBanner() {
    val bannerBg = Color(0xFFF3EAE0) // Your HeaderBg color
    val forestGreen = Color(0xFF6B7A1F)
    val titleColor = Color(0xFF3E2B23)
    val subtitleColor = Color(0xFF8A827B)
    val borderColor = Color(0xFFE5DCD3) // Subtle border to define the shape
    Card(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bannerBg),
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Lowered elevation for a flatter, more organic look
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(42.dp).background(forestGreen, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Your story card is ready!", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = ShareBrown, fontFamily = FontFamily.Serif)
                Text("Share it with the world and grow your impact.", fontSize = 12.sp, color = Color.Gray,fontFamily = FontFamily.Serif)
            }
            Text(text = "🎉", fontSize = 32.sp)
        }
    }
}

@Composable
fun ShareHeader(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderIcon(Icons.AutoMirrored.Filled.ArrowBack) { navController.popBackStack() }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Share Your Story Card", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ShareBrown, fontFamily = FontFamily.Serif)
            Text("Your story is ready to inspire", fontSize = 11.sp, color = Color.Gray)
        }
        HeaderIcon(Icons.Default.Home) { navController.navigate("home") }
    }
}

@Composable
fun HeaderIcon(
    icon: ImageVector,
    onClick: () -> Unit
) {
    val glassColor = Color(0xFFF5EEE6) // The exact soft beige from your 2nd image
    val iconTint = Color(0xFF50342E)  // Deep brown for contrast

    Surface(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp), // 🔥 Increased size for a more premium feel
        shape = CircleShape,
        color = glassColor,
        // 🔥 Shadow provides that "Liquid Glass" depth
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp) // Proportional icon size
            )
        }
    }
}

@Composable
fun LargeShareButton(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    iconColor: Color,
    textColor: Color,
    icon: Any, // 🔥 Uses 'Any' to support both Icons and PNGs
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        shadowElevation = 2.dp,
        border = if (backgroundColor == ShareSoft) BorderStroke(1.dp, Color(0xFFE5DCD3)) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔥 Properly handles both Material Icons and Drawable PNGs
            when (icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(44.dp)
                    )
                }
                is Int -> {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = subtitle,
                    color = textColor.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}
@Composable
fun MoreOptionsGrid() {
    val shareItems = listOf(
        ShareOption("Instagram", R.drawable.ic_instagram),
        ShareOption("Facebook", Icons.Outlined.Facebook),      // Professional Facebook icon
        ShareOption("Copy Link", Icons.Outlined.ContentCopy),   // Accurate Copy icon
        ShareOption("Share Via", Icons.Outlined.IosShare),      // Premium Share icon
        ShareOption("Print", Icons.Outlined.Print)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        shareItems.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE5DCD3)) // Earthy border
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // 🔥 This check handles both your PNGs and Material Icons
                        when (val icon = item.icon) {
                            is ImageVector -> {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = item.title,
                                    tint = ShareBrown,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            is Int -> {
                                Image(
                                    painter = painterResource(id = icon),
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .scale(1.3f),
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.title,
                    fontSize = 10.sp,
                    color = ShareBrown,
                    fontFamily = FontFamily.Serif, // Artisan typography
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
data class ShareOption(val title: String, val icon: Any)

fun shareToWhatsApp(context: Context, bitmap: Bitmap) {
    try {
        // 1. Save the bitmap to a temporary URI so it can be shared
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "KumbaraKala_Share",
            null
        )
        val imageUri = Uri.parse(path)

        // 2. Create the Intent to send an image
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            putExtra(Intent.EXTRA_TEXT, "Check out my handcrafted story card!")
            `package` = "com.whatsapp" // Targets standard WhatsApp
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(shareIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error sharing to WhatsApp", Toast.LENGTH_SHORT).show()
    }
}