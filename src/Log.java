import java.io.*;

public class Log {
    FileWriter logFile;
    BufferedWriter writer;

    protected final static int
        SYSTEM_START = 0,
        SYSTEM_END = 1,
        DATABASE_UPSTART_ERROR = 2;

    protected Log() {
        try {
          logFile = new FileWriter("files/log", true);
           writer = new BufferedWriter(logFile);
        } catch (IOException e) {
            System.err.println("[LOGGER] Unable to initiate.");
            System.exit(1);
        }

        writeEvent(SYSTEM_START);
    }

    protected void writeEvent(int eventId) {
        writeEvent(eventId, "SYSTEM");
    }

    protected void writeEvent(int eventId, String username) {
        try {
            String message = "";
            switch (eventId) {
                case SYSTEM_START:
                    message = "Logger started.";
                    break;
                case SYSTEM_END:
                    message = "Logger closed.";
                    break;
                case DATABASE_UPSTART_ERROR:
                    message = "Database couldn't be started.";
                    break;
                default:
                    System.err.println("[LOGGER] Illegal eventID in writeEvent: " + eventId + ".");
                    System.exit(1);
                    break;
            }
            writer.write("[" + System.currentTimeMillis() + "][" + username + "] " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("[LOGGER] Unable to write to log-file.");
            System.exit(1);
        }
    }

    protected void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("[LOGGER] Unable to close writer.");
            System.exit(1);
        }
    }
}
