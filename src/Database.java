import java.io.Serializable;
import java.util.HashMap;

public class Database implements Serializable {
    private static final long serialVersionUID = 100;
    private HashMap<String, User> users = new HashMap();
    private HashMap<String, Journal> journals = new HashMap<String, Journal>();

    protected Database() {
        journals.put("test", new Journal("test", new User("bob_kelso", User.DOCTOR), "carla"));
    }

    protected boolean usernameExists(String username) {
        return users.containsKey(username);
    }

    protected boolean addUser(User user) {
        if (usernameExists(user.username())) {
            return false;
        } else {
            users.put(user.username(), user);
            return false;
        }
    }

    protected Journal requestJournal(String username, String patientName) {
        Journal journal = journals.get(patientName);
        User user = users.get(username);

        if (user != null && journal != null && (
            (user.username() == patientName) ||
            (user.isNurse() && (journal.nurse() == user.username() || user.division() == journal.division() && journal.division() >= 0)) ||
            (user.isDoctor() && (user.username() == journal.doctor() || (user.division() == journal.division() && journal.division() >= 0))) ||
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
            (user.isNurse() && user.division() == journal.division() && journal.division() >= 0) ||
            (user.isDoctor() && (user.username() == journal.doctor() || (user.division() == journal.division() && journal.division() >= 0)))
        ) {
            journals.put(journal.patient(), journal);
            return true;
        } else {
            return false;
        }
    }

    protected boolean deleteJournal(User user, String patientName) {
        Journal journal = journals.get(patientName);
        if (journal != null && user.isGovernment()) {
            return journals.remove(patientName) != null;
        } else {
            return false;
        }
    }
}