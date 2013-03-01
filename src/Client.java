import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Client extends Communicator {
    public Client() {
        Scanner scanner = new Scanner(System.in);
        String
            username,
            password,
            message;

        System.out.print("Username: ");
        username = scanner.nextLine();
        System.out.print("\nPassword: ");
        password = scanner.nextLine();

        System.setProperty("javax.net.ssl.keyStore", "ssl/" + username);
        System.setProperty("javax.net.ssl.keyStorePassword", password);
        System.setProperty("javax.net.ssl.trustStore", "ssl/client_truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "client_truststore");

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = null;
        try {
            sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 1337);
            sslSocket.startHandshake();

            InputStream inputStream = sslSocket.getInputStream();
            OutputStream outputStream = sslSocket.getOutputStream();

            System.out.println(receive(inputStream));

            while (true) {
                System.out.print("Command: ");
                message = scanner.nextLine();
                send(message, outputStream);

                message = receive(inputStream);
                System.out.println("Server response: " + message);
            }

        } catch (IOException e) {
            System.err.println("Connection to server lost.");
        } finally {
            if (sslSocket != null) {
                try {
                    sslSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}