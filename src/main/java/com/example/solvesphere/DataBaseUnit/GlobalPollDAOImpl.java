package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.PollsUnit.GlobalPoll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalPollDAOImpl implements GlobalPollDAO {


    @Override
    public boolean createPoll(GlobalPoll poll) {
        String sql = "INSERT INTO global_polls (question, option_yes, option_no, votes_yes, votes_no) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, poll.getQuestion());
            stmt.setString(2, poll.getOptionYes());
            stmt.setString(3, poll.getOptionNo());
            stmt.setInt(4, poll.getVotesYes()); // usually 0 on creation
            stmt.setInt(5, poll.getVotesNo());  // usually 0 on creation

            return stmt.executeUpdate() > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<GlobalPoll> getAllPolls() {
        List<GlobalPoll> polls = new ArrayList<>();
        String sql = "SELECT * FROM global_polls ORDER BY id DESC";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                GlobalPoll poll = new GlobalPoll(
                        rs.getLong("id"),
                        rs.getString("question"),
                        rs.getString("option_yes"),
                        rs.getString("option_no"),
                        rs.getInt("votes_yes"),
                        rs.getInt("votes_no")
                );
                polls.add(poll);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return polls;
    }

    @Override
    public boolean votePoll(long pollId, boolean voteYes) {
        return false;
    }

    @Override
    public boolean voteYes(long pollId) {
        return updateVoteCount(pollId, true);
    }

    @Override
    public boolean voteNo(long pollId) {
        return updateVoteCount(pollId, false);
    }

    private boolean updateVoteCount(long pollId, boolean isYesVote) {
        String column = isYesVote ? "votes_yes" : "votes_no";
        String sql = "UPDATE global_polls SET " + column + " = " + column + " + 1 WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, pollId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean hasUserVoted(long pollId, long userId) {
        String sql = "SELECT COUNT(*) FROM global_poll_votes WHERE poll_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, pollId);
            stmt.setLong(2, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean recordVote(long pollId, long userId, boolean voteYes) {
        if (hasUserVoted(pollId, userId)) return false;

        String insertSql = "INSERT INTO global_poll_votes (poll_id, user_id, voted_option) VALUES (?, ?, ?)";
        boolean updated = updateVoteCount(pollId, voteYes);

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSql)) {

            stmt.setLong(1, pollId);
            stmt.setLong(2, userId);
            stmt.setString(3, voteYes ? "YES" : "NO");
            return stmt.executeUpdate() > 0 && updated;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
