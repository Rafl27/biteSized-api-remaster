package com.biteSized.bitesizedv4.model

import com.biteSized.bitesizedv4.enums.VoteType
import jakarta.persistence.*

@Entity
data class CommentVote(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "comment_id")
    val comment: Comment,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type")
    val voteType: VoteType
)
