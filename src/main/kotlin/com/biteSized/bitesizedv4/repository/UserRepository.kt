package com.biteSized.bitesizedv4.repository

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<SecurityProperties.User, Long> {
}