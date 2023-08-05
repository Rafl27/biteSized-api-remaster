package com.biteSized.bitesizedv4.service

import org.apache.catalina.User

interface TokenService {
    fun getUserTokenClaims (authorizationHeader : String) : com.biteSized.bitesizedv4.model.User
    fun getUserId (authorizationHeader: String) : Int
}