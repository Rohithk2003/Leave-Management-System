package userWindow;

public class leaveModel {
    private String name;
    private String userId;
    private int leaveId;
    private String to;
    private String from;
    private String status;
    private String typeOfLeave;
    private String proofLocation;

    public leaveModel() {
        this.name = "";
        this.userId = "";
        this.leaveId = 0;
        this.to = "";
        this.from = "";
        this.status = "";
        this.typeOfLeave = "";
        this.proofLocation = "";
    }

    public String getProofLocation() {
        return proofLocation;
    }

    public void setProofLocation(String proofLocation) {
        this.proofLocation = proofLocation;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeOfLeave() {
        return typeOfLeave;
    }

    public void setTypeOfLeave(String typeOfLeave) {
        this.typeOfLeave = typeOfLeave;
    }
}
