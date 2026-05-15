package com.kumbarakala.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kumbarakala.app.R
import androidx.compose.ui.platform.LocalContext
// --- THEME COLORS ---
private val bgColor = Color(0xFFFAF5EF)
private val headerBg = Color(0xFFF3EAE0)
private val textTitleDark = Color(0xFF3E2B23)
private val primaryAction = Color(0xFFC05D35)
private val textLight = Color(0xFF8A827B)
private val fieldBg = Color(0xFFFAF7F2)

enum class AuthUiState { WELCOME, LOGIN, SIGNUP, FORGOT_PASSWORD, SUCCESS_EMAIL }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit,
    onGuestClick: () -> Unit
) {
    var currentScreen by rememberSaveable { mutableStateOf(AuthUiState.WELCOME) }
    val authResult by viewModel.authState.collectAsState()

    LaunchedEffect(authResult) {
        if (authResult is AuthState.Success) {
            viewModel.resetState()
            onAuthSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                val slideDirection = if (targetState.ordinal > initialState.ordinal) {
                    AnimatedContentTransitionScope.SlideDirection.Left
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }
                (slideIntoContainer(slideDirection, tween(400)) + fadeIn(tween(400))) togetherWith
                        (slideOutOfContainer(slideDirection, tween(400)) + fadeOut(tween(400)))
            },
            label = "AuthAnimation"
        ) { targetState ->
            when (targetState) {
                AuthUiState.WELCOME -> WelcomeState(
                    onLoginClick = { currentScreen = AuthUiState.LOGIN },
                    onSignupClick = { currentScreen = AuthUiState.SIGNUP },
                    onGuestClick = onGuestClick
                )
                AuthUiState.LOGIN -> LoginState(
                    viewModel = viewModel, authResult = authResult,
                    onBackClick = { currentScreen = AuthUiState.WELCOME },
                    onForgotClick = { currentScreen = AuthUiState.FORGOT_PASSWORD },
                    onSignupClick = { currentScreen = AuthUiState.SIGNUP },
                    onAuthSuccess = onAuthSuccess
                )
                AuthUiState.SIGNUP -> SignupState(
                    viewModel = viewModel, authResult = authResult,
                    onBackClick = { currentScreen = AuthUiState.WELCOME },
                    onLoginClick = { currentScreen = AuthUiState.LOGIN }
                )
                AuthUiState.FORGOT_PASSWORD -> ForgotPasswordState(
                    viewModel = viewModel, authResult = authResult,
                    onBackClick = { currentScreen = AuthUiState.LOGIN },
                    onSendSuccess = { currentScreen = AuthUiState.SUCCESS_EMAIL }
                )
                AuthUiState.SUCCESS_EMAIL -> SuccessState(
                    onBackToLoginClick = { currentScreen = AuthUiState.LOGIN }
                )
            }
        }
    }
}

