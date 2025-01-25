package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.UserData.Problem;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalStatsController {

    @FXML
    private PieChart globalStatsPieChart;

    public void initialize() {initChart();}

    @FXML
    private void handleClose() {
        Stage stage = (Stage) globalStatsPieChart.getScene().getWindow();
        stage.close();
    }

    private void initChart(){
        ProblemDAO problemDAO = new ProblemDAOImpl();
        List<Problem> allProblems = problemDAO.fetchAllProblems();
        Map<String, Integer> globalCategoryCounts = new HashMap<>();

        for (Problem problem : allProblems) {
            globalCategoryCounts.put(problem.getCategory(),
                    globalCategoryCounts.getOrDefault(problem.getCategory(), 0) + 1);
        }

        globalStatsPieChart.getData().clear();
        for (Map.Entry<String, Integer> entry : globalCategoryCounts.entrySet()) {
            globalStatsPieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }
}
