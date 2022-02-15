package POJOClasses;

/**
 * @author Athanasios Theodoropoulos
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */

public class PrizeCategoriesData1 {
            private int categoryID;
            private double divident;
            private double jackpot;
            private int winners;
            private double distributed;
            private double fixed;
            private int categoryType;
            private String gameType;
            private int DrawdrawID;
            private int DrawgameID;
            
            
    public double getJackpot() {
        return jackpot;
    }

    public void setJackpot(double jackpot) {
        this.jackpot = jackpot;
    }       

    public int getWinners() {
        return winners;
    }

    public void setWinners(int winners) {
        this.winners = winners;
    }

    public double getDistributed() {
        return distributed;
    }

    public void setDistributed(double distributed) {
        this.distributed = distributed;
    }

    public double getFixed() {
        return fixed;
    }

    public void setFixed(double fixed) {
        this.fixed = fixed;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public int getDrawdrawID() {
        return DrawdrawID;
    }

    public void setDrawdrawID(int DrawdrawID) {
        this.DrawdrawID = DrawdrawID;
    }

    public int getDrawgameID() {
        return DrawgameID;
    }

    public void setDrawgameID(int DrawgameID) {
        this.DrawgameID = DrawgameID;
    }      
            
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setDivident(double divident) {
        this.divident = divident;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public double getDivident() {
        return divident;
    }
}