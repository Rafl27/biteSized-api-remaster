package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.UserAllVotes
import com.biteSized.bitesizedv4.service.CommentVote
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/comment-vote")
@CrossOrigin(origins = arrayOf("http://localhost:5173"))
class CommentVoteController (private val commentVoteService : CommentVote) {
    @GetMapping
    @RequestMapping("{userId}/all")
    fun allVotes(@PathVariable userId : Long) : ResponseEntity<List<UserAllVotes>> {
        return commentVoteService.allUserVotes(userId)
    }
}