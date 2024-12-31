package com.biteSized.bitesizedv4.service

import com.biteSized.bitesizedv4.DTO.*
import com.biteSized.bitesizedv4.controller.StoryController
import com.biteSized.bitesizedv4.enums.VoteType
import com.biteSized.bitesizedv4.exception.AlreadyVotedException
import com.biteSized.bitesizedv4.model.Story
import com.biteSized.bitesizedv4.model.StoryVote
import com.biteSized.bitesizedv4.model.UserTotalVotes
import com.biteSized.bitesizedv4.repository.StoryRepository
import com.biteSized.bitesizedv4.repository.UserTotalVotesRepository
import com.biteSized.bitesizedv4.repository.UserRepository
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.logging.Logger
import kotlin.math.ceil

@Service
class StoryServiceImpl (private val storyRepository: StoryRepository,
                        private val tokenService: TokenService,
                        private val userRepository: UserRepository,
                        private val userTotalVotesRepository: UserTotalVotesRepository
) : StoryService{
    private val logger: Logger = Logger.getLogger(StoryController::class.java.name)
    override fun createStory(newStory: Story, authorization: String): ResponseEntity<Story> {
        newStory.user = tokenService.getUserTokenClaims(authorization)
        newStory.date = Date()
        val createdStory = storyRepository.save(newStory)
        logger.info("New story created: ${createdStory.title}")
        return ResponseEntity(createdStory, HttpStatus.CREATED)
    }

    override fun getCurrentUserStories(authorization: String): ResponseEntity<UserStoriesCountBundle> {
        val user = userRepository.findById(tokenService.getUserId(authorization).toLong())
        if (user.isPresent) {
            val stories = user.get().stories.map { story ->
                UserStories(story.id, story.title, story.content, story.upvotes, story.downvotes, story.art)
            }
            val storyCount = stories.size
            val response = UserStoriesCountBundle(storyCount, stories)
            return ResponseEntity(response, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override  fun getUserStories(userId: Long) : ResponseEntity<UserStoriesCountBundle>{
        val user = userRepository.findById(userId)
        if (user.isPresent){
            val stories = user.get().stories.map { story ->
                UserStories(story.id, story.title, story.content, story.upvotes, story.downvotes, story.art)
            }
            val storyCount = stories.size
            val response = UserStoriesCountBundle(storyCount, stories)
            return ResponseEntity(response, HttpStatus.OK)
        } else {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
    }


    override fun getStoryById(id: Long): ResponseEntity<SingleStory> {
        val story = storyRepository.findById(id)
        if (story.isPresent) {
            val storyEntity = story.get()
            val storyResponse = SingleStory(storyEntity.id, storyEntity.title, storyEntity.content, storyEntity.date, storyEntity.upvotes, storyEntity.downvotes, storyEntity.art)
            return ResponseEntity(storyResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun deleteStoryById(id: Long): ResponseEntity<String> {
        val story = storyRepository.findById(id)
        if (story.isPresent) {
            val deletedStory = story.get()
            storyRepository.deleteById(id)
            val responseMessage = "Story with ID $id (${deletedStory.title}) has been deleted."
            return ResponseEntity(responseMessage, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun updateStoryById(id: Long, updatedStory : Story): ResponseEntity<Story> {
        val existingStory = storyRepository.findById(id)
        if (existingStory.isPresent) {
            val story = existingStory.get()
            story.title = updatedStory.title
            story.content = updatedStory.content
            val updatedStoryEntity = storyRepository.save(story)
            return ResponseEntity(updatedStoryEntity, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun storyUpvote(storyId: Long, userId : Long, authorization: String): ResponseEntity<UpvoteResponse> {
        val story = storyRepository.findById(storyId)
        if (story.isPresent) {
            val storyEntity = story.get()

            if (storyEntity.votes.any {it.user.id == userId})
                throw AlreadyVotedException("You have already voted on this story.")

            val upvote = StoryVote(story = storyEntity, user = tokenService.getUserTokenClaims(authorization), voteType = VoteType.UPVOTE)
            storyEntity.votes.add(upvote)
            //?: elvis operator, if upvotes is null it returns 0
            storyEntity.upvotes = (storyEntity.upvotes ?: 0) + 1
            storyRepository.save(storyEntity)

            val userTotalVotes = story.get().user?.let { userTotalVotesRepository.findByUserId(it.id) }
                ?: UserTotalVotes(user = story.get().user, upvotes = 0, downvotes = 0)
            userTotalVotes.upvotes = (userTotalVotes.upvotes ?: 0) + 1
            userTotalVotesRepository.save(userTotalVotes)

            val upvoteResponse = UpvoteResponse(id = storyId, upvote = storyEntity.upvotes)
            return ResponseEntity(upvoteResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun storyDownvote(storyId: Long, userId : Long, authorization: String): ResponseEntity<DownvoteResponse> {
        val story = storyRepository.findById(storyId)
        if (story.isPresent) {
            val storyEntity = story.get()

            if (storyEntity.votes.any {it.user.id == userId})
                throw AlreadyVotedException("You have already voted on this story.")

            val downvote = StoryVote(story = storyEntity, user = tokenService.getUserTokenClaims(authorization), voteType = VoteType.DOWNVOTE)
            storyEntity.votes.add(downvote)
            //?: elvis operator, if upvotes is null it returns 0
            storyEntity.downvotes = (storyEntity.downvotes ?: 0) + 1
            storyRepository.save(storyEntity)
            val downVoteResponse = DownvoteResponse(id = storyId, downvote = storyEntity.downvotes)
            return ResponseEntity(downVoteResponse, HttpStatus.OK)
        } else {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    override fun allStories(page: Int, size: Int): Page<CompleteStoryNoComments> {
        val offset = page * size
        val queryResult = storyRepository.getStoryAndUserNoComment(size, offset)
        val storyAndUserNoCommentDTO = queryResult.map { story ->
            CompleteStoryNoComments(
                story[0] as String,
                story[1] as String,
                    story[2] as Long,
                story[3] as String,
                story[4] as Date,
                story[5] as Int,
                story[6] as Int,
                story[7] as String,
                story[8] as String,
                story[9] as Long,
                    story[10] as String?,
                    story[11] as Long
            )
        }
        val totalItems = storyRepository.getNumberOfStories()
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("upvotes")))
        return PageImpl(storyAndUserNoCommentDTO, pageable, totalItems);
    }

    override fun allStoriesNewest(page: Int, size: Int): Page<CompleteStoryNoComments> {
        val offset = page * size
        val queryResult = storyRepository.getStoryAndUserNoCommentNewest(size, offset)
        val storyAndUserNoCommentDTO = queryResult.map { story ->
            CompleteStoryNoComments(
                    story[0] as String,
                    story[1] as String,
                    story[2] as Long,
                    story[3] as String,
                    story[4] as Date,
                    story[5] as Int,
                    story[6] as Int,
                    story[7] as String,
                    story[8] as String,
                    story[9] as Long,
                    story[10] as String?,
                    story[11] as Long
            )
        }
        val totalItems = storyRepository.getNumberOfStories()
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("upvotes")))
        return PageImpl(storyAndUserNoCommentDTO, pageable, totalItems);
    }

    override fun allStoriesHot(page: Int, size: Int): Page<CompleteStoryNoComments> {
        val offset = page * size
        val queryResult = storyRepository.getStoryAndUserNoCommentHot(size, offset)
        val storyAndUserNoCommentDTO = queryResult.map { story ->
            CompleteStoryNoComments(
                    story[0] as String,
                    story[1] as String,
                    story[2] as Long,
                    story[3] as String,
                    story[4] as Date,
                    story[5] as Int,
                    story[6] as Int,
                    story[7] as String,
                    story[8] as String,
                    story[9] as Long,
                    story[10] as String?,
                    story[11] as Long
            )
        }
        val totalItems = storyRepository.getNumberOfStories()
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("upvotes")))
        return PageImpl(storyAndUserNoCommentDTO, pageable, totalItems);
    }

    override fun threadsTotalUpvoteDownvote(storyId: Long): ResponseEntity<List<ThreadsTotalUpvoteDownvote>> {
        val queryResult = storyRepository.getThreadsTotalUpvotesDownvotes(storyId)
        val threadsTotalUpvoteDownvote = queryResult.map { threadsTotalUpvoteDownvote ->
            ThreadsTotalUpvoteDownvote(
                threadsTotalUpvoteDownvote[0] as Long,
                threadsTotalUpvoteDownvote[1] as BigDecimal,
                threadsTotalUpvoteDownvote[2] as BigDecimal
            )
        }

        return ResponseEntity(threadsTotalUpvoteDownvote, HttpStatus.OK)
    }

    override fun totalComments(storyId: Long): ResponseEntity<List<TotalComments>> {
        val queryResult = storyRepository.getTotalComments(storyId)
        val totalComments = queryResult.map { totalComments ->
            TotalComments(
                    (totalComments[1] as? Long) ?: 0,
                    (totalComments[0] as? Long) ?: 0,
            )
        }

        return ResponseEntity(totalComments, HttpStatus.OK)
    }
}
