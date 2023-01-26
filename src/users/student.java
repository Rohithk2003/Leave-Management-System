package users;

public class student {
    protected String name;
    protected String id;
    protected String address;
    protected int secId;
    protected int depId;

    public student() {
        name = "";
        address = "";
        depId = 0;
        id = "";
        secId = 0;
        id = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getDepId() {
        return depId;
    }

    public void setDepId(int depId) {
        this.depId = depId;
    }


}
