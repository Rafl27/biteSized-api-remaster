package com.biteSized.bitesizedv4.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "Comment")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "content", length = 5000)
    val content: String,

    @ManyToOne
    @JoinColumn(name = "story_id")
    val story: Story,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "parent_id")
    val parent: Comment?,

    @Column(name = "date")
    val date: Date?,

    @Column(name = "upvotes")
    var upvotes: Int? = 0,

    @Column(name = "downvotes")
    var downvotes: Int? = 0,

    @Column(name = "art", length = 5000)
    val art: String? = "",

    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
    var votes: MutableList<CommentVote> = mutableListOf()
)

