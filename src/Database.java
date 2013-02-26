import java.io.Serializable;
import java.util.HashMap;

public class Database implements Serializable {
    private static final long serialVersionUID = 100;
    private HashMap<String, User> users = new HashMap();

    protected Database() {
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
}
