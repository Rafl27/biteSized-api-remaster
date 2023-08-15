package com.biteSized.bitesizedv4.util

import com.biteSized.bitesizedv4.DTO.UserBasicInfo
import org.springframework.stereotype.Service

@Service
class StringArrayIntoDTO () {
    fun userBasicInfo(queryResult : Array<String>) : UserBasicInfo{
        val resultString = queryResult[0]
        val parts = resultString.split(",")

        val username = parts[0]
        val email = parts[1]
        val profilePicture = parts[2]

        return UserBasicInfo(username, email, profilePicture)
    }
}