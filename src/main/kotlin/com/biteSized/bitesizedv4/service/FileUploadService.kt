package com.biteSized.bitesizedv4.service

import org.springframework.web.multipart.MultipartFile

interface FileUploadService {
    fun uploadFile(file: MultipartFile)
}