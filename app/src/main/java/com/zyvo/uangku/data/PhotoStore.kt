package com.zyvo.uangku.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Foto target disimpan sebagai JPEG kecil di penyimpanan internal app, bukan
 * menunjuk ke galeri — supaya tetap ada walau foto aslinya dihapus.
 */
object PhotoStore {

    fun save(context: Context, uri: Uri, id: Long): String? = runCatching {
        val bitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return null

        val maxSide = 1000f
        val scale = minOf(1f, maxSide / maxOf(bitmap.width, bitmap.height))
        val scaled = if (scale < 1f) {
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * scale).toInt().coerceAtLeast(1),
                (bitmap.height * scale).toInt().coerceAtLeast(1),
                true
            )
        } else {
            bitmap
        }

        val dir = File(context.filesDir, "goals").apply { mkdirs() }
        val file = File(dir, "goal_$id.jpg")
        FileOutputStream(file).use { out ->
            scaled.compress(Bitmap.CompressFormat.JPEG, 85, out)
        }
        if (scaled !== bitmap) scaled.recycle()
        bitmap.recycle()
        file.absolutePath
    }.getOrNull()

    fun delete(path: String?) {
        if (path.isNullOrBlank()) return
        runCatching { File(path).delete() }
    }
}
