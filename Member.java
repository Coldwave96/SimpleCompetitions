/**
 * This Member class provides basic structure of a Member.
 * Each member has a member identifier, a name and an email address.
 */
public class Member {
    /*
    @param memberId   Member ID
    @param memberName Member name
    @param address    Member address
     */
    private String memberId;
    private String memberName;
    private String address;

    //setters and getters
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
