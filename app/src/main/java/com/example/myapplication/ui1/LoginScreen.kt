package com.example.myapplication.ui1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.Screen

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))


        Text(
            text = "TiSense",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(

            painter = painterResource(id = R.drawable.tisense_icon_2),
            contentDescription = "TiSense Logo",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(92.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF8A63D2), Color(0xFFC8AFFF))
                    )
                )
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))

        // --- Header Text ---
        Text(
            text = "Create an account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your email to sign in or create an account",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

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
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Continue Button ---
        Button(
            onClick = { navController.navigate(Screen.Home.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Continue", color = Color.White, fontSize = 16.sp)
        }

        // The "OR" divider and social login buttons have been removed.
        // The LegalText is pushed to the bottom.
        Spacer(modifier = Modifier.weight(1f))

        // --- Legal Text ---
        LegalText()
        Spacer(modifier = Modifier.height(24.dp))
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
        color = Color.Gray,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        lineHeight = 16.sp
    )
}

// The Preview has been removed as it depended on the non-existent MyApplicationTheme.
