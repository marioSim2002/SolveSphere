package com.example.solvesphere.ValidationsUnit;

import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public abstract class ValidateInputData {

    public static boolean validData(String[] dataArr){
        for (String data : dataArr) {
            if (data == null || data.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean validEmail(String email){
        return email.contains("@");
    }
}
