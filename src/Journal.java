import java.io.Serializable;

public class Journal implements Serializable {
    private static final long serialVersionUID = 200;
    private int division = -1;
    private final String patient;
    private String
        doctor,
        nurse,
        data;

    protected Journal(String data, String patient, String doctor, String nurse, int division) {
        this.data = data;
        this.patient = patient;
        this.nurse = nurse;
        this.doctor = doctor;
        this.division = division;
    }

    protected String patient() {
        return patient;
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

    protected String data() {
        return data;
    }
}