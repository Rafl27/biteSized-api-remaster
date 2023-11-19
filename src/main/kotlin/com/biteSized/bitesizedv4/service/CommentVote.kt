package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.UserAllVotes
import org.springframework.http.ResponseEntity

interface CommentVote {
    fun allUserVotes(userId : Long) : ResponseEntity<List<UserAllVotes>>
}