@Composable
fun AuthHeaderLayout(
    showBackButton: Boolean = true,
    showLargePot: Boolean = false,
    showBottomImg: Boolean = false,
    onBackClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(bgColor)) {

        if (showBottomImg) {
            Image(
                painter = painterResource(id = R.drawable.auth_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(260.dp)
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // --- HEADER SECTION ---
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) { // Tight height to pull content up

                Canvas(modifier = Modifier.fillMaxWidth().height(190.dp)) {
                    val w = size.width
                    val h = size.height
                    val mainPath = Path().apply {
                        moveTo(0f, h * 0.60f)
                        cubicTo(w * 0.35f, h * 0.45f, w * 0.70f, h * 1.10f, w, h * 0.85f)
                        lineTo(w, 0f); lineTo(0f, 0f); close()
                    }
                    drawPath(mainPath, color = headerBg)
                }
                // --- THE BACK BUTTON (MATCHING 2ND IMAGE) ---
                if (showBackButton) {
                    Surface(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(top = 56.dp, start = 24.dp)
                            .size(56.dp),
                        shape = CircleShape,
                        color = Color(0xFFF5EEE6), // Exact soft beige from sample
                        shadowElevation = 4.dp     // Increased elevation for that circular shadow
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color(0xFF3E2B23), // Exact Deep Brown
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // --- LIFTED FORM CONTENT ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-45).dp), // Lifts "Login" and card up into the header space
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                if (showBottomImg) Spacer(modifier = Modifier.height(220.dp))
                else Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
@Composable
private fun WelcomeState(onLoginClick: () -> Unit, onSignupClick: () -> Unit, onGuestClick: () -> Unit) {
    AuthHeaderLayout(showBackButton = false, showLargePot = true, showBottomImg = true) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Kumbara-Kala", fontSize = 32.sp, fontFamily = FontFamily.Serif, color = TextTitleDark, fontWeight = FontWeight.Bold)
            HeaderOrnament()
            Spacer(modifier = Modifier.height(14.dp))
            Text("Stories shaped by hands.\nTreasures for generations.", textAlign = TextAlign.Center, fontSize = 15.sp, color = textLight, fontFamily = FontFamily.Serif, lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(48.dp))
            AuthActionButton("Login", onClick = onLoginClick)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedActionBox("Create Account", onClick = onSignupClick)
            Spacer(modifier = Modifier.height(32.dp))
            Text("Continue as Guest", color = primaryAction, fontWeight = FontWeight.Bold, fontSize = 15.sp, fontFamily = FontFamily.Serif, modifier = Modifier.clickable { onGuestClick() })
        }
    }
}

@Composable
private fun LoginState(
    viewModel: AuthViewModel,
    authResult: AuthState,
    onBackClick: () -> Unit,
    onForgotClick: () -> Unit,
    onSignupClick: () -> Unit,
    onAuthSuccess: () -> Unit // 🔥 Add this parameter
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // 🔥 SUCCESS TRIGGER: Move to home when logged in
    LaunchedEffect(authResult) {
        if (authResult is AuthState.Success) {
            onAuthSuccess()
        }
    }

    AuthHeaderLayout(showBackButton = true, showLargePot = false, showBottomImg = false, onBackClick = onBackClick) {
        Text("Login", fontSize = 28.sp, fontFamily = FontFamily.Serif, color = textTitleDark, fontWeight = FontWeight.Bold)
        HeaderOrnament()

        Spacer(modifier = Modifier.height(14.dp))

        Text("Welcome back! Please login\nto continue.", textAlign = TextAlign.Center, fontSize = 14.sp, color = textLight, fontFamily = FontFamily.Serif)

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(24.dp)).padding(20.dp)) {
            AuthCustomInputField("Email", email, "Enter your email", Icons.Outlined.Email) { email = it }
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomInputField("Password", password, "Enter your password", Icons.Outlined.Lock, isPassword = true, passwordVisible = passwordVisible, onVisibilityToggle = { passwordVisible = !passwordVisible }) { password = it }

            Text("Forgot Password?", color = primaryAction, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.align(Alignment.End).padding(top = 16.dp).clickable { onForgotClick() })
        }

        // 🔥 ERROR DISPLAY: This shows the "Account not found / Sign up first" message
        if (authResult is AuthState.Error) {
            Text(
                text = authResult.message,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AuthActionButton(
            "Login",
            isLoading = authResult is AuthState.Loading,
            onClick = { viewModel.login(email.trim(), password) } // Use .trim() for real apps
        )

        SocialLoginSection()

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Don't have an account? ", color = textLight, fontSize = 14.sp)
            Text("Sign Up", color = primaryAction, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.clickable { onSignupClick() })
        }
    }
}

@Composable
private fun SignupState(
    viewModel: AuthViewModel,
    authResult: AuthState,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current // Get context for Toast
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // 🔥 Trigger success Toast
    LaunchedEffect(authResult) {
        if (authResult is AuthState.Success) {
            android.widget.Toast.makeText(
                context,
                "Account created successfully",
                android.widget.Toast.LENGTH_SHORT
            ).show()
            onLoginClick()
        }
    }

    AuthHeaderLayout(showBackButton = true, showLargePot = false, showBottomImg = false, onBackClick = onBackClick) {
        Text("Create Account", fontSize = 28.sp, fontFamily = FontFamily.Serif, color = textTitleDark, fontWeight = FontWeight.Bold)
        HeaderOrnament()
        Spacer(modifier = Modifier.height(14.dp))
        Text("Join the Kumbara-Kala family.", fontSize = 14.sp, color = textLight, fontFamily = FontFamily.Serif)

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(24.dp)).padding(20.dp)) {
            AuthCustomInputField("Full Name", name, "Enter your full name", Icons.Outlined.PersonOutline) { name = it }
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomInputField("Email", email, "Enter your email", Icons.Outlined.Email) { email = it }
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomInputField("Password", password, "Create a password", Icons.Outlined.Lock, isPassword = true, passwordVisible = passwordVisible, onVisibilityToggle = { passwordVisible = !passwordVisible }) { password = it }
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomInputField("Confirm Password", confirmPassword, "Confirm password", Icons.Outlined.Lock, isPassword = true, passwordVisible = passwordVisible, onVisibilityToggle = { passwordVisible = !passwordVisible }) { confirmPassword = it }
        }

        if (authResult is AuthState.Error) {
            Text(authResult.message, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Button trigger
        AuthActionButton(
            text = "Create Account",
            isLoading = authResult is AuthState.Loading,
            onClick = {
                if (password == confirmPassword) {
                    viewModel.signUp(email, password)
                } else {
                    android.widget.Toast.makeText(context, "Passwords do not match", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Already have an account? ", color = textLight, fontSize = 14.sp)
            Text("Login", color = primaryAction, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.clickable { onLoginClick() })
        }
    }
}

@Composable
private fun ForgotPasswordState(viewModel: AuthViewModel, authResult: AuthState, onBackClick: () -> Unit, onSendSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    LaunchedEffect(authResult) { if (authResult is AuthState.PasswordResetSent) onSendSuccess() }
    AuthHeaderLayout(showBackButton = true, showBottomImg = false, onBackClick = onBackClick) {
        Text("Forgot Password?", fontSize = 28.sp, fontFamily = FontFamily.Serif, color = textTitleDark, fontWeight = FontWeight.Bold)
        HeaderOrnament()
        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(24.dp)).padding(20.dp)) {
            AuthCustomInputField("Email Address", email, "Enter your email", Icons.Outlined.Email) { email = it }
        }
        Spacer(modifier = Modifier.height(24.dp))
        AuthActionButton("Send Reset Link", isLoading = authResult is AuthState.Loading, onClick = { viewModel.resetPassword(email) })
    }
}

@Composable
private fun SuccessState(onBackToLoginClick: () -> Unit) {
    AuthHeaderLayout(showBackButton = false, showBottomImg = false) {
        Text("Check Your Email", fontSize = 28.sp, fontFamily = FontFamily.Serif, color = textTitleDark, fontWeight = FontWeight.Bold)
        HeaderOrnament()
        Spacer(modifier = Modifier.height(32.dp))
        AuthActionButton("Back to Login", onClick = onBackToLoginClick)
    }
}

// --- HELPER COMPONENTS ---

@Composable
fun SocialLoginSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 16.dp)) {
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
            Text("  or continue with  ", fontSize = 12.sp, color = textLight)
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
        }
        SocialButton(text = "Google", iconRes = R.drawable.ic_google)
        Spacer(modifier = Modifier.height(12.dp))
        SocialButton(text = "Apple", iconRes = R.drawable.ic_apple)
    }
}

