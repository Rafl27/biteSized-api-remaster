package com.biteSized.bitesizedv4.model

import jakarta.persistence.*

@Entity
@Table(name = "Comment")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "content")
    val content: String,

    @ManyToOne
    @JoinColumn(name = "story_id")
    val story: Story,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "parent_id")
    val parent: Comment?
)

