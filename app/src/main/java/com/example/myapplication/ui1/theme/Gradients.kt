package com.example.myapplication.ui1.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Defines the gradient brushes used throughout the application for screen backgrounds.
 * The gradients use a calming, cohesive color palette.
 */
object AppGradients {

    /**
     * A warm, gentle sunrise gradient for the Home screen. Welcoming and positive.
     */
    val home: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFF3E0), // Light Orange
            Color(0xFFFFFFFF)  // White
        )
    )

    /**
     * A focused, serene purple gradient for the Planner screen. Aids concentration.
     */
    val planner: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF3E5F5), // Light Purple
            Color(0xFFFFFFFF)  // White
        )
    )

    /**
     * A calming blue-green gradient for the Breakroom. Promotes relaxation.
     */
    val breakroom: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA), // Lighter Cyan
            Color(0xFFFFFFFF)  // White
        )
    )

    /**
     * A gentle lavender and thistle gradient for the Stretch screens. Soothing and calm.
     */
    val stretch: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF1E6FF), // A very light lavender
            Color(0xFFE6E6FA)  // Lavender
        )
    )

    /**
     * A soft, airy blue gradient for the Breathing Exercise screen. Light and peaceful.
     */
    val breathing: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE3F2FD), // Light Blue
            Color(0xFFFFFFFF)  // White
        )
    )

    /**
     * A professional and clean blue-grey gradient for the Insights screen.
     */
    val insights: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFECEFF1), // Blue Grey
            Color(0xFFFAFAFA)  // Off-white
        )
    )

    /**
     * A sophisticated dark gradient for the Music screen. Creates a focused, moody atmosphere.
     */
    val music: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2C3E50), // Dark Slate Blue
            Color(0xFF000000)  // Black
        )
    )

    /**
     * A neutral, clean gradient for settings or account pages.
     */
    val neutral: Brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF5F5F5), // Very light grey
            Color(0xFFFFFFFF)  // White
        )
    )
}
