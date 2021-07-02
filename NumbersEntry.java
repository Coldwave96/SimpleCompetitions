/**
 * The NumbersEntry class is the superclass of AutoNumbersEntry.
 * It provides NumbersEntry used in the LuckyNumbersCompetition when generate manual entries.
 */
public class NumbersEntry extends Entry {
    private int[] numbers; //store numbers generated in  LuckyNumbersCompetition

    //setters and getters
    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public int[] getNumbers() {
        return numbers;
    }
}
