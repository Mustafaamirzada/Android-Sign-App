package com.mustafa.project001.ui.screens

import ads_mobile_sdk.h6
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.mustafa.project001.R
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleWordSign(
    navController: NavHostController,
    word: String
) {
    val context = LocalContext.current

    // Decode the word (in case it was URL encoded)
    val decodedWord = remember(word) {
        try {
            URLDecoder.decode(word, "UTF-8")
        } catch (e: Exception) {
            word
        }
    }

    // Background Gradient
    val mainGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF8F9FA), Color(0xFFE9ECEF))
    )

    fun getVideoUri(resId: Int): String = "android.resource://${context.packageName}/$resId"

    // Your dictionaries
    val videoMap = mapOf(
        "کتاب" to R.raw.book,
        "سیب" to R.raw.apple,
        "برادر" to R.raw.brother
    )

    val imageMap = mapOf(
        "کتاب" to R.drawable.m,
        "سیب" to R.drawable.hand,
        "برادر" to R.drawable.garuda
    )

    val videoRes = videoMap[decodedWord]
    val imageRes = imageMap[decodedWord]

    var isLooping by remember { mutableStateOf(true) }

    // Main Content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainGradient)
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 16.dp),

//            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp) // Rounded bottom corners


        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("search") }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Sign Details",
                    fontWeight = FontWeight.Bold
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 16.dp)
                    .clickable { navController.navigate("search") },
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween  // This pushes items to ends
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = word,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }
            }
        }


        // --- MAIN CONTENT ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top=40.dp)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- WORD HEADER SECTION ---
            Card(
                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image/Icon
                    Surface(
                        modifier = Modifier.size(70.dp),
                        shape = CircleShape,
                        color = Color(0xFFF1F3F5)
                    ) {
                        if (imageRes != null) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = decodedWord,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Default icon when no image available
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = decodedWord.firstOrNull()?.toString() ?: "?",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6C757D)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        Text(
                            text = "Word",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = decodedWord,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF212529)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- VIDEO SECTION ---
            Text(
                text = "📹 Visual Sign Representation",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 12.dp, start = 4.dp),
                style = MaterialTheme.typography.titleMedium,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (videoRes != null) {
                        val videoPath = getVideoUri(videoRes)

                        AndroidView(
                            factory = { ctx ->
                                VideoView(ctx).apply {
                                    setVideoPath(videoPath)
                                    val mediaController = MediaController(ctx)
                                    mediaController.setAnchorView(this)
                                    setMediaController(mediaController)

                                    setOnPreparedListener { mp ->
                                        mp.isLooping = isLooping
                                        start()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(28.dp))
                        )

                        // Loop toggle button
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .clickable { isLooping = !isLooping },
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = if (isLooping) "🔁" else "⏹️",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        // No video available
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎬",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No sign video available for '$decodedWord'",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- ADDITIONAL INFO SECTION (Optional) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "💡 Did you know?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF2C3E50)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sign language is a complete, natural language that has the same linguistic properties as spoken languages.",
                        fontSize = 14.sp,
                        color = Color(0xFF6C757D),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}