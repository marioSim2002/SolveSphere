package com.example.solvesphere.DataBaseUnit;

public interface UserVotesDAO {

    /**
     * Checks if a user has already voted on a specific comment.
     *
     * @param userId    The ID of the user.
     * @param commentId The ID of the comment.
     * @return true if the user has voted, false otherwise.
     */
    boolean hasUserVoted(long userId, long commentId);

    void incrementUpvote(long commentId);

    void incrementDownvote(long commentId);
    /**
     * Records a vote for a specific comment by a user.
     * If a vote already exists, it updates the vote type.
     *
     * @param userId    The ID of the user.
     * @param commentId The ID of the comment.
     * @param voteType  The type of vote ("upvote" or "downvote").
     */
    void recordVote(long userId, long commentId, String voteType);

    /**
     * Retrieves the type of vote (e.g., "upvote" or "downvote") a user has cast on a specific comment.
     *
     * @param userId    The ID of the user.
     * @param commentId The ID of the comment.
     * @return The type of vote as a string, or null if no vote exists.
     */
    String getUserVoteType(long userId, long commentId);

    void decrementUpvote(long commentId);

    void decrementDownvote(long commentId);
}
