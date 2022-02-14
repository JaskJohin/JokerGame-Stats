package POJOClasses;

/**
 * @author Aleksandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Athanasios Theodoropoulos
 */

public class DrawData1 {
    private int drawID;
    private long drawTime;
    private String status;
    private int drawBreak;
    private int visualDraw;  

    public int getDrawID() {
        return drawID;
    }

    public void setDrawID(int drawID) {
        this.drawID = drawID;
    }

    public long getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(long drawTime) {
        this.drawTime = drawTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDrawBreak() {
        return drawBreak;
    }

    public void setDrawBreak(int drawBreak) {
        this.drawBreak = drawBreak;
    }

    public int getVisualDraw() {
        return visualDraw;
    }

    public void setVisualDraw(int visualDraw) {
        this.visualDraw = visualDraw;
    }    
}
