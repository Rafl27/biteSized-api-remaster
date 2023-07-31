package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsernameAndPassword(username: String, password: String): User
    fun findByUsername(username: String): User
    fun findByEmail(email: String) : User
}
