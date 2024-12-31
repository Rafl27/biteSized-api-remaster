package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

@Entity
data class UserTotalVotes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Column(name = "totalUpvotes")
    var upvotes: Int? = 0,

    @Column(name = "totalDownvotes")
    var downvotes: Int? = 0,
)
