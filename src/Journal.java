import java.io.Serializable;
import java.util.HashSet;

public class Journal implements Serializable {
    private static final long serialVersionUID = 200;
    private HashSet<String> accessList = new HashSet<String>();
    private final String patient;
    private int division = -1;
    private String
        doctor = "",
        nurse = "";

    protected Journal(String patient) {
        this.patient = patient;
    }

    protected String patient() {
        return patient();
    }

    protected int division() {
        return division;
    }

    protected String doctor() {
        return doctor;
    }

    protected String nurse() {
        return nurse;
    }
}