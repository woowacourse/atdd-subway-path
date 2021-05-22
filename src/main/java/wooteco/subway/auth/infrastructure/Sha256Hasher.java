package wooteco.subway.auth.infrastructure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Hasher {

    private static final String SHA_256 = "SHA-256";
    private static final int KEY_STRETCHING_COUNT = 10;

    public String hashing(String password) {
        MessageDigest md = getMessageDigest(SHA_256);
        byte[] digest = hashAndKeyStretching(password, md);

        return byteToString(digest);
    }

    private MessageDigest getMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(String.format("존재하지 않는 알고리즘:[%s]", algorithm));
        }
    }

    private byte[] hashAndKeyStretching(String password, MessageDigest md) {
        byte[] digest = password.getBytes();
        for (int i = 0; i < KEY_STRETCHING_COUNT; i++) {
            md.update(digest);
            digest = md.digest();
        }
        return digest;
    }

    private String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte));
        }
        return sb.toString();
    }
}
