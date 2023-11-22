package com.biteSized.bitesizedv4.controller

import com.biteSized.bitesizedv4.DTO.FollowResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/followers")
@CrossOrigin(origins = arrayOf("http://localhost:5173"))
class FollowersController {

    @PostMapping("{userId}/follow")
    fun followUser(@PathVariable userId : Int, @RequestHeader("Authorization") authorizationHeader: String) : ResponseEntity<List<FollowResponse>> {
        return
    }
}