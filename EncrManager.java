import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;

public class EncrManager {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;

    public static void encryptCsv(String password) throws Exception {
        try {
            // Generate random salt
            byte[] salt = new byte[8];
            SecureRandom random = SecureRandom.getInstanceStrong(); // Strong random number generator
            random.nextBytes(salt);

            // Derive key from password using PBKDF2 with the salt
            SecretKeySpec keySpec = generateKey(password, salt);

            // Encryption steps
            String inputCsvPath = ConfigManager.getFilePath();
            byte[] plaintext = Files.readAllBytes(Paths.get(inputCsvPath));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedData = cipher.doFinal(plaintext);

            // Combine salt and encrypted data for storage
            byte[] combinedData = new byte[salt.length + encryptedData.length];
            System.arraycopy(salt, 0, combinedData, 0, salt.length);
            System.arraycopy(encryptedData, 0, combinedData, salt.length, encryptedData.length);

            // Write combined data (salt + encrypted data) to the CSV file
            Files.write(Paths.get(inputCsvPath), combinedData, StandardOpenOption.WRITE);
            System.out.println("CSV file encrypted successfully!");
        } catch (Exception e) {
            System.err.println("Error encrypting CSV file: " + e.getMessage());
            throw e; // Re-throw the exception for further handling
        }
    }

    public static void decryptCsv(String password) throws Exception {
        String encryptedCsvPath = ConfigManager.getFilePath();
        byte[] combinedData = Files.readAllBytes(Paths.get(encryptedCsvPath));

        // Extract salt from the beginning of the file
        byte[] salt = new byte[8];
        System.arraycopy(combinedData, 0, salt, 0, salt.length);

        // Extract encrypted data after the salt
        byte[] encryptedData = new byte[combinedData.length - salt.length];
        System.arraycopy(combinedData, salt.length, encryptedData, 0, encryptedData.length);

        // Derive key using password and the extracted salt
        SecretKeySpec keySpec = generateKey(password, salt);

        // Decryption steps
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // Write decrypted data back to the file
        Files.write(Paths.get(encryptedCsvPath), decryptedData, StandardOpenOption.WRITE);
        System.out.println("CSV file decrypted successfully!");
    }

    private static SecretKeySpec generateKey(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }
}
