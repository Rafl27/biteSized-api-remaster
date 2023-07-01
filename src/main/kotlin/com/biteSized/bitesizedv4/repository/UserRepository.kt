package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.User
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
}