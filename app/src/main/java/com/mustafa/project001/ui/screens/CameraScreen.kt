package com.mustafa.project001.ui.screens

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.*
import com.mustafa.project001.ui.components.PermissionDeniedScreen
import com.mustafa.project001.ui.components.PermissionRationaleScreen
import com.mustafa.project001.viewmodel.CameraViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen() {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val viewModel: CameraViewModel = viewModel()

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Surface(modifier = Modifier.fillMaxSize()){
        when {
            cameraPermissionState.status.isGranted -> {
                CameraPreview(viewModel)
            }
            cameraPermissionState.status.shouldShowRationale -> {
                PermissionRationaleScreen {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
            else -> {
                PermissionDeniedScreen()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreview(viewModel: CameraViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
//    val capturedImage by viewModel.capturedImage.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
//    val progress by viewModel.progress.collectAsState()
//    val bottomSheetState by viewModel.bottomSheetState.collectAsState()
//    val sheetOffset by viewModel.sheetOffset.collectAsState()

    // Animation states
    val captureButtonScale = remember { Animatable(1f) }
    val progressRotation = remember { Animatable(0f) }

    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            progressRotation.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing)
                )
            )
        } else {
            progressRotation.snapTo(0f)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Camera Preview
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {

                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = surfaceProvider
                        }

                        imageCapture = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build()

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            //        BottomSheetScaffold(
            //            sheetPeekHeight = 128.dp,
            //            modifier = Modifier
            //                .padding(24.dp)
            //                .background(Color.White)
            //                .clip(RoundedCornerShape(16.dp)),
            //            sheetContent = {
            //                Column(
            //                    Modifier
            //                        .fillMaxSize()
            //                        .padding(16.dp)
            //                        .background(Color.Transparent),
            //                    horizontalAlignment = Alignment.CenterHorizontally
            //                ) {
            //                    Box(
            //                        Modifier.height(120.dp),
            //                        contentAlignment = Alignment.Center
            //                    ) {
            //                        Text("Swipe up to expand sheet")
            //                    }
            //                    Text("Sheet content")
            //
            //                }
            //            }
            //        ) { innerPadding ->
            //            Box(
            //                modifier = Modifier
            //                    .fillMaxSize()
            //                    .padding(innerPadding)
            //                    .background(color = Color.Transparent),
            //                contentAlignment = Alignment. Center
            //            ) {
            //                Card() {
            //                    Text("Scaffold Content")
            //                }
            //            }
            //        }
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    contentColor = Color.Transparent,
                    containerColor = Color.Black.copy(alpha = 0.8f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            //.height(420.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // progress ring (visible when processing)
                        if (isProcessing) {
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(160.dp)
                                        .align(Alignment.Center)

                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .graphicsLayer {
                                                rotationZ = progressRotation.value
                                            },
                                        color = Color.White,
                                        trackColor = Color.White.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .align(Alignment.Center)
                                ) {
                                    Text("Scaffold Content")
                                }
                            }
                            // sheet content
                            Button(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                }
                            ) {
                                Text("Hide Bottom Sheet")
                            }
                        }

                    }
                }
            }

            // status overlay
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TransparentBox(
                    fps = 30.0f,
                    prediction = "ک",
                    confidence = 90.0f,
                    inferenceTime = 3,
                    isProcessing = false,
                )

            }


            // Camera UI Overlay
            CameraOverlay(
                onCaptureClick = {
                    scope.launch {
                        showBottomSheet = true
                        // Animate capture button
                        captureButtonScale.animateTo(
                            targetValue = 0.8f,
                            animationSpec = tween(100, easing = FastOutLinearInEasing)
                        )
                        captureButtonScale.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(200, easing = LinearOutSlowInEasing)
                        )

                        imageCapture?.takePicture(
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onCaptureSuccess(image: ImageProxy) {
                                    super.onCaptureSuccess(image)

                                    // Convert and process image
                                    val bitmap = image.toBitmap()
                                    val rotatedBitmap =
                                        rotateBitmap(bitmap, image.imageInfo.rotationDegrees)

                                    viewModel.processAndSaveImage(context, rotatedBitmap)

                                    image.close()
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    exception.printStackTrace()
                                }
                            }
                        )
                    }
                },
                isProcessing = isProcessing,
                captureButtonScale = captureButtonScale.value,
                progressRotation = progressRotation.value
            )
        }
    }
}

// Transparent Box for Stats Overview
@Composable
fun TransparentBox(
    fps: Float,
    prediction: String,
    confidence: Float,
    inferenceTime: Long,
    isProcessing: Boolean = false
){
    val animatedConfidence by animateFloatAsState(
        targetValue = confidence,
        label = "ConfidentAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.Black.copy(0.1f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = if (isProcessing) {
                        listOf(
                            Color(0xFF2196F3).copy(alpha = 0.4f),
                            Color(0xFF1976D2).copy(alpha = 0.6f)
                        )
                    } else {
                        listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.6f)
                        )
                    },
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .padding(horizontal = 24.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isProcessing){
            // show loading indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // show load circle for processing
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Section for: FPS, Inference info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // FPS Indicator
                    StatItem(
                        label = "FPS",
                        value = String.format(Locale.US, "%.1f", fps),
                        color = Color(0xFF4CAF50), // Green
                        icon = "⏱️"
                    )

                    // Inference Time
                    StatItem(
                        label = "Inference",
                        value = "${inferenceTime}ms",
                        color = Color(0xFF2196F3), // Blue
                        icon = "⚡"
                    )

                    // Model Info
                    StatItem(
                        label = "Model",
                        value = "Dari Sign",
                        color = Color(0xFF9C27B0), // Purple
                        icon = "🧠"
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: Color,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}


@Composable
fun CameraOverlay(
    onCaptureClick: () -> Unit,
    isProcessing: Boolean,
    captureButtonScale: Float,
    progressRotation: Float
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ){
        // Top Bar
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Camera Predictions",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.height(20.dp))


        }

        // Bottom Controls
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.BottomCenter)
        ){
            // Capture Button with progress ring
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = captureButtonScale
                        scaleY = captureButtonScale
                    }
            ){
                // progress ring (visible when processing)
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                rotationZ = progressRotation
                            },
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }

                // Capture Button
                Box(
                    modifier = Modifier
                        .size(if (isProcessing) 60.dp else 70.dp)
                        .shadow(8.dp, CircleShape)
                        .background(
                            if (isProcessing) Color.Gray else Color.White,
                            CircleShape
                        )
                        .clip(CircleShape)
                        .align(Alignment.Center)
                ) {
                    IconButton(
                        onClick = onCaptureClick,
                        enabled = !isProcessing,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isProcessing) 50.dp else 60.dp)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF4CAF68),
                                            Color(0xFF4ECDC4)
                                        )
                                    ),
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}


// Helper functions
fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
    if (degrees == 0) return bitmap
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}