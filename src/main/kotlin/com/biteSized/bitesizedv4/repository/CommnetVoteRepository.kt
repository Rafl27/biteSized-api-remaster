package com.biteSized.bitesizedv4.repository

import com.biteSized.bitesizedv4.model.CommentVote
import org.springframework.data.jpa.repository.JpaRepository

interface CommentVoteRepository : JpaRepository <CommentVote, Long> {
}