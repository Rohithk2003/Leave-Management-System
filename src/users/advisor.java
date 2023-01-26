package users;

public class advisor {
    protected String name;
    protected String advisorId;
    protected String address;
    protected int secId;

    public advisor() {
        name = "";
        advisorId = "";
        address = "";
        secId = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }


}