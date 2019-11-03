package com.mantzavelas.tripassistantapi.photos.datamuse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelativeWord {

    private String word;

    private int score;

    public RelativeWord() {
    }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

}
