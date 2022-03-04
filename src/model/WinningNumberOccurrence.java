package model;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

public class WinningNumberOccurrence {
    private int winningNumber;
    private int occurrences;

    public WinningNumberOccurrence() {
    }

    public void setWinningNumber(int winningNumber) {
        this.winningNumber = winningNumber;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public int getWinningNumber() {
        return winningNumber;
    }

    public int getOccurrences() {
        return occurrences;
    }    
}