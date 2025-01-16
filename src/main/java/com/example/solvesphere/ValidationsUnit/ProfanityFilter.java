package com.example.solvesphere.ValidationsUnit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProfanityFilter {

    private String bannedWordsStr;
    public ProfanityFilter() {
        bannedWordsStr = "";
        String filePath = "G:\\My Drive\\solveSphere\\BannedWords.txt"; //file path to banned words
        loadBannedWords(filePath);
    }

    //load banned words from the file
    private void loadBannedWords(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line.trim().toLowerCase()).append(",");
            }
            bannedWordsStr = builder.toString();
        } catch (IOException e) {
            System.err.println("Error reading BannedWords.txt: " + e.getMessage());
        }
    }

    // FILTER //
    public String filterText(String input) {
        if (input == null || input.isEmpty()) {
            return input; // default
        }

        String filteredText = input;
        String[] bannedWordsArray = bannedWordsStr.split(","); //split the banned words string into individual words

        for (String word : bannedWordsArray) {
            if (filteredText.toLowerCase().contains(word)) {
                String replacement = "*".repeat(word.length());
                filteredText = filteredText.replaceAll("(?i)" + word, replacement);
            }
        }
        return filteredText;
    }
}