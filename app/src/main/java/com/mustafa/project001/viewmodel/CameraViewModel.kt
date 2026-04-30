package com.mustafa.project001.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

data class ProcessedImage(
    val uri: String,
    val bitmap: Bitmap,
    val resizedBitmap: Bitmap,
    val timestamp: Long = System.currentTimeMillis()
)

class CameraViewModel : ViewModel() {

    private val _capturedImage = MutableStateFlow<ProcessedImage?>(null)
    val capturedImage: StateFlow<ProcessedImage?> = _capturedImage.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _bottomSheetState = MutableStateFlow(BottomSheetState.HIDDEN)
    val bottomSheetState: StateFlow<BottomSheetState> = _bottomSheetState.asStateFlow()

    private val _sheetOffset = MutableStateFlow(0f)
    val sheetOffset: StateFlow<Float> = _sheetOffset.asStateFlow()

    fun setCapturedImage(image: ProcessedImage) {
        _capturedImage.value = image
    }

    fun updateBottomSheetState(state: BottomSheetState) {
        _bottomSheetState.value = state
    }

    fun updateSheetOffset(offset: Float) {
        _sheetOffset.value = offset
    }

    fun processAndSaveImage(context: Context, bitmap: Bitmap) {
        viewModelScope.launch {
            _isProcessing.value = true
            _bottomSheetState.value = BottomSheetState.PROCESSING

            // Simulate processing steps with progress
            for (i in 1..100 step 10) {
                _progress.value = i / 100f
                delay(20) // Smooth animation
            }

            // Resize image to 224x224 for model input
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

            // Save original image
            val filename = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
            val file = File(context.filesDir, filename)

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
            }

            val processedImage = ProcessedImage(
                uri = Uri.fromFile(file).toString(),
                bitmap = bitmap,
                resizedBitmap = resizedBitmap
            )

            _capturedImage.value = processedImage
            _progress.value = 1f
            delay(100)
            _isProcessing.value = false
            _bottomSheetState.value = BottomSheetState.EXPANDED
        }
    }

    fun clearImage() {
        _capturedImage.value = null
        _bottomSheetState.value = BottomSheetState.HIDDEN
        _progress.value = 0f
    }

    fun hideBottomSheet() {
        _bottomSheetState.value = BottomSheetState.HIDDEN
    }
}

enum class BottomSheetState {
    HIDDEN,
    PROCESSING,
    EXPANDED
}