package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
}