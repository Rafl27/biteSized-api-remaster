package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.UserAllVotes
import com.biteSized.bitesizedv4.repository.CommentVoteRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentVoteImpl (private val commentVoteRepository: CommentVoteRepository) : CommentVote {
    override fun allUserVotes(userId: Long): ResponseEntity<List<UserAllVotes>> {
        val queryResult = commentVoteRepository.getAllUserVotes(userId)
        val allUserVotesEntity = queryResult.map {
            userVotes ->
            UserAllVotes (
                    userVotes[0] as Long,
                    userVotes[1] as String?,
                    userVotes[2] as String,
                    userVotes[3] as Date?,
                    userVotes[4] as Int,
                    userVotes[5] as Int,
                    userVotes[6] as Long,
                    userVotes[7] as String
            )
        }
        return ResponseEntity(allUserVotesEntity, HttpStatus.OK)
    }
}