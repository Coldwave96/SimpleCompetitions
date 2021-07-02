import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * The AutoNumbersEntry class is the subclass of NumbersEntry.
 * It provides AutoNumbersEntry used in the LuckyNumbersCompetition when generate auto entries.
 */
public class AutoNumbersEntry extends NumbersEntry {
    /*
    @param NUMBER_COUNT constant represents number count
    @param MAX_NUMBER   constant represents max number
     */
    private final int NUMBER_COUNT = 7;
    private final int MAX_NUMBER = 35;

    /**
     * This method is used to create auto entry numbers by using seed in Testing mode.
     *
     * @param seed create random numbers by using seed
     */
    public void createNumbers (int seed) {
        ArrayList<Integer> validList = new ArrayList<>();
        int[] tempNumbers = new int[NUMBER_COUNT];
        for (int i = 1; i <= MAX_NUMBER; i++) {
            validList.add(i);
        }
        Collections.shuffle(validList, new Random(seed));
        for (int i = 0; i < NUMBER_COUNT; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        this.setNumbers(tempNumbers);
    }

    /**
     * This method overloads createNumbers function to create auto entry numbers in Normal mode.
     */
    public void createNumbers() {
        ArrayList<Integer> validList = new ArrayList<>();
        int[] tempNumbers = new int[NUMBER_COUNT];
        for (int i = 1; i <= MAX_NUMBER; i++) {
            validList.add(i);
        }
        Collections.shuffle(validList, new Random());
        for (int i = 0; i < NUMBER_COUNT; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        this.setNumbers(tempNumbers);
    }
}
