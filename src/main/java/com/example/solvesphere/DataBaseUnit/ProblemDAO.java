package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.AdminProblem;
import com.example.solvesphere.UserData.Problem;

import java.security.cert.PolicyNode;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ProblemDAO {
    List<Problem> getProblemsByUserInterest(Map<String, Integer> userInterests);
    List<Problem> searchProblems(String keyword);
    List<String> getTagsByProblemId(long problemId);

    List<Problem> fetchAllProblems();
    List<Problem> getProblemsPostedByUsers(List<Long> userIds);

    List<Problem> getProblemsPostedByCurrentUser(long userId);  //fetch problems posted by the current user

    List<Problem> getProblemsByCountry(String country);
    boolean addProblem(Problem problem);

    boolean DeleteProblem(long problemId);

    Problem getProblemById(long problemId);
    Map<String, Integer> getProblemCategoryCounts();

    List<Problem> findSimilarProblemsByTitleAndDescription(String titleInput,String descInput) throws ClassNotFoundException;

    List<AdminProblem> getAdminProblems() throws SQLException, ClassNotFoundException;
}
