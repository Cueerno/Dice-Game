package task_3.config;

import java.security.SecureRandom;

public class SecureKeyGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static int generateSecureRandomInt(int range) {
        return secureRandom.nextInt(range);
    }

    public static byte[] generate256BitKey() {
        byte[] key = new byte[32]; // 256 bits
        secureRandom.nextBytes(key);
        return key;
    }
}