@Composable
fun SocialButton(text: String, iconRes: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().height(52.dp).border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).clickable { },
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textTitleDark)
    }
}

@Composable
fun HeaderOrnament() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier.size(4.dp).background(primaryAction, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.Default.FilterVintage, null, tint = primaryAction, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier.size(4.dp).background(primaryAction, CircleShape))
    }
}

@Composable
fun AuthActionButton(text: String, isLoading: Boolean = false, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(54.dp).background(primaryAction, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).clickable(enabled = !isLoading) { onClick() }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
        else Text(text, color = Color.White, fontSize = 16.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun OutlinedActionBox(text: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(54.dp).border(1.dp, primaryAction, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).clickable { onClick() }, horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Text(text, color = primaryAction, fontSize = 16.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AuthCustomInputField(label: String, value: String, placeholder: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isPassword: Boolean = false, passwordVisible: Boolean = false, onVisibilityToggle: () -> Unit = {}, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 13.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = textTitleDark)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth().height(52.dp).background(fieldBg, RoundedCornerShape(18.dp)).border(1.dp, headerBg, RoundedCornerShape(18.dp)).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = primaryAction, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) Text(placeholder, color = Color.Gray.copy(alpha = 0.8f), fontSize = 14.sp)
                BasicTextField(value = value, onValueChange = onValueChange, textStyle = TextStyle(fontSize = 14.sp, color = textTitleDark), modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None, keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email))
            }
            if (isPassword) IconButton(onClick = onVisibilityToggle, modifier = Modifier.size(24.dp)) { Icon(if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, null, tint = primaryAction) }
        }
    }
}