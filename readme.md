fun CameraApp() {
val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
val viewModel: CameraViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            cameraPermissionState.status.isGranted -> {
                CameraScreen(viewModel)
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

@Composable
fun CameraScreen(viewModel: CameraViewModel) {
val context = LocalContext.current
val lifecycleOwner = LocalLifecycleOwner.current
val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
val scope = rememberCoroutineScope()

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val capturedImage by viewModel.capturedImage.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val bottomSheetState by viewModel.bottomSheetState.collectAsState()
    val sheetOffset by viewModel.sheetOffset.collectAsState()

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

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(surfaceProvider)
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

        // Camera UI Overlay
        CameraOverlay(
            onCaptureClick = {
                scope.launch {
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
                                val rotatedBitmap = rotateBitmap(bitmap, image.imageInfo.rotationDegrees)

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

        // Transparent Bottom Sheet with Image
        AnimatedVisibility(
            visible = bottomSheetState != BottomSheetState.HIDDEN,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            )
        ) {
            TransparentImageBottomSheet(
                capturedImage = capturedImage,
                isProcessing = isProcessing,
                progress = progress,
                progressRotation = progressRotation.value,
                sheetOffset = sheetOffset,
                onOffsetChange = { viewModel.updateSheetOffset(it) },
                onClose = { viewModel.hideBottomSheet() }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransparentImageBottomSheet(
capturedImage: ProcessedImage?,
isProcessing: Boolean,
progress: Float,
progressRotation: Float,
sheetOffset: Float,
onOffsetChange: (Float) -> Unit,
onClose: () -> Unit
) {
val configuration = LocalConfiguration.current
val screenHeight = configuration.screenHeightDp.dp

    val sheetHeight = if (isProcessing) 200.dp else screenHeight * 0.7f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(sheetHeight)
            .offset(y = sheetOffset.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        val newOffset = (sheetOffset + dragAmount.y).coerceIn(-200f, 200f)
                        onOffsetChange(newOffset)
                    },
                    onDragEnd = {
                        onOffsetChange(0f)
                    }
                )
            },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        ) {
            // Drag Handle and Close Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.5f))
                        .align(Alignment.Center)
                )

                // Close Button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            if (isProcessing) {
                // Processing State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Animated processing indicator
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .graphicsLayer {
                                rotationZ = progressRotation
                            }
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            progress = progress,
                            color = Color(0xFF4ECDC4),
                            strokeWidth = 4.dp,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Processing text with animated dots
                    AnimatedProcessingText()

                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Display captured image
                capturedImage?.let { image ->
                    // Original image (full size)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image.uri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Captured Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Show that 224x224 resized version is ready for AI
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = Color(0xFF4ECDC4).copy(alpha = 0.3f),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .animateContentSize()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Android,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "Ready for AI analysis (224x224)",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedProcessingText() {
val dots = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            dots.value = ""
            delay(500)
            dots.value = "."
            delay(500)
            dots.value = ".."
            delay(500)
            dots.value = "..."
            delay(500)
        }
    }

    Text(
        text = "Processing${dots.value}",
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.animateContentSize()
    )
}
