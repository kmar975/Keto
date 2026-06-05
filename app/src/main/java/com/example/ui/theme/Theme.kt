package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Curated Professional theme mirroring the elegant Design Spec
private val ProfessionalPolishColorScheme =
  lightColorScheme(
    primary = EnergyAmber,
    secondary = KetoneCyan,
    tertiary = BrainPink,
    background = DeepDarkBlue,
    surface = DarkCardBg,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = CleanWhite,
    onSurface = CleanWhite
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false, // Force brand colors for consistent premium rendering
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = ProfessionalPolishColorScheme,
    typography = Typography,
    content = content
  )
}
