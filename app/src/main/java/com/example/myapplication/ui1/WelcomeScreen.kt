package com.example.myapplication.ui1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.Screen
import kotlinx.coroutines.delay

// The duration the welcome screen will be displayed in milliseconds.
private const val WELCOME_SCREEN_DURATION = 3000L

@Composable
fun WelcomeScreen(navController: NavController) {

    // This effect will run once when the screen is first composed.
    LaunchedEffect(Unit) {
        delay(WELCOME_SCREEN_DURATION)
        // Navigate to the Login screen and remove the Welcome screen from the back stack.
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Welcome.route) {
                inclusive = true
            }
        }
    }

    // The main container with the gradient background from your original image.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Using darker shades for the gradient
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF3A1C71), // Deeper Purple
                        Color(0xFF194242)  // Darker Teal
                    ),
                    start = Offset(Float.POSITIVE_INFINITY, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // This Box holds the logo and text elements, allowing for precise offsets.
        Box(
            modifier = Modifier
                .size(280.dp) // Adjusted parent size for more room
                .offset(x = (-10).dp), // <-- FIX: Shifts the entire group left
            contentAlignment = Alignment.Center
        ) {


            // The logo image, now slightly larger and shifted left for symmetry.
            Image(
                painter = painterResource(id = R.drawable.tisense_icon_2),
                contentDescription = "TiSense Logo",
                modifier = Modifier
                    .size(220.dp) // FIX 1: Made the logo a little bit bigger
                    .offset(x = (-10).dp) // FIX 2: Moved it to the left
            )


            // "ti" Text, with adjusted offset to match the new logo position.
            Text(
                text = "ti",
                color = Color(0xFFBDE8F0), // Light cyan/blue text color
                fontSize = 42.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.offset(x = (-42).dp, y = (-15).dp)
            )


            // "Sense" Text, with adjusted offset.
            Text(
                text = "Sense",
                color = Color(0xFFBDE8F0),
                fontSize = 42.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.offset(x = 55.dp, y = 30.dp) // Adjusted offset
            )

            // "Time is of the essence" Tagline, with adjusted offset.
            Text(
                text = "Time is of the essence",
                color = Color(0xFFBDE8F0).copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.offset(x = 55.dp, y = 62.dp) // Adjusted offset
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController())
}
