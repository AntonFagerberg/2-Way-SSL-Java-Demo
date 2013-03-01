import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 200;
    final static int
        PATIENT = 0,
        NURSE = 1,
        DOCTOR = 2,
        GOVERNMENT = 3;

    private String username;
    private int division,
        userType;

    protected User(String username, int userType) {
        this(username, userType, -1);
    }

    protected User(String username, int userType, int division) {
        if (username == "") {
            System.err.println("[USER] Username can't be blank.");
            System.exit(1);
        }

        switch(userType) {
            case PATIENT:
            case NURSE:
            case DOCTOR:
            case GOVERNMENT:
                this.userType = userType;
                break;
            default:
                System.err.println("[USER] Illegal user-type: " + userType + ".");
                System.exit(1);
                break;
        }

        this.username = username;
        this.division = division;
    }

    protected boolean isPatient() {
        return userType == PATIENT;
    }

    protected boolean isNurse() {
        return userType == NURSE;
    }

    protected boolean isDoctor() {
        return userType == DOCTOR;
    }

    protected boolean isGovernment() {
        return userType == GOVERNMENT;
    }

    protected String username() {
        return username;
    }

    protected int division() {
        return (isNurse() || isDoctor()) ? division : -1;
    }
}