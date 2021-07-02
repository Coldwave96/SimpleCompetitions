/**
 * This Bill class provides basic structure of a Bill.
 * Each bill has a bill identifier, identifier of the member who paid this bill, total amount of
 * this bill and status of this bill (whether this bill has been used in previous competitions).
 */
public class Bill {
    /*
    @param id          bill identifier
    @param billId      Bill ID of a bill
    @param memberId    Member ID of a bill
    @param totalAmount total amount of a bill
    @param usedOrNot   bill status
     */
    private int id;
    private String billId;
    private String memberId;
    private double totalAmount;
    private boolean usedOrNot;

    //setters and getters
    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillId() {
        return billId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setUsedOrNot(boolean usedOrNot) {
        this.usedOrNot = usedOrNot;
    }

    public boolean isUsedOrNot() {
        return usedOrNot;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
