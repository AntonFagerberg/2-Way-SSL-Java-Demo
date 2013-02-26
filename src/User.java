import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 200;
    final static int
        PATIENT = 0,
        NURSE = 1,
        DOCTOR = 2,
        GOVERNMENT = 3;

    private int userType;
    private String username;
    private byte[]
        salt = PasswordProtector.generateSalt(),
        password;

    protected User(String username, char[] password, int userType) {
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
        this.password = PasswordProtector.encryptPassword(password, salt);
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
}
