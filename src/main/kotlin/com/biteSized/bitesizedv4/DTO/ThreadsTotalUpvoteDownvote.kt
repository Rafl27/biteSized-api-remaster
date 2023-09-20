package com.biteSized.bitesizedv4.DTO

import java.math.BigDecimal

data class ThreadsTotalUpvoteDownvote(
    val storyId : Long,
    val totalUpvotes : BigDecimal,
    val totalDownVotes : BigDecimal
)
