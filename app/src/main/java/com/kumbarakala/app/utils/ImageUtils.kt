package com.kumbarakala.app.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore

object ImageUtils {

    fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        fileName: String = "KumbaraKalaStory"
    ): Uri? {

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/KumbaraKala"
            )
        }

        val resolver = context.contentResolver

        val imageUri =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            val outputStream = resolver.openOutputStream(uri)
            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        }

        return imageUri
    }
}
fun saveImageToInternalStorage(context: android.content.Context, uri: android.net.Uri): android.net.Uri? {
    val fileName = "product_${java.util.UUID.randomUUID()}.jpg"
    val file = android.util.Log.d("IMG", "Saving to: $fileName").run {
        java.io.File(context.filesDir, fileName)
    }

    return try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        android.net.Uri.fromFile(file)
    } catch (e: Exception) {
        null
    }
}