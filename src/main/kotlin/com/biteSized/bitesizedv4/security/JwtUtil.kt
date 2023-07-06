package com.biteSized.bitesizedv4.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtUtil {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    fun generateToken(username: String): String {
        val claims: Map<String, Any> = HashMap()
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        return extractClaims(token).subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            extractClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun extractClaims(token: String): Claims {
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }

    companion object {
        const val EXPIRATION_TIME: Long = 864_000_000 // 10 days
    }
}