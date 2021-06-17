package com.talent.crossbar.models;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String question,optionA,optionB,optionC,optionD,correct,answered;
    private int attemptedCount;
    private long epochTime;
    private List<String> optionAList = new ArrayList<>();
    private List<String> optionBList = new ArrayList<>();
    private List<String> optionCList = new ArrayList<>();
    private List<String> optionDList = new ArrayList<>();

    public Question() {
    }

    public Question(String question, String optionA, String optionB, String optionC, String optionD, String correct, String answer, int attemptedCount, long epochTime, List<String> optionAList, List<String> optionBList, List<String> optionCList, List<String> optionDList) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct = correct;
        this.answered = answer;
        this.attemptedCount = attemptedCount;
        this.epochTime = epochTime;
        this.optionAList = optionAList;
        this.optionBList = optionBList;
        this.optionCList = optionCList;
        this.optionDList = optionDList;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answer) {
        this.answered = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public int getAttemptedCount() {
        return attemptedCount;
    }

    public void setAttemptedCount(int attemptedCount) {
        this.attemptedCount = attemptedCount;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    public List<String> getOptionAList() {
        return optionAList;
    }

    public void setOptionAList(List<String> optionAList) {
        this.optionAList = optionAList;
    }

    public List<String> getOptionBList() {
        return optionBList;
    }

    public void setOptionBList(List<String> optionBList) {
        this.optionBList = optionBList;
    }

    public List<String> getOptionCList() {
        return optionCList;
    }

    public void setOptionCList(List<String> optionCList) {
        this.optionCList = optionCList;
    }

    public List<String> getOptionDList() {
        return optionDList;
    }

    public void setOptionDList(List<String> optionDList) {
        this.optionDList = optionDList;
    }
}
