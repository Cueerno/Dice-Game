package task_3.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacCalculator {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    public static String computeHMAC(byte[] key, int message) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(key, HMAC_ALGORITHM);
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(String.valueOf(message).getBytes());
            return bytesToHex(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC", e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) result.append('0');
            result.append(hex);
        }
        return result.toString().toUpperCase();
    }
}