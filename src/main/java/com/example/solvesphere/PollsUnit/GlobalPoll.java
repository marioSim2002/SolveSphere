package com.example.solvesphere.PollsUnit;

public class GlobalPoll {
    private long id;
    private String question;
    private String optionYes;
    private String optionNo;
    private int votesYes;
    private int votesNo;

    // קונסטרקטור לשימוש ביצירת סקר חדש (id יוקצה מה־DB)
    public GlobalPoll(String question, String optionYes, String optionNo) {
        this.question = question;
        this.optionYes = optionYes;
        this.optionNo = optionNo;
        this.votesYes = 0;
        this.votesNo = 0;
    }

    // קונסטרקטור מלא לטעינה מה־DB
    public GlobalPoll(long id, String question, String optionYes, String optionNo, int votesYes, int votesNo) {
        this.id = id;
        this.question = question;
        this.optionYes = optionYes;
        this.optionNo = optionNo;
        this.votesYes = votesYes;
        this.votesNo = votesNo;
    }

    // Getters & Setters
    public long getId() { return id; }
    public String getQuestion() { return question; }
    public String getOptionYes() { return optionYes; }
    public String getOptionNo() { return optionNo; }
    public int getVotesYes() { return votesYes; }
    public int getVotesNo() { return votesNo; }

    public void setVotesYes(int votesYes) { this.votesYes = votesYes; }
    public void setVotesNo(int votesNo) { this.votesNo = votesNo; }
}
