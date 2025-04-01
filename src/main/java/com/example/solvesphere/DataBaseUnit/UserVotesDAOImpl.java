package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DataBaseUnit.DBQueries.VoteQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserVotesDAOImpl implements UserVotesDAO {

    @Override
    public boolean hasUserVoted(long userId, long commentId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VoteQueries.CHECK_USER_VOTED)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, commentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if a record exists
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void recordVote(long userId, long commentId, String voteType) {
        String currentVoteType = getUserVoteType(userId, commentId);

        if (currentVoteType == null) {
            // user hasn't voted yet, insert a new record
            try (Connection conn = DatabaseConnectionManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(VoteQueries.RECORD_VOTE)) {
                stmt.setLong(1, userId);
                stmt.setLong(2, commentId);
                stmt.setString(3, voteType);
                stmt.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (!currentVoteType.equals(voteType)) {
            // User has voted differently, update the vote type
            try (Connection conn = DatabaseConnectionManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(VoteQueries.UPDATE_VOTE_TYPE)) {
                stmt.setString(1, voteType);
                stmt.setLong(2, userId);
                stmt.setLong(3, commentId);
                stmt.executeUpdate();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //user is trying to cast the same vote again
            System.out.println("User has already cast this type of vote. No action taken.");
        }
    }




    @Override

    public String getUserVoteType(long userId, long commentId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VoteQueries.GET_USER_VOTE_TYPE)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, commentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("vote_type"); // Return the vote type (upvote or downvote)
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Return null if no vote is found
    }

    @Override
    public void decrementUpvote(long commentId) {
        String sql = "UPDATE comments SET upvotes = upvotes - 1 WHERE id = ? AND upvotes > 0";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decrementDownvote(long commentId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VoteQueries.DECREMENT_DOWNVOTE_SCRIPT)) {
            stmt.setLong(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void incrementUpvote(long commentId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VoteQueries.INCREMENT_UPVOTE_SCRIPT)) {
            stmt.setLong(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void incrementDownvote(long commentId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(VoteQueries.INCREMENT_DOWNVOTE_SCRIPT)) {
            stmt.setLong(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
