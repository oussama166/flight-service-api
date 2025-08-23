package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Config.RSAkeyProps;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Component
public class EncryptInfoUtils {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public EncryptInfoUtils(RSAkeyProps rsaKeyProps) {
        this.publicKey = rsaKeyProps.publicKey();
        this.privateKey = rsaKeyProps.privateKey();
    }

    public String encrypt(String data){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));

        } catch (Exception e) {
            return null;
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
