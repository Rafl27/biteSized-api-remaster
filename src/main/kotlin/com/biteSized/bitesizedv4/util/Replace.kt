package com.biteSized.bitesizedv4.util

import org.springframework.stereotype.Service

@Service
class Replace {
    fun bearer(string: String): String {
        return string.replace(oldValue = "Bearer ", newValue = "")
    }
}