package main.scoresRecord;

public record Player(String name, int score) {
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }


}
