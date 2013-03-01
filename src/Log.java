import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Log {
    FileWriter logFile;
    BufferedWriter writer;

    protected Log() {
        try {
          logFile = new FileWriter("files/log", true);
           writer = new BufferedWriter(logFile);
        } catch (IOException e) {
            System.err.println("[LOGGER] Unable to initiate.");
            System.exit(1);
        }

        writeEvent("System started.");
    }

    protected void writeJournalAccess(String user, String patient, String action, Boolean allowed) {
        try {
            writer.write("[" + System.currentTimeMillis() + "] User \"" + user + "\" requested to \"" + action + "\" the journal for patient \"" + patient + "\". Allowed: " + allowed + ".");
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("[LOGGER] Unable to write to log-file.");
            System.exit(1);
        }
    }

    protected void writeEvent(String message) {
        try {
            writer.write("[" + System.currentTimeMillis() + "] " + message);
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