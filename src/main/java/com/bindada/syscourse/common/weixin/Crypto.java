package com.bindada.syscourse.common.weixin;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Crypto {
    public static boolean verifySignature(String signature, String timestamp, String nonce, String token) {
        // 此处应参考 https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
        if (signature.isEmpty() || timestamp.isEmpty() || nonce.isEmpty() || token.isEmpty()) {
            return false;
        }

        String[] temp = {token, timestamp, nonce};
        Arrays.sort(temp);
        String plainText = String.join("", temp);

        String calculatedSignature = "";
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] cipherBytes = sha1.digest(plainText.getBytes(StandardCharsets.UTF_8));
            calculatedSignature = Hex.encodeHexString(cipherBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return calculatedSignature.equals(signature);
    }
}
