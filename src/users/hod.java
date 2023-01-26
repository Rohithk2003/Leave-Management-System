package users;

public class hod {
    protected String name;
    protected String hodId;
    protected String address;
    protected int depId;

    public hod() {
        name = "";
        address = "";
        depId = 0;
        hodId = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHodId() {
        return hodId;
    }

    public void setHodId(String hodId) {
        this.hodId = hodId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDepId() {
        return depId;
    }

    public void setDepId(int depId) {
        this.depId = depId;
    }


}