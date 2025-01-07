package ru.ecofin.service.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptionUtils {

  private EncryptionUtils() {
    throw new IllegalStateException("Utility class");
  }

  private static final String ALGORITHM = "AES";
  private static final String SECRET_KEY = "d1A23d5F923r9dL3H42";

  private static SecretKeySpec generateKey() throws Exception {
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
    keyBytes = sha.digest(keyBytes);
    return new SecretKeySpec(keyBytes, ALGORITHM);
  }

  public static String encrypt(String data) throws Exception {
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    SecretKeySpec keySpec = generateKey();
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    return Base64.getEncoder()
        .encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
  }

  public static String decrypt(String encryptedData) throws Exception {
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    SecretKeySpec keySpec = generateKey();
    cipher.init(Cipher.DECRYPT_MODE, keySpec);
    return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)),
        StandardCharsets.UTF_8);
  }
}
