import java.io.Serializable;
import java.util.*;

/**
 * The LuckyNumbersCompetition class is the subclass of Competition. It is the class to handle
 * LuckyNumbersCompetition type competitions. In a LuckyNumbersCompetition, user could add entries
 * automatically or manually. The number of entries is decided by the total amount of the bill.
 * Each entry contains 7 integer numbers which are all in range of 1 to 35. For manual entries,
 * the program creates objects of NumbersEntry class. For auto entries, the program creates objects
 * of AutoNumberEntry class which is child class of NumbersEntry class.
 *
 * To find winners, in a LuckyNumbersCompetition, the system automatically generates a lucky
 * entry that contains 7 lucky numbers. If an entry shares at least two numbers in common with
 * the lucky entry, the owner of that entry would get some prize.
 *
 * @see NumbersEntry
 * @see AutoNumbersEntry
 */
public class LuckyNumbersCompetition extends Competition implements Serializable {
    /*
    @param entriesMap    HashMap<Integer Entry ID, int[] numbers> stores entries
    @param manualAutoMap HashMap<Integer Entry ID, Integer status> stores manual(1) and auto(0) entry
     */
    Map<Integer, int[]> entriesMap = new HashMap<>();
    Map<Integer, Integer> manualAutoMap = new HashMap<>();

    /**
     * This method is used to generate new objects of NumbersEntry class.
     *
     * @return an object of NumbersEntry class
     */
    private NumbersEntry addNewEntry() {
        NumbersEntry newEntry = new NumbersEntry();
        this.setEntryID(this.getEntryID() + 1);
        return newEntry;
    }

    /**
     * This method is used to generate new objects of AutoNumbersEntry class.
     *
     * @return an object of AutoNumbersEntry class
     */
    private AutoNumbersEntry addNewAutoEntry() {
        AutoNumbersEntry newAutoEntry = new AutoNumbersEntry();
        this.setEntryID(this.getEntryID() + 1);
        return newAutoEntry;
    }

    /**
     * This method converts a String which is composed entirely of numbers into an Array.
     *
     * @param str String to be converted
     * @return Integer Array
     */
    private int[] strToArray(String str) {
        ArrayList<Integer> validList = new ArrayList<>();
        String[] tempStr = str.split(" ");
        for (String s : tempStr) {
            validList.add(Integer.parseInt(s));
        }

        int[] tempNumber = new int[validList.size()];
        for (int i = 0; i < validList.size(); i++) {
            tempNumber[i] = validList.get(i);
        }
        Arrays.sort(tempNumber);
        return tempNumber;
    }

