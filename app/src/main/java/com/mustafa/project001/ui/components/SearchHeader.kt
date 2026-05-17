package com.mustafa.project001.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchHeader(
    textValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface (
        color = Color.White,
        elevation = 4.dp, // Adds a subtle shadow
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp) // Rounded bottom corners
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Row 1: Back Button and Page Title
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Sign Details",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }

            // Row 2: The Search Field
            OutlinedTextField(
                value = textValue,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
                placeholder = { Text("Search word...", color = Color.Gray) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp), // Pill-shaped search bar
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color(0xFFF7F7F7), // Soft background color
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = Color.Transparent // Hide border when not active for a cleaner look
                ),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                },
                trailingIcon = {
                    if (textValue.text.isNotEmpty()) {
                        IconButton(onClick = { onValueChange(TextFieldValue("")) }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                        }
                    }
                }
            )
        }
    }
}
