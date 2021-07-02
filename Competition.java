import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

/**
 * This abstract class is the superclass of LuckyNumbersCompetition and RandomPickCompetition.
 * It also uses several HashMap data structure to store some important data.
 * 1.memberMap stores every bill's id which user input and its corresponding member id in bill file.
 * 2.billMap stores every entry's id and the id of the bill which it belongs.
 * 3.winnerMap stores winner entries' id and its prizes.
 *
 * @see LuckyNumbersCompetition
 * @see RandomPickCompetition
 */
public abstract class Competition implements Serializable {
    /*
    @param name          competition name
    @param id            competition identifier
    @param entryID       number of entries created
    @param startID       first entryID for current entry set

    @param winnerEntry   winner entry for this competition
    @param winnerNum     number of winning entries
    @param totalPrizes   total awarded prizes

    @param memberMap     HashMap<String Bill ID, String Member ID> stores members and billID
    @param billMap       HashMap<Integer Entry ID, String Bill ID> stores bills and entryID
    @param winnerMap     HashMap<Integer Entry ID, Integer prize> stores winner of each entry
     */
    private String name;
    private int id;
    private int entryID = 0;
    private int startID = 1;

    private int[] winnerEntry = null;
    private int winnerNum = 0;
    private int totalPrizes = 0;

    public static Map<String, String> memberMap = new HashMap<>();
    Map<Integer, String> billMap = new HashMap<>();
    Map<Integer, Integer> winnerMap = new HashMap<>();

    //getters and setters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setEntryID(int entryID) {
        this.entryID = entryID;
    }

    public int getEntryID() {
        return entryID;
    }

    public void setStartID(int startID) {
        this.startID = startID;
    }

    public int getStartID() {
        return startID;
    }

    public void setWinnerEntry(int[] winnerEntry) {
        this.winnerEntry = winnerEntry;
    }

    public int[] getWinnerEntry() {
        return winnerEntry;
    }

    public void setWinnerNum(int winnerNum) {
        this.winnerNum = winnerNum;
    }

    public int getWinnerNum() {
        return winnerNum;
    }

    public int getTotalPrizes() {
        return totalPrizes;
    }

    public void setTotalPrizes(int totalPrizes) {
        this.totalPrizes = totalPrizes;
    }

    /**
     * This protected method prints information of current competition including Competition ID,
     * Competition Name and its type.
     */
    protected void printCompetitionInfo() {
        System.out.printf("Competition ID: %d, Competition Name: %s, Type: %s\n",
                this.getId(), this.getName(), this.getClass().getName());
    }

    /**
     * This protected method is designed to check whether a string is composed entirely of numbers.
     *
     * @param str String to be checked
     * @return if str is composed entirely of numbers then return true and return false conversely.
     */
    protected boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * This protected method is designed to ask if user wants to add more entries.
     *
     * @param keyboard Scanner object to work with system input stream
     * @param dataProvider DataProvider object to work with data files
     * @param mode mode of current Competition object
     */
    protected void addMoreEntries(Scanner keyboard, DataProvider dataProvider, char mode) {
        System.out.println("Add more entries (Y/N)?");
        char choice = keyboard.next().toUpperCase().charAt(0);
        switch (choice) {
            case 'Y':
                this.handleEntry(keyboard, dataProvider, mode);
                break;
            case 'N':
                break;
            default:
                System.out.println("Unsupported option. Please try again!");
                this.addMoreEntries(keyboard, dataProvider, mode);
                break;
        }
    }

    /**
     * This method is designed to ask user to input competition name.
     *
     * @param keyboard Scanner object to work with system input stream
     */
    public void handleCompetition(Scanner keyboard) {
        System.out.println("Competition name: ");
        this.setName(keyboard.nextLine());
        System.out.println("A new competition has been created!");
    }

    /**
     * This method is designed to ask user to creat entries. User should enter a valid id of a bill
     * which existed in bill file. If everything works well, the program will store bill's id and
     * automatically search for its corresponding member's id through a DataProvider object which
     * generated at the beginning of the program.
     *
     * @param keyboard Scanner object to work with system input stream
     * @param dataProvider DataProvider object to work with data files
     * @param mode mode of current Competition object
     * @see DataProvider
     */
    public void handleEntry(Scanner keyboard, DataProvider dataProvider, char mode) {
        //loop to take and check Bill ID
        String billID;
        while (true) {
            System.out.println("Bill ID: ");
            billID = keyboard.next();
            keyboard.nextLine();

            if (billID.trim().length() != 6 || !this.isNumeric(billID)) {
                System.out.println("Invalid bill id! It must be a 6-digit number. " +
                        "Please try again.");
            } else if (dataProvider.getBill(billID) == null) {
                System.out.println("This bill does not exist. Please try again.");
            } else if (dataProvider.getBill(billID).getMemberId().equals("")) {
                System.out.println("This bill has no member id. Please try again.");
            } else if (dataProvider.usedBillId.contains(billID) ||
                    dataProvider.getBill(billID).isUsedOrNot()) {
                System.out.println("This bill has already been used for a competition. " +
                        "Please try again.");
            } else {
                //store Bill ID and Member ID
                memberMap.put(billID, dataProvider.getBill(billID).getMemberId());
                dataProvider.usedBillId.add(billID);
                break;
            }
        }

        //get total amount from DataProvider object and check its legality
        double totalAmount = dataProvider.getBill(billID).getTotalAmount();
        if (totalAmount < 50) {
            System.out.println("This bill is not eligible for an entry. " +
                    "The total amount is smaller than $50.0");
            this.addMoreEntries(keyboard, dataProvider, mode);
        } else {
            this.addEntries(keyboard, dataProvider, billID, totalAmount, mode);
        }
    }

    /**
     * This abstract method is used to add entries in the current competition.
     *
     * @param keyboard Scanner object to work with system input stream
     * @param dataProvider DataProvider object to work with data files
     * @param billID Bill ID to participate in the competition
     * @param totalAmount total amount of the bill which participating in the competition
     * @param mode mode of current Competition object
     */
    public abstract void addEntries(Scanner keyboard, DataProvider dataProvider, String billID,
                                    double totalAmount, char mode);

    /**
     * This abstract method is used to draw winners in the current competition.
     *
     * @param dataProvider DataProvider object to work with data files
     * @param mode mode of current Competition object
     */
    public abstract void drawWinners(DataProvider dataProvider, char mode);
}
