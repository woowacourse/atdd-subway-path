package wooteco.subway.learningtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageDigestTest {

    public static final String SHA_256 = "SHA-256";
    public static final int KEY_STRETCHING_COUNT = 5;

    @DisplayName("MessageDigest를 통해 비밀번호 암호화를 한다")
    @ParameterizedTest
    @ValueSource(strings = {"impassword", "password486", "123456", "ContainCapitalAnd1234!@#$"})
    void messageDigestTest(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
        byte[] firstDigest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        byte[] secondDigest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        String firstHashingPassword = Arrays.toString(firstDigest);
        String secondHashingPassword = Arrays.toString(secondDigest);

        System.out.println("first : " + firstHashingPassword);
        System.out.println("second : " + secondHashingPassword);
        System.out.println("= = = = =\n");

        assertThat(firstHashingPassword).isEqualTo(secondHashingPassword);
    }

    @DisplayName("Key Stretching 기법을 통해 n번 해슁을 한다")
    @Test
    void keyStretching() throws NoSuchAlgorithmException {
        String password = "I)(Think1This2Very3Hard4Password_!@#";
        MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
        byte[] firstDigest = null;
        byte[] secondDigest = null;

        for (int i = 0; i < KEY_STRETCHING_COUNT; i++) {
            firstDigest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            secondDigest = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        }

        String firstHashingPassword = Arrays.toString(firstDigest);
        String secondHashingPassword = Arrays.toString(firstDigest);

        assertThat(firstHashingPassword).isEqualTo(secondHashingPassword);
    }
}
