package com.example.videoplayerapplication.ui.components

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.net.Uri
import android.util.Log
import android.util.Size

data class Video(
    val id: Long,
    val title: String,
    val duration: Long,
    val size: Long,
    val thumbnail: Bitmap?,
    val uri: Uri,
    val date: Long
)

fun getVideos(context: Context): List<Video> {
    val videos = mutableListOf<Video>()
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DATE_ADDED
    )

    context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        MediaStore.Video.Media.DATE_ADDED + " DESC"
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val title = cursor.getString(titleColumn)
            val duration = cursor.getLong(durationColumn)
            val size = cursor.getLong(sizeColumn)
            val date = cursor.getLong(dateColumn) * 1000

            val uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id.toString())
            val thumbnail = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(uri, Size(1920, 1080), null)
            } else {
                null
            }

            videos.add(Video(id, title, duration, size, thumbnail, uri, date))
        }
    }

    return videos
}



