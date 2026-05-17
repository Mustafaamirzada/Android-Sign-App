package com.mustafa.project001.ui.screens


import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.mustafa.project001.R
import com.mustafa.project001.utils.PersianTextProcessor
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun ProductionScreen() {
    // Gradient Background for the whole screen
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFFF0F2F5), Color(0xFFFFFFFF))
            )
        )
    ) {
        SignLanguageApp()
    }
}

@Composable
fun SignLanguageApp() {
    val context = LocalContext.current

    fun getVideoUri(resId: Int): String = "android.resource://${context.packageName}/$resId"

    val videoMap = mapOf(
        "احترام" to getVideoUri(R.raw.a),
        "چطور" to getVideoUri(R.raw.b),
        "خوشامدید" to getVideoUri(R.raw.c),
        "کتاب" to getVideoUri(R.raw.book),
        "تبریک" to getVideoUri(R.raw.congratulation),
        "گوش" to getVideoUri(R.raw.ear),
        "انگشتان" to getVideoUri(R.raw.finger),
        "صمیمی" to getVideoUri(R.raw.frind),
        "سیب" to getVideoUri(R.raw.apple),
        "بایسیکل" to getVideoUri(R.raw.bicycle),
        "تخته" to getVideoUri(R.raw.board),
        "برادر" to getVideoUri(R.raw.brother),
        "موتور" to getVideoUri(R.raw.car),
        "دروازه" to getVideoUri(R.raw.door),
        "خوردن" to getVideoUri(R.raw.eat),
        "پدر" to getVideoUri(R.raw.father),
        "انگور" to getVideoUri(R.raw.grape),
        "دست" to getVideoUri(R.raw.hand),
        "مادر" to getVideoUri(R.raw.mother),
        "قلم" to getVideoUri(R.raw.pen),
        "خواهر" to getVideoUri(R.raw.sester),
        "ساعت" to getVideoUri(R.raw.watch),
        "انار" to getVideoUri(R.raw.ponegranate),
        "بوت" to getVideoUri(R.raw.shose),
        "سپورت" to getVideoUri(R.raw.sport),
        "درخت" to getVideoUri(R.raw.tree)
    )

//    SearchScreen(videoMap = videoMap)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenn(videoMap: Map<String, String>) {
    var text by remember { mutableStateOf("") }
    var videoQueue by remember { mutableStateOf(listOf<String>()) }
    var currentIndex by remember { mutableStateOf(0) }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val results = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            text = results?.getOrNull(0) ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Title Section
        Text(
            text = "SignFlow",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp
            )
        )
        Text(
            text = "Translate speech to sign language",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(        horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(3.dp)) {

            // Modern Search TextField
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Search or Speak...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-AF")
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        }
                        speechLauncher.launch(intent)
                    }) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val processedWords = PersianTextProcessor.processText(text)

//                        val words = text.trim().split(" ")
                        videoQueue = processedWords.mapNotNull { word ->
                            videoMap[word]
                        }
                        currentIndex = 0
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Translate", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))


        // Video Player Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                if (videoQueue.isNotEmpty() && currentIndex < videoQueue.size) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setVideoPath(videoQueue[currentIndex])
                                setOnCompletionListener {
                                    if (currentIndex < videoQueue.size - 1) {
                                        currentIndex++
                                        setVideoPath(videoQueue[currentIndex])
                                        start()
                                    }
                                }
                                start()
                            }
                        },
                        update = { view ->
                            view.setVideoPath(videoQueue[currentIndex])
                            view.start()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        "Video Preview",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))






        // Grid Title
        Text(
            text = "Vocabulary Library",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        val filteredVideos =
            videoMap.filter { it.key.contains(text, ignoreCase = true) }


        if (filteredVideos.isNotEmpty()) {
            Text(
                text = "Search Results",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Box(modifier = Modifier.height(300.dp)) {
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    filteredVideos.forEach { (word, uri) ->
                        item {
//                            WordVideoCard(
//                                word = word,
//                                videoUri = uri,
//                                onClick = {
//                                    videoQueue = listOf(uri)
//                                    currentIndex = 0
//                                    text = word // Optional: Fill the input with the exact word clicked
//                                }
//                            )
                        }
                    }
                }
            }
        } else if (text.isNotBlank()) {
            // Show a message if they typed something but no video matches
            Text(
                "No matching video found for \"$text\"",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }


    }
}


@Composable
fun WordVideoCardn(word: String, videoUri: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            // Add this import: import androidx.compose.foundation.clickable
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoPath(videoUri)
                        // This prevents the mini-videos from playing sound all at once
                        setOnPreparedListener { it.setVolume(0f, 0f) }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp).align(Alignment.Center)
            )
        }
        Text(
            text = word,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 1
        )
    }
}



object PersianTextProcessorn {
    // List of common Persian stop words
    private val stopWords = setOf(
        "از", "به", "در", "را", "با", "که", "این", "آن", "برای", "و", "اما",
        "ولی", "اگر", "چون", "تا", "هر", "بود", "است", "شد", "نیز", "هم"
    )

    // Simple Stemming: Removes common suffixes
    private val suffixes = listOf("تر", "ترین", "‌ها", "ها", "می‌", "انه", "یت" ,"ان")

    fun processText(input: String): List<String> {
        return input.trim()
            .split("\\s+".toRegex()) // Tokenization
            .map { it.lowercase() }
            .filter { it !in stopWords } // Stop word removal
            .map { stem(it) } // Stemming
    }

    private fun stem(word: String): String {
        var currentWord = word
        // Simple suffix stripping logic
        suffixes.forEach { suffix ->
            if (currentWord.endsWith(suffix) && currentWord.length > suffix.length) {
                currentWord = currentWord.substring(0, currentWord.length - suffix.length)
            }
            if (currentWord.startsWith("می‌")) {
                currentWord = currentWord.replace("می‌", "")
            }
        }
        return currentWord
    }
}



//fun WordVideoCard(word: String, videoUri: String, onClick: () -> Unit) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier
//            .padding(4.dp)
//            // Add this import: import androidx.compose.foundation.clickable
//            .clickable { onClick() }
//    ) {
//        Box(
//            modifier = Modifier
//                .aspectRatio(1f)
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color.Black)
//        ) {
//            AndroidView(
//                factory = { ctx ->
//                    VideoView(ctx).apply {
//                        setVideoPath(videoUri)
//                        // This prevents the mini-videos from playing sound all at once
//                        setOnPreparedListener { it.setVolume(0f, 0f) }
//                    }
//                },
//                modifier = Modifier.fillMaxSize()
//            )
//            Icon(
//                imageVector = Icons.Default.PlayArrow,
//                contentDescription = null,
//                tint = Color.White.copy(alpha = 0.5f),
//                modifier = Modifier.size(16.dp).align(Alignment.Center)
//            )
//        }
//        Text(
//            text = word,
//            style = MaterialTheme.typography.labelSmall,
//            modifier = Modifier.padding(top = 4.dp),
//            maxLines = 1
//        )
//    }
//}
//
//Box(modifier = Modifier.height(500.dp)) {
//    androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
//        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(4),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        modifier = Modifier.fillMaxSize()
//    ) {
//        videoMap.forEach { (word, uri) ->
//            item {
//                WordVideoCard(word = word, videoUri = uri) {
//                    // When clicked, play this video in the main player
//                    videoQueue = listOf(uri)
//                    currentIndex = 0
//                }
//            }
//        }
//    }
//}

