import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PasswordProtector {
    protected static byte[] encryptPassword(char[] password, byte[] salt) {
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, 20000, 160);

        SecretKeyFactory keyFactory = null;
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[USER] Unable to get SecretKeyFactory instance \"PBKDF2WithHmacSHA1\".");
            System.exit(1);
        }

        try {
            return keyFactory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            System.err.println("[USER] Invalid key specification.");
            System.exit(1);
            return null;
        }
    }

    protected static byte[] generateSalt() {
        SecureRandom secureRandom = null;

        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("[USER] Unable to get SecureRandom instance \"SHA1PRNG\".");
            System.exit(1);
        }

        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);

        return salt;
    }
}