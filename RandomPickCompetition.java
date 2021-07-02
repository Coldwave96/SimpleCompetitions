import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The RandomPickCompetition class is the subclass of Competition. It is the class to handle
 * RandomPickCompetition type competitions. In a RandomPickCompetition, the program will use a
 * simple algorithm to generate the entries based on thr total amount of the bill. Specifically, it
 * just generate basic entries (objects of the base Entry class) – no numbers are required.
 *
 * In a RandomPick competition, the system automatically picks 3 winning entries from the entry
 * list of the competition. The prizes for the first, second, and third winning entries are 50000
 * points, 5000 points, and 1000 points, respectively.
 *
 * @see Entry
 */
public class RandomPickCompetition extends Competition implements Serializable {
    /*
    @param FIRST_PRIZE         constant represents first level prize
    @param SECOND_PRIZE        constant represents second level prize
    @param THIRD_PRIZE         constant represents third level prize
    @param prizes              constant Array store prizes of three winning level

    @param MAX_WINNING_ENTRIES constant represents max winning entries

    @param entries             ArrayList stores new generated Entry objects
     */
    private final int FIRST_PRIZE = 50000;
    private final int SECOND_PRIZE = 5000;
    private final int THIRD_PRIZE = 1000;
    private final int[] prizes = {FIRST_PRIZE, SECOND_PRIZE, THIRD_PRIZE};
	
    private final int MAX_WINNING_ENTRIES = 3;

    ArrayList<Entry> entries = new ArrayList<>();

    /**
     * This method is used to generate new objects of Entry class.
     *
     * @return an object of Entry class
     */
    private Entry addNewEntry() {
        Entry newEntry = new Entry();
        this.setEntryID(this.getEntryID() + 1);
        entries.add(newEntry);
        return newEntry;
    }

    /**
     * This method overrides the abstract methods in the parent class Competition.
     *
     * @param keyboard Scanner object to work with system input stream
     * @param dataProvider DataProvider object to work with data files
     * @param billID Bill ID to participate in the competition
     * @param totalAmount total amount of the bill which participating in the competition
     * @param mode mode of current Competition object
     */
    @Override
    public void addEntries(Scanner keyboard, DataProvider dataProvider, String billID,
                           double totalAmount, char mode) {
        int entryNum = (int)(totalAmount / 50);

        System.out.printf("This bill ($%s) is eligible for %d entries.\n",
                dataProvider.getBill(billID).getTotalAmount(), entryNum);
        System.out.println("The following entries have been automatically generated:");

        //loop to print generated entries
        int loop = 0;
        while (loop < entryNum) {
            Entry newEntry = this.addNewEntry();
            newEntry.setEntryId(this.getEntryID());

            System.out.printf("Entry ID: %-6d\n", this.getEntryID());

            billMap.put(this.getEntryID(), billID);
            loop += 1;
        }
        this.addMoreEntries(keyboard, dataProvider, mode);
    }

    /**
     * This method overrides the abstract methods in the parent class Competition.
     *
     * @param dataProvider DataProvider object to work with data files
     * @param mode mode of current Competition object
     */
    @Override
    public void drawWinners(DataProvider dataProvider, char mode) {
        this.printCompetitionInfo();
        System.out.println("Winning entries:");

        //randomly pick three winning entries from the entry list.
        Random randomGenerator;
        if (mode == 'T') {
            randomGenerator = new Random(this.getId());
        } else {
            randomGenerator = new Random();
        }
		
        int winningEntryCount = 0;
        while (winningEntryCount < MAX_WINNING_ENTRIES) {
            int winningEntryIndex = randomGenerator.nextInt(entries.size());

            Entry winningEntry = entries.get(winningEntryIndex);
            if (winningEntry.getPrize() == 0) {
                int currentPrize = prizes[winningEntryCount];
                winningEntry.setPrize(currentPrize);
                winnerMap.put(winningEntry.getEntryId(), winningEntry.getPrize());
                winningEntryCount++;
            }
        }

        //update winnerMap
        //every entry keeps the largest prize only
        for (int i = 1; i <= entries.size(); i++) {
            if (winnerMap.get(i) != null) {
                int temPrize = winnerMap.get(i);
                for (int j = i + 1; j <= entries.size(); j++) {
                    if (winnerMap.get(j) != null) {
                        if (dataProvider.getBill(billMap.get(j)).getMemberId().equals(
                                dataProvider.getBill(billMap.get(i)).getMemberId())) {
                            if (temPrize >= winnerMap.get(j)) {
                                winnerMap.remove(j);
                            } else {
                                winnerMap.remove(i);
                            }
                        }
                    }
                }
            }
        }

        //print winner entries
        for (int i = 1; i <= entries.size(); i++) {
            if (winnerMap.get(i) != null) {
                this.setWinnerNum(this.getWinnerNum() + 1);
                this.setTotalPrizes(this.getTotalPrizes() + winnerMap.get(i));
                this.setWinnerEntry(new int[i]);

                String memberId = memberMap.get(billMap.get(i));
                String memberName = dataProvider.getMember(memberId).getMemberName();

                System.out.printf("Member ID: %s, Member Name: %s, Entry ID: %d, Prize: %-5d\n",
                        memberId, memberName, i, winnerMap.get(i));
            }
        }
    }
}
