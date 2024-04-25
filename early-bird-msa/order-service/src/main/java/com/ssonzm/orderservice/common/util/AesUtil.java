package com.ssonzm.orderservice.common.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AesUtil {
    private static final int IV_LENGTH = 16;

    private String secretKey; // 32byte

    private String iv = "";

    private static final String AES_METHOD = "AES/CBC/PKCS5Padding";

    private static final String CIPHER_FINAL_ENCODING = "UTF-8";

    private static final String ENCODING_METHOD = "AES";

    private final Environment env;
    public AesUtil(Environment env) {
        this.env = env;
        secretKey = env.getProperty("aes.secret");
    }

    public String encodeUnique(String targetEncodeString) {
        try {
            iv = secretKey.substring(0, IV_LENGTH);

            byte[] keyData = secretKey.getBytes();

            SecretKey secureKey = new SecretKeySpec(keyData, ENCODING_METHOD);

            Cipher c = Cipher.getInstance(AES_METHOD);
            c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));

            byte[] encrypted = c.doFinal(targetEncodeString.getBytes(CIPHER_FINAL_ENCODING));
            String enStr = new String(Base64.getEncoder().encode(encrypted));

            return enStr;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decodeUnique(String targetDecodeString) {
        try {
            iv = secretKey.substring(0, IV_LENGTH);

            byte[] keyData = secretKey.getBytes();
            SecretKey secureKey = new SecretKeySpec(keyData, ENCODING_METHOD);
            Cipher c = Cipher.getInstance(AES_METHOD);
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes(CIPHER_FINAL_ENCODING)));

            byte[] byteStr = Base64.getDecoder().decode(targetDecodeString.getBytes());

            return new String(c.doFinal(byteStr), CIPHER_FINAL_ENCODING);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
