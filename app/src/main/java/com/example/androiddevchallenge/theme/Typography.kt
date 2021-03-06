package com.example.androiddevchallenge.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R

val fontFamilies = FontFamily(
    fonts = listOf(
        Font(R.font.fredoka_one_regular)
    )
)

val typography = Typography(
    h3 = TextStyle(
        fontFamily = fontFamilies,
        fontWeight = FontWeight.Normal,
        fontSize = 64.sp
    ),
    h4 = TextStyle(
        fontFamily = fontFamilies,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp
    )
)