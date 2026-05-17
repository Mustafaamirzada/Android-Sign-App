package com.mustafa.project001.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun DictionaryScreen(
    navController: NavHostController
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var hasNavigated by remember { mutableStateOf(false) }

    // Define a nice gradient for a modern look
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF7A28F3), // Primary Dark
            Color(0xFF9D78CB)  // Light Purple
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Lexicon",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Search thousands of words instantly",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp) // Standard text field height is usually 44-56dp
                    .clip(RoundedCornerShape(8.dp)) // Clips the ripple effect to the corners
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp) // The "radius"
                    )
                    .background(Color.White)
                    .clickable {
                        navController.navigate("search")
                    }
                    .padding(horizontal = 12.dp), // Inner spacing for the text
                contentAlignment = Alignment.CenterStart // Aligns text like a real input
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Tap to search...",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 30.dp)
                )
            }


        }
    }
}
