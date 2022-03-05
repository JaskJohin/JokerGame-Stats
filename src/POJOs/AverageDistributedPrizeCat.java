package POJOs;

/**
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 * @author Thanos Theodoropoulos
 */

public class AverageDistributedPrizeCat {
    private int categoryId;
    private long averageDistributed;

    public AverageDistributedPrizeCat() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public long getAverageDistributed() {
        return averageDistributed;
    }

    public void setAverageDistributed(long averageDistributed) {
        this.averageDistributed = averageDistributed;
    } 
}