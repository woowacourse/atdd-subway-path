package wooteco.subway.auth.infrastructure;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHasher {
    private static final String SHA_256 = "SHA-256";
    private static final int KEY_STRETCHING_COUNT = 5;
    private static final int SALT_SIZE = 16;
    private static final SecureRandom secureRandom = new SecureRandom();

    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance(SHA_256);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String hashing(String target, String salt) {
        String appendSalt = target.concat(salt);
        byte[] digest = appendSalt.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < KEY_STRETCHING_COUNT; i++) {
            digest = messageDigest.digest(digest);
        }

        return bytesToHexString(digest);
    }

    public static String createSalt() {
        byte[] randomBytes = new byte[SALT_SIZE];
        secureRandom.nextBytes(randomBytes);
        return bytesToHexString(randomBytes);
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte a : bytes) {
            stringBuilder.append(String.format("%02x", a));
        }
        return stringBuilder.toString();
    }

}
