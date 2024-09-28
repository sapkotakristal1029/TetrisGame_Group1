package main.scoresRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scores {
    private static final String Scores_file = "database/scores.json";
    private static final String SCORES_DIRECTORY = "database";

    private static void ensureDirectoryExists() {
        File directory = new File(SCORES_DIRECTORY);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created: " + SCORES_DIRECTORY);
            } else {
                System.out.println("Failed to create directory: " + SCORES_DIRECTORY);
                throw new RuntimeException("Unable to create directory for scores.");
            }
        }
    }

    public static void saveScore(Player player){
        ensureDirectoryExists();
        Gson gson = new Gson();
        List<Player> playerList = LoadScores();
        playerList.add(player);

        // Sort the list in descending order by score
        playerList.sort(Comparator.comparingInt(Player::score).reversed());
        // Keep only the top 10 scores
        if (playerList.size() > 10) {
            playerList = playerList.subList(0, 10);
        }
        try (FileWriter fw = new FileWriter(Scores_file)) {
             gson.toJson(playerList, fw);
        } catch (IOException e) {
             System.out.println("An error occurred while saving the score: " + e.getMessage());
             throw new RuntimeException(e);
        }
    }

    private static List<Player> LoadScores(){
        ensureDirectoryExists();
        Gson gson = new Gson();
        List<Player> playerList = new ArrayList<>();
        try (FileReader fr = new FileReader(Scores_file)) {
            Type listType = new TypeToken<ArrayList<Player>>(){}.getType();
            playerList = gson.fromJson(fr, listType);
        } catch (FileNotFoundException e) {
            // File not found, let's create a new empty JSON file
            System.out.println("File not found. Creating a new file: " + Scores_file);
            try (FileWriter writer = new FileWriter(Scores_file)) {
                gson.toJson(playerList, writer);
            } catch (IOException ioException) {
                System.out.println("An error occurred while creating the file: " + ioException.getMessage());
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred while loading the scores: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return playerList != null ? playerList : new ArrayList<>();
    }

    public static void displayTopScores(int top){
        List<Player> playerList = LoadScores();

        playerList.sort(Comparator.comparingInt(Player::score).reversed());
        int rankWidth = 10;
        int nameWidth = 20;
        int scoreWidth = 10;

        System.out.println("Top "+top+" scores:");
        System.out.printf("%-" + rankWidth + "s%-" + nameWidth + "s%-" + scoreWidth + "s%n", "Rank", "Name", "Score");
        System.out.println(">".repeat(rankWidth )+"=".repeat(nameWidth
        )+"<".repeat(scoreWidth)); // Divider line

        for (int i=0; i < Math.min(top, playerList.size()); i++){
            Player p = playerList.get(i);
            System.out.printf("%-"+ rankWidth+ "s%-"+nameWidth+"s%-"+scoreWidth+"d%n",("No."+(i+1)), p.name(), p.score());
        }
    }

    public static List<Player> getScores(){
        List<Player> playerList = LoadScores();
        return new ArrayList<>(playerList);
    }

    public static int lastScore(){
        List<Player> topPlayers = Scores.getScores();
        int listLen = topPlayers.size();
        return topPlayers.get(listLen-1).score();
    }

    public static boolean have10() {
        return (getScores().size() >= 10);
    }

    //for testing

    public static void main(String[] args) {
        // For testing, let's add some scores
//        saveScore(new Player("Alice", 150));
//        saveScore(new Player("Bob", 300));
//        saveScore(new Player("Charlie", 200));

        // Display top 30 scores
        displayTopScores(10);
        List<Player> topPlayers = getScores();
        System.out.println("Top Players List: " + topPlayers);
        if (have10()) {
            System.out.println("Have 10 scores");
        } else {System.out.println("No 10");};
    }
}

