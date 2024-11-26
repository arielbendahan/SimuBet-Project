package com.example.simubetproject;

public class Model {
    private String homeTeam;
    private String awayTeam;
    private String commenceTime;
    private String homeOdds;
    private String awayOdds;
    private String tieOdds;

    public Model(String homeTeam, String awayTeam, String commenceTime, String homeOdds, String awayOdds) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.commenceTime = commenceTime;
        this.homeOdds = homeOdds;
        this.awayOdds = awayOdds;
    }

    public String getTieOdds() {
        return tieOdds;
    }

    public void setTieOdds(String tieOdds) {
        this.tieOdds = tieOdds;
    }

    public Model(String homeTeam, String awayTeam, String commenceTime, String homeOdds, String awayOdds, String tieOdds) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.commenceTime = commenceTime;
        this.homeOdds = homeOdds;
        this.awayOdds = awayOdds;
        this.tieOdds = tieOdds;
    }

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
}
