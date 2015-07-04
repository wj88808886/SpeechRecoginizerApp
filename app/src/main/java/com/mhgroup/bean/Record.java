package com.mhgroup.bean;

/**
 * Created by DK Wang on 2015/3/31.
 */
public class Record {

    private int id;
    private String firstWord;
    private String wholePhrase;
    private String changedPhrase;
    private int length;
    private int frequency;

    public Record(String firstWord, String wholePhrase,String changedPhrase)
    {
        this.firstWord = firstWord;
        this.wholePhrase = wholePhrase;
        this.changedPhrase = changedPhrase;
        this.length = getLength(wholePhrase);
    }

    public Record(int id, String firstWord, String wholePhrase, String changedPhrase, int length, int frequency)
    {
        this.id = id;
        this.firstWord = firstWord;
        this.wholePhrase = wholePhrase;
        this.changedPhrase = changedPhrase;
        this.length = length;
        this.frequency = frequency;
    }

    private int getLength(String input)
    {
        String[] tokens = input.split(" ");
        return tokens.length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    public String getWholePhrase() {
        return wholePhrase;
    }

    public void setWholePhrase(String wholePhrase) {
        this.wholePhrase = wholePhrase;
    }

    public String getChangedPhrase() {
        return changedPhrase;
    }

    public void setChangedPhrase(String changedPhrase) {
        this.changedPhrase = changedPhrase;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String toString()
    {
        return "FirstWord: " + this.firstWord + "\n" +
                "WholePhrase: " + this.wholePhrase + "\n" +
                "ChangedPhrase: " + this.changedPhrase + "\n";
    }

}
