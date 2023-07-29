package com.biteSized.bitesizedv4.DTO

import java.time.LocalDate

data class CompleteStoryResponse(val titleStory: String,
                                 val contentStory: String,
                                 val artStory: String,
                                 val dateStory: LocalDate,
                                 val upvotesStory: Int,
                                 val downvotesStory: Int,
                                 val contentComment: String,
                                 val artComment: String,
                                 val dateComment: LocalDate,
                                 val upvotesComment: Int,
                                 val downvotesComment: Int)
