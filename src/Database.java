import java.io.Serializable;
import java.util.HashMap;

public class Database implements Serializable {
    private static final long serialVersionUID = 100;
    private HashMap<String, User> users = new HashMap<String, User>();
    private HashMap<String, Journal> journals = new HashMap<String, Journal>();

    protected Database() {
    }

    protected User getUser(String username) {
        return users.get(username);
    }

    protected void addUser(User user) {
        users.put(user.username(), user);
    }

    protected Journal requestJournal(User user, String patientName) {
        Journal journal = journals.get(patientName);

        if (journal != null && (
            (user.username().equals(patientName)) ||
            (user.isNurse() && (journal.nurse().equals(user.username()) || user.division() == journal.division() && journal.division() >= 0)) ||
            (user.isDoctor() && (user.username().equals(journal.doctor()) || (user.division() == journal.division() && journal.division() >= 0))) ||
            (user.isGovernment())
            )
        ) {
            return journal;
        } else {
            return null;
        }
    }

    protected boolean writeJournal(User user, Journal journal) {
        if (
            journals.containsKey(journal.patient()) ||
            (user.isNurse() && user.division() == journal.division() && journal.division() >= 0) ||
            (user.isDoctor() && (user.username().equals(journal.doctor()) || (user.division() == journal.division() && journal.division() >= 0)))
        ) {
            journals.put(journal.patient(), journal);
            return true;
        } else {
            return false;
        }
    }

    protected boolean deleteJournal(User user, String patientName) {
        Journal journal = journals.get(patientName);
        return journal != null && user.isGovernment() && journals.remove(patientName) != null;
    }
}