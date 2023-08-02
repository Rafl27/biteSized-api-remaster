package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.model.User
import com.biteSized.bitesizedv4.security.JwtUtil
import com.biteSized.bitesizedv4.util.Replace
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TokenServiceImpl (@Autowired private val jwtUtil: JwtUtil, private val replace: Replace) : TokenService{
    override fun getUserTokenClaims(authorizationHeader: String): User {
        val token = replace.bearer(authorizationHeader)
        val claims = jwtUtil.decodeToken(token)
        val userId = claims.get("id", Integer::class.java)
        val username = claims.get("username", String::class.java)
        val profilePicture = claims.get("profilePicture", String::class.java)
        val email = claims.get("email", String::class.java)
        return User(userId.toLong(), username, profilePicture, email)
    }

}