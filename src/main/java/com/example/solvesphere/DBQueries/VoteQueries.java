package com.example.solvesphere.DBQueries;

public class VoteQueries {

    ///////comments////////
    public static final String DECREMENT_DOWNVOTE_SCRIPT = "UPDATE comments SET downvotes = downvotes - 1 WHERE id = ? AND downvotes > 0";
    public static final String DECREMENT_UPVOTE_SCRIPT = "UPDATE comments SET upvotes = upvotes - 1 WHERE id = ? AND upvotes > 0";
    public static final String INCREMENT_UPVOTE_SCRIPT = "UPDATE comments SET upvotes = upvotes + 1 WHERE id = ?";
    public static final String INCREMENT_DOWNVOTE_SCRIPT = "UPDATE comments SET downvotes = downvotes + 1 WHERE id = ?";
     public static final String DELETE_VOTE_SCRIPT =  "DELETE FROM user_votes WHERE user_id = ? AND comment_id = ?";
///////***************************/////////

    //////// users ///////

    public static final String CHECK_USER_VOTED = "SELECT COUNT(*) FROM comment_votes WHERE user_id = ? AND comment_id = ?";
    public static final String RECORD_VOTE =
            "INSERT INTO comment_votes (user_id, comment_id, vote_type) " +
                    "VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE vote_type = VALUES(vote_type);";
 
    public static final String GET_USER_VOTE_TYPE = "SELECT vote_type FROM comment_votes WHERE user_id = ? AND comment_id = ?";
    public static final String UPDATE_VOTE_TYPE = "UPDATE comment_votes SET vote_type = ? WHERE user_id = ? AND comment_id = ?";

}
