package com.biteSized.bitesizedv4.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import java.security.Key
import kotlin.collections.HashMap

@Component
class JwtUtil {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    private fun getSigningKey(): Key {
        return Keys.hmacShaKeyFor(secretKey.toByteArray())
    }
//    private val secureKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)



    public fun generateToken(claims: Map<String, Any>): String {
        return Jwts.builder()
            .setClaims(claims)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun decodeToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(getSigningKey())
            .parseClaimsJws(token)
            .body
    }
}