import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Communicator {
    byte[] data = new byte[100];
    int bytesLeft,
        bytesExpected,
        chunkReceived;

    protected void send(String message, OutputStream outputStream) throws IOException {
        if (message.length() > 100)
            throw new IndexOutOfBoundsException("Message length > 100.");

        char[] messageArray = message.toCharArray();

        for (int i = 0; i < message.length(); i++) {
            data[i] = (byte) messageArray[i];
        }

        for (int i = message.length(); i < 100; i++) {
            data[i] = 32;
        }

        outputStream.write(data);
    }

    protected String receive(InputStream inputStream) throws IOException {
        bytesLeft = bytesExpected = 100;

        while (bytesLeft > 0) {
            chunkReceived = inputStream.read(data, bytesExpected - bytesLeft, bytesLeft);
            if (chunkReceived == -1) {
                throw new IOException("Datastream closed.");
            } else {
                bytesLeft -= chunkReceived;
            }
        }

        return new String(data).trim();
    }
}