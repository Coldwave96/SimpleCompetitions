import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This DataProvider class works with data files provided by user. It loads data from the files
 * and stores in HashMap. HashMap members stores every member's id and its Member object.
 * HashMap bills stores every bill's id and its Bill object.
 *
 * It also offers function to update bill file. After user saving competitions into a local file,
 * the program will automatically change status of those bills which are used in the competitions
 * to "true". Thus these bill will not be able to used again if user loads the local binary file.
 *
 * @see Member
 * @see Bill
 */
public class DataProvider implements Serializable {
    /*
    @param memberFile path to the member file
    @param billFile   path to the bill file

    @param members    HashMap<String Member ID, Member Member class> stores member data
    @param bills      HashMap<String Bill ID, Bill Bill class> stores bill data

    @param usedBillId ArrayList stores Bill ID which have been used in previous competition
     */
    private String memberFile;
    private String billFile;

    Map<String, Member> members = new HashMap<>();
    Map<String, Bill> bills = new HashMap<>();

    ArrayList<String> usedBillId = new ArrayList<>();

    /**
     * DataProvider Constructor to reads data from the file and stores in HashMap when initializing.
     *
     * @param memberFile A path to the member file (e.g., members.csv)
     * @param billFile A path to the bill file (e.g., bills.csv)
     * @exception DataAccessException handle file cannot be opened or read error
     * @exception DataFormatException handle format of the content incorrect error
     */
    public DataProvider(String memberFile, String billFile) {
        this.setMemberFile(memberFile);
        this.setBillFile(billFile);

        try {
            //read member data from member file
            try {
                File newMemberFile = new File(this.getMemberFile());
                Scanner inputMemberStream = new Scanner(new FileInputStream(newMemberFile));

                while (inputMemberStream.hasNextLine()) {
                    String[] aMember = inputMemberStream.nextLine().split(",");
                    String memberId = aMember[0];
                    String memberName = aMember[1];
                    String address = aMember[2];

                     //check whether Member ID is valid or not
                    try {
                        Integer.valueOf(memberId);
                    } catch (NumberFormatException e) {
                        throw new DataFormatException();
                    }

                    if (memberId.length() == 6) {
                        Member newMember = new Member();
                        newMember.setMemberId(memberId);
                        newMember.setMemberName(memberName);
                        newMember.setAddress(address);
                        members.put(memberId, newMember);
                    } else {
                        throw new DataFormatException();
                    }
                }
                inputMemberStream.close();
            } catch (FileNotFoundException e) {
                throw new DataAccessException();
            } catch (Exception e) {
                throw new DataFormatException();
            }

            //read bill data from bill file
            try {
                File newBillFile = new File(this.getBillFile());
                Scanner inputBillStream = new Scanner(new FileInputStream(newBillFile));
                int index = 0;

                while (inputBillStream.hasNextLine()) {
                    String[] aBill = inputBillStream.nextLine().split(",");
                    String billId = aBill[0];
                    String memberId = aBill[1];
                    String totalAmount = aBill[2];
                    String usedOrNot = aBill[3];
                    index += 1;

                    //check whether Member ID is valid or not
                    if (!this.isNumeric(memberId)) {
                        throw new DataFormatException();
                    }

                    //check whether boolean value is valid or not
                    if (!this.isBoolean(usedOrNot)) {
                        throw new DataFormatException();
                    }

                    //check whether Bill ID and total amount is valid or not
                    try {
                        Integer.valueOf(billId);
                        Double.valueOf(totalAmount);
                    } catch (NumberFormatException e) {
                        throw new DataFormatException();
                    }

                    if (billId.length() == 6 && (memberId.length() == 6 ||
                            memberId.length() == 0)) {
                        Bill newBill = new Bill();
                        newBill.setBillId(billId);
                        newBill.setMemberId(memberId);
                        newBill.setTotalAmount(Double.parseDouble(totalAmount));
                        newBill.setUsedOrNot(Boolean.parseBoolean(usedOrNot));
                        bills.put(billId, newBill);
                        newBill.setId(index);
                    } else {
                        throw new DataFormatException();
                    }
                }
                inputBillStream.close();
            } catch (FileNotFoundException e) {
                throw new DataAccessException();
            } catch (Exception e) {
                throw new DataFormatException();
            }
        } catch (DataAccessException | DataFormatException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    //setters and getters
    public void setMemberFile(String memberFile) {
        this.memberFile = memberFile;
    }

    public String getMemberFile() {
        return memberFile;
    }

    public void setBillFile(String billFile) {
        this.billFile = billFile;
    }

    public String getBillFile() {
        return billFile;
    }

    public Member getMember(String id) {
        return this.members.get(id);
    }

    public Bill getBill(String id) {
        return this.bills.get(id);
    }

    /**
     * This method is designed to check whether a string is composed entirely of numbers.
     *
     * @param str String to be checked
     * @return if str is composed entirely of numbers then return true.
     */
    private boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * This method is designed to check whether a string is boolean value.
     *
     * @param str String to be checked
     * @return if str is boolean value return true
     */
    private boolean isBoolean(String str) {
        return "true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str);
    }

    /**
     * This method is designed to update status of some bills which have been used in previous competition.
     */
    public void updateBillFile() {
         try {
             FileInputStream fileIn = new FileInputStream(this.billFile);
             FileWriter out = null;
             CharArrayWriter tempStream = new CharArrayWriter();

             //read bill file line by line and track index of each line
             try (LineNumberReader lineReader =
                          new LineNumberReader(new InputStreamReader(fileIn))) {
                 String line;
                 while ((line = lineReader.readLine()) != null) {
                     //HashMap<Integer id, String Bill ID> stores used Bill ID and Bill class id
                     Map<Integer, String> ids = new HashMap<>();
                     for (String i : this.usedBillId) {
                         ids.put(getBill(i).getId(), i);
                     }

                     //check whether current line index is in the ids
                     //locate the line index of used bill then change status
                     if (ids.get(lineReader.getLineNumber()) != null) {
                         line = line.replace("false", "true");
                     }
                     tempStream.write(line);
                     tempStream.append(System.getProperty("line.separator"));
                 }
                 //write new bill data into bill file
                 out = new FileWriter(this.billFile);
                 tempStream.writeTo(out);
                 out.flush();
             } catch (Exception e) {
                 System.out.println("Something wrong happened while updating bill file.");
                 System.exit(0);
             } finally {
                 fileIn.close();
                 assert out != null;
                 out.close();
                 tempStream.close();
             }
         } catch (Exception e) {
             System.out.println(e.getMessage());
             System.exit(0);
         }
    }
}
