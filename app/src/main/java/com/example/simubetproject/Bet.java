package com.example.simubetproject;

import java.util.ArrayList;

public class Bet {
    private String userId;
    private ArrayList<Model> selectedBets;
    private double amountBet;
    private double totalOddMultiplier;
    private double totalAmountWon;
    private String result;  // NEW FIELD: Result of the bet (e.g., "PENDING", "WON", "LOST")

    public Bet() {
        // Default constructor required for calls to DataSnapshot.getValue(Bet.class)
    }

    public Bet(String userId, ArrayList<Model> selectedBets, double amountBet, double totalOddMultiplier, double totalAmountWon, String result) {
        this.userId = userId;
        this.selectedBets = selectedBets;
        this.amountBet = amountBet;
        this.totalOddMultiplier = totalOddMultiplier;
        this.totalAmountWon = totalAmountWon;
        this.result = result;  // Initialize the result
    }

    public Bet(String userId, ArrayList<Model> selectedBets, double amountBet, Double totalOddMultiplier, double v) {

    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Model> getSelectedBets() {
        return selectedBets;
    }

    public void setSelectedBets(ArrayList<Model> selectedBets) {
        this.selectedBets = selectedBets;
    }

    public double getAmountBet() {
        return amountBet;
    }

    public void setAmountBet(double amountBet) {
        this.amountBet = amountBet;
    }

    public double getTotalOddMultiplier() {
        return totalOddMultiplier;
    }

    public void setTotalOddMultiplier(double totalOddMultiplier) {
        this.totalOddMultiplier = totalOddMultiplier;
    }

    public double getTotalAmountWon() {
        return totalAmountWon;
    }

    public void setTotalAmountWon(double totalAmountWon) {
        this.totalAmountWon = totalAmountWon;
    }

    // Getter and setter for result
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<String> getBets() {
        return null;
    }

    public String getAnteAmount() {
        return "";
    }

    public String getPossibleWinning() {
        return "";
    }

    public Object getOddMultiplier() {
        return null;
    }

    public Object getAmountWon() {
        return null;
    }
}
