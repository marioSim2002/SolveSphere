package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.PollsUnit.GlobalPoll;

import java.util.List;

public interface GlobalPollDAO {
    boolean createPoll(GlobalPoll poll);
    List<GlobalPoll> getAllPolls();
    boolean votePoll(long pollId, boolean voteYes);

    boolean voteYes(long pollId);

    boolean voteNo(long pollId);

    boolean hasUserVoted(long pollId, long userId);

    boolean recordVote(long pollId, long userId, boolean voteYes);
}
