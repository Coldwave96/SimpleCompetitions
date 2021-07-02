import java.io.Serializable;

/**
 * The Entry class is the superclass of NumbersEntry.
 * It provides normal entry used in the RandomPickCompetition type competitions.
 */
public class Entry implements Serializable {
    /*
    @param entryId entry identifier
    @param prize   entry prize in RandomPickCompetition
     */
    private int entryId;
    private int prize;

    //setters and getters
    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public int getPrize() {
        return prize;
    }
}
