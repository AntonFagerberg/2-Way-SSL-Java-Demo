import java.io.*;

public class Server {
    private Log log = new Log();

    private Server() {
        // INITATE SNIPPET
        /*
        Database database = new Database();
        database.addUser(new User("bob_kelso", "salami".toCharArray(), User.DOCTOR));
        database.addUser(new User("marcus_resell", "always_sick".toCharArray(), User.PATIENT));
        database.addUser(new User("carla", "love_turk".toCharArray(), User.NURSE));
        database.addUser(new User("obama", "thanks_obama".toCharArray(), User.GOVERNMENT));
        ///*/

        Database database = loadDatabase();
//        System.out.println(database.usernameExists("bob_kelso"));
        saveDatabase(database);
        log.close();
    }

    private Database loadDatabase() {
        return loadDatabase("files/database");
    }

    private Database loadDatabase(String fileName) {
        Database database = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            database = (Database) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            log.writeEvent(Log.DATABASE_UPSTART_ERROR);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            log.writeEvent(Log.DATABASE_UPSTART_ERROR);
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            log.writeEvent(Log.DATABASE_UPSTART_ERROR);
            e.printStackTrace();
            System.exit(1);
        }

        return database;
    }

    private void saveDatabase(Database database) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("files/database");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(database);
        } catch (FileNotFoundException e) {
            log.writeEvent(Log.DATABASE_UPSTART_ERROR);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            log.writeEvent(Log.DATABASE_UPSTART_ERROR);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
