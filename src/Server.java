import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.security.cert.X509Certificate;
import java.io.*;

public class Server extends Communicator {
    private Log log = new Log();

    private Server() {
        System.setProperty("javax.net.ssl.keyStore", "ssl/server_keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "server_keystore");
        System.setProperty("javax.net.ssl.trustStore", "ssl/server_truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "server_truststore");

        Database database = loadDatabase();

//        Database database = new Database();
//        database.addUser(new User("BobKelso", User.DOCTOR, 1));
//        database.addUser(new User("Carla", User.NURSE, 1));
//        database.addUser(new User("DrCox", User.DOCTOR, 2));
//        database.addUser(new User("Laverne", User.NURSE, 2));
//        database.addUser(new User("Steve", User.PATIENT));
//        database.addUser(new User("Buck", User.PATIENT));
//        database.addUser(new User("Obama", User.GOVERNMENT));
//        saveDatabase(database);

        InputStream inputStream;
        OutputStream outputStream;

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = null;

        try {
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(1337);
        } catch (IOException e) {
            System.err.println("Unable to initiate SSLServerSocket.");
            System.exit(1);
        }

        while (true) {
            try {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                sslSocket.setNeedClientAuth(true);
                SSLSession sslSession = sslSocket.getSession();
                X509Certificate x509Certificate = sslSession.getPeerCertificateChain()[0];
                String username = x509Certificate.getSubjectDN().getName().split("CN=")[1].split(",")[0];

                inputStream = sslSocket.getInputStream();
                outputStream = sslSocket.getOutputStream();

                User user = database.getUser(username);

                if (user != null) {
                    send("Welcome " + user.username() + ", you are authenticated!", outputStream);
                } else {
                    send("Username is not valid. Connection will be closed.", outputStream);
                    sslSocket.close();
                    sslSession.invalidate();
                }

                String[] message;
                int command;

                while (true) {
                    message = receive(inputStream).split(" ");
                    try {
                        switch (Integer.parseInt(message[0])) {
                            case 0:
                                send("Connection terminated.", outputStream);
                                log.writeEvent("User \"" + username + "\" signed off.");
                                sslSession.invalidate();
                                sslSocket.close();
                                break;
                            case 1:
                                Journal journal = database.requestJournal(user, message[1]);
                                if (journal != null) {
                                    log.writeJournalAccess(user.username(), message[1], "read", true);
                                    send("Journal data: " + journal.data(), outputStream);
                                } else {
                                    log.writeJournalAccess(user.username(), message[1], "read", false);
                                    send("Unable to get journal.", outputStream);
                                }
                                break;
                            case 2:
                                if (database.writeJournal(user, new Journal(message[1], message[2], message[3], message[4], Integer.parseInt(message[5])))) {
                                    log.writeJournalAccess(user.username(), message[2], "write", true);
                                    send("Journal created.", outputStream);
                                } else {
                                    log.writeJournalAccess(user.username(), message[2], "write", true);
                                    send("Journal not created.", outputStream);
                                }
                                break;
                            case 3:
                                if (database.deleteJournal(user, message[1])) {
                                    log.writeJournalAccess(user.username(), message[1], "delete", true);
                                    send("Journal deleted.", outputStream);
                                } else {
                                    log.writeJournalAccess(user.username(), message[1], "delete", false);
                                    send("Journal not deleted.", outputStream);
                                }
                                break;
                            default:
                                send("Invalid command.", outputStream);
                                break;
                        }
                    } catch (NumberFormatException e) {
                        send("Invalid command.", outputStream);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        send("Invalid command.", outputStream);
                    }
                }
            } catch (IOException e) {
                saveDatabase(database);
                System.err.println("Connection closed. Restarting.");
            }
        }
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
            log.writeEvent("Unable to load database.");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            log.writeEvent("Unable to load database.");
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            log.writeEvent("Unable to load database.");
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
            log.writeEvent("Unable to save database.");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            log.writeEvent("Unable to save database.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}