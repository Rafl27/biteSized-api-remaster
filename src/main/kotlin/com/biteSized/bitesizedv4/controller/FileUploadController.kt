package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.service.FileUploadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = arrayOf("http://localhost:5173"))
class FileUploadController (private val fileUploadService: FileUploadService) {
    @PostMapping
    fun uploadImage(@RequestParam("file") file: MultipartFile) : ResponseEntity<String> {
        fileUploadService.uploadFile(file)
        return ResponseEntity.ok("Image uploaded successfully.")
    }
}