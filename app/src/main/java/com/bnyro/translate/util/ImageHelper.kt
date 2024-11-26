

package com.bnyro.translate.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri

data class ImageTransform(
    val width: Int,
    val height: Int,
    val offsetX: Int,
    val offsetY: Int
)

object ImageHelper {
    fun getImage(context: Context, uri: Uri): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }
    }

    fun setAlpha(originalBitmap: Bitmap, alpha: Int): Bitmap {
        val newBitmap = Bitmap.createBitmap(
            originalBitmap.getWidth(),
            originalBitmap.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(newBitmap)
        val paint = Paint().apply { this.alpha = alpha }
        canvas.drawBitmap(originalBitmap, 0f, 0f, paint)
        return newBitmap
    }

    fun cropImage(
        targetBitmap: Bitmap,
        transform: ImageTransform
    ): Bitmap = Bitmap.createBitmap(
        targetBitmap,
        transform.offsetX,
        transform.offsetY,
        transform.width,
        transform.height
    )
}