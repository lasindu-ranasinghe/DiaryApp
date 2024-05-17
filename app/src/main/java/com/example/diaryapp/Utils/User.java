package com.example.diaryapp.Utils;

import android.util.Base64;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class User {

    // Change this key to your own secret key
    private static final String SECRET_KEY = "DiaryAppKey";

    public String encryptPassword(String password) {
        try {
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
