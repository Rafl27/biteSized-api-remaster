package com.biteSized.bitesizedv4.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.io.IOException

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
            val destinationPath = Path.of(uploadDirectory, file.originalFilename!!)
            Files.copy(file.inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}