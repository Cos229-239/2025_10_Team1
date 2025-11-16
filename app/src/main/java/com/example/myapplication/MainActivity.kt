package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                SignInScreen()
            }
        }
    }
}

@Composable
fun SignInScreen() {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // --- Logo ---
        Image(
            painter = painterResource(id = R.drawable.tisense_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- App Name ---
        Text(
            text = "TiSense",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- Subtitle ---
        Text(
            text = "Create an account",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )

        Text(
            text = "Enter your email to sign in or create an account",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // --- Email Input ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Black
            )
        )


        Spacer(modifier = Modifier.height(20.dp))

        // --- Continue Button ---
        Button(
            onClick = { /* Handle Continue */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue", color = Color.White, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Divider with OR ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text("  or  ", color = Color.Gray)
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Sign-in Buttons ---
        SignInButton(
            icon = painterResource(id = R.drawable.ic_google),
            text = "Continue with Google"
        )
        Spacer(modifier = Modifier.height(12.dp))
        SignInButton(
            icon = painterResource(id = R.drawable.ic_apple),
            text = "Continue with Apple"
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- Terms of Service ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By clicking continue, you agree to our",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                textAlign = TextAlign.Center
            )
            Row {
                Text(
                    text = "Terms of Service",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        textDecoration = TextDecoration.Underline
                    )
                )
                Text(" and ")
                Text(
                    text = "Privacy Policy",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Black,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}

@Composable
fun SignInButton(icon: Painter, text: String) {
    OutlinedButton(
        onClick = { /* Handle click */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, color = Color.Black)
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Sign In Screen Preview"
)
@Composable
fun SignInScreenPreview() {
    MyApplicationTheme {
        SignInScreen()
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenInteractivePreview() {
    MyApplicationTheme {
        SignInScreen()
    }
}
