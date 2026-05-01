package com.mustafa.project001.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mustafa.project001.R

@Composable
fun Dictionary() {
    SignLanguageApp()
}

@Composable
fun SignLanguageApp() {

    val context = LocalContext.current

    var text by remember { mutableStateOf("") }
    var videoQueue by remember { mutableStateOf(listOf<String>()) }
    var currentIndex by remember { mutableStateOf(0) }

    // Helper function to convert resource ID to URI string
    fun getVideoUri(resId: Int): String {
        return "android.resource://${context.packageName}/$resId"
    }

    // Map words → videos (put files in res/raw)
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

    // Speech launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val results = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            if (!results.isNullOrEmpty()) {

                val spokenText = results[0]

                // Clean Persian text
                val words = spokenText
                    .replace("[^\\u0600-\\u06FF\\s]".toRegex(), "")
                    .trim()
                    .split(" ")

                text = spokenText

                // Build video queue
                videoQueue = words.mapNotNull { word ->
                    videoMap[word]
                }

                currentIndex = 0
            }
        }
    }

    Column(modifier = Modifier.padding(20.dp)) {

        Text("SignFlow", modifier = Modifier.padding(10.dp))

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {

            // Play Button
            Button(onClick = {
                val words = text
                    .replace("[^\\u0600-\\u06FF\\s]".toRegex(), "")
                    .trim()
                    .split(" ")

                videoQueue = words.mapNotNull { videoMap[it] }
                currentIndex = 0
            }) {
                Text("Play")
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Speak Button
            Button(onClick = {

                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-AF")
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                }

                speechLauncher.launch(intent)

            }) {
                Text("🎤 Speak")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Video Player
        AndroidView(
            factory = { ctx ->
                VideoView(ctx)
            },
            update = { videoView ->

                if (videoQueue.isNotEmpty() && currentIndex < videoQueue.size) {

                    val videoPath = videoQueue[currentIndex]

                    // Prevent reload loop
                    if (videoView.tag != videoPath) {

                        videoView.tag = videoPath
                        videoView.setVideoPath(videoPath)

                        videoView.setOnCompletionListener {
                            currentIndex++
                        }

                        videoView.start()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )
    }
}