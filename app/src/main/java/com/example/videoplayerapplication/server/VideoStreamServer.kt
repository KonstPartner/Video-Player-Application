package com.example.videoplayerapplication.server

import fi.iki.elonen.NanoHTTPD
import java.io.FileInputStream
import java.io.IOException

class VideoStreamServer(port: Int) : NanoHTTPD(port) {
    var videoFilePath: String = ""

    override fun serve(session: IHTTPSession): Response {
        return try {
            val fis = FileInputStream(videoFilePath)
            newChunkedResponse(Response.Status.OK, "video/mp4", fis)
        } catch (e: IOException) {
            newFixedLengthResponse(Response.Status.FORBIDDEN, MIME_PLAINTEXT, "Error opening file")
        }
    }

    fun stopServer() {
        stop()
    }
}

