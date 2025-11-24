package com.example.myapplication.ui1

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.Screen

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailSubmitted by remember { mutableStateOf(false) }

    val standardGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5E3F89), Color(0xFF2C7A7A))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(standardGradient) // FIX: Applied standard gradient
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // --- Logo and Title ---
        Text(
            text = "TiSense",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White // FIX: Text color changed for contrast
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF8A2BE2), Color(0xFF008080))
                    )
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.tisense_icon_2),
                contentDescription = "TiSense Logo",
                modifier = Modifier.size(150.dp)
            )
        }
        Spacer(modifier = Modifier.height(48.dp))

        // --- Header Text ---
        Text(
            text = if (!isEmailSubmitted) "Create an account" else "Enter your password",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White // FIX: Text color changed for contrast
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (!isEmailSubmitted) "Enter your email to sign in or create an account" else email,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f), // FIX: Text color changed for contrast
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // --- Conditional UI ---
        if (!isEmailSubmitted) {
            // --- Email Input ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("email@domain.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White, // FIX: Text color
                    unfocusedTextColor = Color.White, // FIX: Text color
                )
            )
        } else {
            // --- Password Input ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { isEmailSubmitted = false }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White, // FIX: Text color
                        unfocusedTextColor = Color.White, // FIX: Text color
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Continue / Sign In Button ---
        Button(
            onClick = {
                if (!isEmailSubmitted) {
                    isEmailSubmitted = true
                } else {
                    navController.navigate(Screen.Home.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                text = if (!isEmailSubmitted) "Continue" else "Sign In",
                color = Color.Black, // FIX: Text color for contrast
                fontSize = 16.sp
            )
        }

        // --- Social/Guest options only shown on email screen ---
        if (!isEmailSubmitted) {
            // --- "OR" Divider ---
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.5f))
                Text(
                    text = "OR",
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.5f))
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- Social Login Buttons ---
            SocialLoginButton(
                iconId = R.drawable.ic_google,
                text = "Sign in with Google",
                onClick = {  }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SocialLoginButton(
                iconId = R.drawable.ic_apple,
                text = "Sign in with Apple",
                onClick = {  }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Guest Option ---
        if (!isEmailSubmitted) {
            ClickableText(
                text = AnnotatedString("Continue as guest"),
                onClick = { offset ->
                    navController.navigate(Screen.Home.route)
                },
                style = TextStyle(
                    color = Color.White.copy(alpha = 0.8f), // FIX: Text color for contrast
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- Legal Text ---
        LegalText()
        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * A reusable button for social media sign-in (Google, Apple, etc.).
 */
@Composable
private fun SocialLoginButton(iconId: Int, text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White // FIX: Text/Icon color
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = SolidColor(Color.White.copy(alpha = 0.7f))
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier.size(24.dp), // FIX: Adjusted icon size
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 16.sp, color = Color.White) // FIX: Text color
        }
    }
}

/**
 * The legal disclaimer text at the bottom of the screen.
 */
@Composable
private fun LegalText() {
    Text(
        text = buildAnnotatedString {
            append("By clicking continue, you agree to our ")
            pushStyle(style = SpanStyle(fontWeight = FontWeight.Bold))
            append("Terms of Service")
            pop()
            append(" and ")
            pushStyle(style = SpanStyle(fontWeight = FontWeight.Bold))
            append("Privacy Policy")
            pop()
        },
        color = Color.White.copy(alpha = 0.6f), // FIX: Text color for contrast
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        lineHeight = 16.sp
    )
}
