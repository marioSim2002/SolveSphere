package com.example.solvesphere.ProblemData;

import com.example.solvesphere.UserData.Problem;
import java.util.List;

public interface ProblemDAO {
    List<Problem> getProblemsByUserInterest(List<String> userInterests);
    Problem getProblemById(int problemId);
    List<Problem> searchProblems(String keyword);
}
