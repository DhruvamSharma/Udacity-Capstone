package com.onearticleoneweek.wahadatkashmiri.roomlib.database.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashingFunction {


    public static String hash(String dataToHash) {

        String hashed = dataToHash;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                byte[] hash = messageDigest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
                //hashed = HttpRequest.Base64.encodeBytes(hash);
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashed;

    }


}