    /**
     * This method is designed to check whether an array has duplicated numbers.
     *
     * @param num Integer Array to be checked
     * @return whether the Array contains duplicated numbers or not
     */
    private boolean isDuplicated(int[] num) {
        for (int i = 0; i < num.length; i++) {
            int temp = num[i];
            for (int j = i + 1; j < num.length; j++) {
                if (num[j] == temp) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method is designed to check whether all numbers are integer between 1 and 35.
     *
     * @param num Integer Array to be checked
     * @return whether all numbers in the Array are in range or not
     */
    private boolean numbersInRange(int[] num) {
        for (int i : num) {
            if (i < 1 || i > 35) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to print all entries which are created in current competition.
     *
     * @param entriesMap HashMap<Integer Entry ID, int[] numbers> to store entries
     * @param manualEntries numbers of manually generated entries
     * @param entryNum total numbers of entries
     * @param startID first entryID for current entry set
     */
    private void printEntries(Map<Integer, int[]> entriesMap, int manualEntries,
                              int entryNum, int startID) {
        System.out.println("The following entries have been added:");
        for (int i = startID; i < startID + manualEntries; i++) {
            System.out.printf("Entry ID: %-7dNumbers:", i);
            for (int m = 0; m < entriesMap.get(i).length; m++) {
                System.out.printf("%3d", entriesMap.get(i)[m]);
            }
            System.out.println();
        }
        for (int j = startID + manualEntries; j < startID + entryNum; j++) {
            System.out.printf("Entry ID: %-7dNumbers:", j);
            for (int m = 0; m < entriesMap.get(j).length; m++) {
                System.out.printf("%3d", entriesMap.get(j)[m]);
            }
            System.out.println(" [Auto]");
        }
    }

    /**
     * This method overrides the abstract methods in the parent class Competition.
     *
     * @param keyboard Scanner object to work with system input stream
     * @param dataProvider DataProvider object to work with data files
     * @param billID Bill ID to participate in the competition
     * @param totalAmount total amount of the bill which participating in the competition
     * @param mode mode of current Competition object
     * @see Competition
     */
    @Override
    public void addEntries(Scanner keyboard, DataProvider dataProvider, String billID,
                           double totalAmount, char mode) {
        int entryNum = (int)(totalAmount / 50); //double to int
        int manualEntries; //number of manual entries

        System.out.printf("This bill ($%s) is eligible for %d entries. " +
                        "How many manual entries did the customer fill up?: \n",
                dataProvider.getBill(billID).getTotalAmount(), entryNum);

        //loop to check the quantity of manual entries
        while (true) {
            String manualEntryNum = keyboard.next();
            keyboard.nextLine();

            if (!this.isNumeric(manualEntryNum)) {
                System.out.println("Invalid input! Numbers are expected. Please try again!");
            } else {
                manualEntries = Integer.parseInt(manualEntryNum);

                //check whether number of manual entries is larger than number of all entries
                if (manualEntries <= entryNum) {
                    break;
                } else {
                    System.out.printf("The number must be in the range from 0 to %d. " +
                            "Please try again.\n", entryNum);
                }
            }
        }

        //loop to add manual entries
        int loop1 = 0;
        while (loop1 < manualEntries) { //loop for proper times
            while (true) {
                System.out.println("Please enter 7 different numbers (from the range 1 to 35) " +
                        "separated by whitespace.");
                String tempStr = keyboard.nextLine().trim(); //delete whitespaces at the begin and end

                //first check whether the string is composed entirely of numbers
                try {
                    if (!super.isNumeric(tempStr.replace(" ", ""))) {
                        System.out.println("Invalid input! Numbers are expected. " +
                                "Please try again!");
                    } else {
                        int[] tempNumbers = this.strToArray(tempStr); //string to array
                        if (tempNumbers.length < 7) {
                            System.out.println("Invalid input! Fewer than 7 numbers are provided." +
                                    " Please try again!");
                        } else if (tempNumbers.length > 7) {
                            System.out.println("Invalid input! More than 7 numbers are provided. " +
                                    "Please try again!");
                        } else if (!this.numbersInRange(tempNumbers)) {
                            System.out.println("Invalid input! All numbers must be in the " +
                                    "range from 1 to 35!");
                        } else if (this.isDuplicated(tempNumbers)) {
                            System.out.println("Invalid input! All numbers must be different!");
                        } else {
                            NumbersEntry newEntry = this.addNewEntry();
                            newEntry.setEntryId(this.getEntryID());
                            newEntry.setNumbers(tempNumbers);
                            entriesMap.put(newEntry.getEntryId(), newEntry.getNumbers()); //store entryID and numbers
                            billMap.put(this.getEntryID(), billID); //store entryID and Bill ID
                            manualAutoMap.put(this.getEntryID(), 1); //mark this entry as a manual one
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input! Different numbers should be separated by " +
                            "one whitespace only. Please try again!");
                }
            }
            loop1 += 1;
        }

        //loop to add auto entries
        int loop2 = 0;
        while (loop2 < entryNum - manualEntries) { //loop for proper times
            AutoNumbersEntry newAutoEntry = this.addNewAutoEntry();
            newAutoEntry.setEntryId(this.getEntryID());

            //check program running mode
            if (mode == 'T') {
                newAutoEntry.createNumbers(newAutoEntry.getEntryId() - 1);
            } else {
                newAutoEntry.createNumbers();
            }

            //store things
            entriesMap.put(newAutoEntry.getEntryId(), newAutoEntry.getNumbers());
            billMap.put(this.getEntryID(), billID);
            manualAutoMap.put(this.getEntryID(), 0);
            loop2 += 1;
        }
        this.printEntries(entriesMap, manualEntries, entryNum, this.getStartID()); //print entries
        this.setStartID(this.getStartID() + entryNum);
        this.addMoreEntries(keyboard, dataProvider, mode);
    }

    /**
     * This method is designed to calculate the prize with the winner entry and a normal entry.
     * Here are different levels of prizes.
     * 1.7 numbers in common: 50000 points
     * 2.6 numbers in common: 5000  points
     * 3.5 numbers in common: 1000  points
     * 4.4 numbers in common: 500   points
     * 5.3 numbers in common: 100   points
     * 6.2 numbers in common: 50    points
     *
     * @param winner numbers array of the winner entry
     * @param normal numbers array of the normal entry
     * @return prize of the normal entry
     */
    private int prize(int[] winner,int[] normal) {
        int count = 0; //number of same numbers
        int prize = 0;

        //count number of same item
        for (int k : winner) {
            for (int i : normal) {
                if (k == i) {
                    count += 1;
                }
            }
        }

        //convert count to prize
        switch (count) {
            case 7 -> prize = 50000;
            case 6 -> prize = 5000;
            case 5 -> prize = 1000;
            case 4 -> prize = 500;
            case 3 -> prize = 100;
            case 2 -> prize = 50;
        }
        return prize;
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
        AutoNumbersEntry winnerAutoEntry = new AutoNumbersEntry();

        if (mode == 'T') {
            winnerAutoEntry.createNumbers(this.getId());
        } else {
            winnerAutoEntry.createNumbers();
        }

        //print winner entry
        this.setWinnerEntry(winnerAutoEntry.getNumbers());
        System.out.print("Lucky Numbers:");
        for (int i : this.getWinnerEntry()) {
            System.out.printf("%3d", i);
        }
        System.out.println(" [Auto]");

        //print entries which win prize
        System.out.println("Winning entries:");
        //put the prize of every entry into winnerMap
        for (int i = 1; i <= entriesMap.size(); i++) {
            int prize = this.prize(this.getWinnerEntry(), entriesMap.get(i));
            winnerMap.put(i, prize);
        }

        //update winnerMap
        //every entry keeps the largest prize only
        for (int i = 1; i <= winnerMap.size(); i++) {
            int temPrize = winnerMap.get(i);
            for (int j = i + 1; j <= winnerMap.size(); j++) {
                if (dataProvider.getBill(billMap.get(j)).getMemberId().equals(
                        dataProvider.getBill(billMap.get(i)).getMemberId())) {
                    if (temPrize >= winnerMap.get(j)) {
                        winnerMap.replace(j, 0);
                    } else {
                        winnerMap.replace(i, 0);
                    }
                }
            }
        }

        //print winner entries
        for (int i = 1; i <= winnerMap.size(); i++) {
            if (winnerMap.get(i) > 0) {
                this.setWinnerNum(this.getWinnerNum() + 1);
                this.setTotalPrizes(this.getTotalPrizes() + winnerMap.get(i));

                String billID = billMap.get(i);
                String memberID = dataProvider.getBill(billID).getMemberId();
                System.out.printf("Member ID: %s, Member Name: %s, Prize: %-5d\n", memberID,
                        dataProvider.getMember(memberID).getMemberName(), winnerMap.get(i));
                System.out.printf("--> Entry ID: %d, Numbers:", i);
                for (int j = 0; j < entriesMap.get(i).length; j++) {
                    System.out.printf("%3d", entriesMap.get(i)[j]);
                }
                if (manualAutoMap.get(i) == 0) {
                    System.out.println(" [Auto]");
                } else {
                    System.out.println();
                }
            }
        }
    }
}
