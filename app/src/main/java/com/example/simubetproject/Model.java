package com.example.simubetproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class Model implements Parcelable { //implements serializable so it can send via intent
    private String homeTeam;
    private String awayTeam;
    private String commenceTime;
    private String homeOdds;
    private String awayOdds;
    private String tieOdds;
    //for the buttons on the UI to work properly
    private boolean homeIsPressed, awayIsPressed, tieIsPressed;
    private int amountOddsButtonsPressed;
    private String selectedBet;
    private String selectedTeam;

    public Model() {
        // Default constructor required for calls to DataSnapshot.getValue(Model.class)
    }

    public Model(String homeTeam, String awayTeam, String commenceTime, String homeOdds, String awayOdds) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.commenceTime = commenceTime;
        this.homeOdds = homeOdds;
        this.awayOdds = awayOdds;

        homeIsPressed = false;
        awayIsPressed = false;
        tieIsPressed = false;

        amountOddsButtonsPressed = 0;
    }

    public Model(String homeTeam, String awayTeam, String commenceTime, String homeOdds, String awayOdds, String tieOdds) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.commenceTime = commenceTime;
        this.homeOdds = homeOdds;
        this.awayOdds = awayOdds;
        this.tieOdds = tieOdds;
    }

    protected Model(Parcel in) {
        homeTeam = in.readString();
        awayTeam = in.readString();
        commenceTime = in.readString();
        homeOdds = in.readString();
        awayOdds = in.readString();
        tieOdds = in.readString();
        homeIsPressed = in.readByte() != 0;
        awayIsPressed = in.readByte() != 0;
        tieIsPressed = in.readByte() != 0;
        amountOddsButtonsPressed = in.readInt();
        selectedBet = in.readString();
        selectedTeam = in.readString();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getCommenceTime() {
        return commenceTime;
    }

    public void setCommenceTime(String commenceTime) {
        this.commenceTime = commenceTime;
    }

    public String getHomeOdds() {
        return homeOdds;
    }

    public void setHomeOdds(String homeOdds) {
        this.homeOdds = homeOdds;
    }

    public String getAwayOdds() {
        return awayOdds;
    }

    public void setAwayOdds(String awayOdds) {
        this.awayOdds = awayOdds;
    }

    public String getTieOdds() {
        return tieOdds;
    }

    public void setTieOdds(String tieOdds) {
        this.tieOdds = tieOdds;
    }

    public boolean isHomeIsPressed() {
        return homeIsPressed;
    }

    public void setHomeIsPressed(boolean homeIsPressed) {
        this.homeIsPressed = homeIsPressed;
    }

    public boolean isAwayIsPressed() {
        return awayIsPressed;
    }

    public void setAwayIsPressed(boolean awayIsPressed) {
        this.awayIsPressed = awayIsPressed;
    }

    public boolean isTieIsPressed() {
        return tieIsPressed;
    }

    public void setTieIsPressed(boolean tieIsPressed) {
        this.tieIsPressed = tieIsPressed;
    }

    public int getAmountOddsButtonsPressed() {
        return amountOddsButtonsPressed;
    }

    public void increaseAmountOddsButtonsPressed() {
        this.amountOddsButtonsPressed += 1;
    }

    public void decreaseAmountOddsButtonsPressed() {
        this.amountOddsButtonsPressed -= 1;
    }

    public String getSelectedBet() {
        return selectedBet;
    }

    public void setSelectedBet(String selectedBet) {
        this.selectedBet = selectedBet;
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(homeTeam);
        dest.writeString(awayTeam);
        dest.writeString(commenceTime);
        dest.writeString(homeOdds);
        dest.writeString(awayOdds);
        dest.writeString(tieOdds);
        dest.writeByte((byte) (homeIsPressed ? 1 : 0));
        dest.writeByte((byte) (awayIsPressed ? 1 : 0));
        dest.writeByte((byte) (tieIsPressed ? 1 : 0));
        dest.writeInt(amountOddsButtonsPressed);
        dest.writeString(selectedBet);
        dest.writeString(selectedTeam);
    }

    @Override
    public String toString() {
        // Customize this to display meaningful details about the model
        return homeTeam + " vs " + awayTeam;
    }
}
