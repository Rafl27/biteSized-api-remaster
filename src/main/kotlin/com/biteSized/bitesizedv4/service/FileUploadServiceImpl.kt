package com.biteSized.bitesizedv4.service

import net.coobird.thumbnailator.Thumbnails
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.io.IOException
import java.util.*

@Service
class FileUploadServiceImpl (@Value("\${file.upload.directory}") private val uploadDirectory: String) : FileUploadService {
    init {
        // Creates the directory if it doesn't exist
        val directory = File(uploadDirectory)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    override fun uploadFile(file: MultipartFile) {
        try {
            val allowedImageTypes = setOf("image/jpeg", "image/jpg", "image/png", "image/gif")
            val contentType = file.contentType

            if (contentType != null && allowedImageTypes.contains(contentType.lowercase(Locale.getDefault()))) {
                val destinationPath = Path.of(uploadDirectory, file.originalFilename!!)

                Thumbnails.of(file.inputStream)
                    .scale(0.5)
                    .toFile(destinationPath.toFile())
            } else {
                throw IllegalArgumentException("Invalid file type. Only image files (JPEG, PNG, GIF) are allowed.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}