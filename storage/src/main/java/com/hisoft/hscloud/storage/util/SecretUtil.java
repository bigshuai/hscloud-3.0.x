package com.hisoft.hscloud.storage.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SecretUtil {
    public static String createSign(String timeStr, String container, String name, String macKey, String auth) {
        String methodStr = "GET\n";
       // container = "201305";
        StringBuilder macData = new StringBuilder();
        macData.append(methodStr).append(timeStr + "\n/v1/AUTH_").append(auth)
                .append("/").append(container).append("/").append(name);

        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            byte[] secretByte = macKey.getBytes("UTF-8");
            byte[] dataBytes = macData.toString().getBytes("UTF-8");
            SecretKey secret = new SecretKeySpec(secretByte, "HmacSHA1");

            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = new Hex().encode(doFinal);
            String checksum = new String(hexB);
            return checksum;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
