package com.myspring.safechannel.securityConfiguration;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import static org.apache.commons.codec.binary.Hex.decodeHex;;


@Service
class EncryptDecrypt {
	 private static final Logger logger = (Logger) LoggerFactory.getLogger(EncryptDecrypt.class);
//	
	private static SecretKeySpec secretKey;
    private static byte[] key;
//    private static final String ALGORITHM = "AES";
    static final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    
    
    public void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_16);
//            sha = MessageDigest.getInstance("SHA-1");
//            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
            
            logger.info(secretKey.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt) {
    	
    	try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, loadAESKey(),iv);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
		
//		try {
//            prepareSecreteKey("3iPFmNptVtTtZVZqZhgEoAW9R3mLaUX0");
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
//        } catch (Exception e) {
//            System.out.println("Error while encrypting: " + e.toString());
//        }
//        return null;
    }

    public String decrypt(String strToDecrypt) {
    	try {

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
//			logger.info("The key is this {}",loadAESKey().toString());
			cipher.init(Cipher.DECRYPT_MODE, loadAESKey(),iv);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
    	
    	
//        try {
//            prepareSecreteKey("3iPFmNptVtTtZVZqZhgEoAW9R3mLaUX0");
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
//        } catch (Exception e) {
//            System.out.println("Error while decrypting: " + e.toString());
//        }
//        return null;
    } 
    
    public  SecretKey loadAESKey() throws IOException { 
//		String data="3iPFmNptVtTtZVZqZhgEoAW9R3mLaUX0";
//		byte[] encoded;
//		try {
//			encoded = decodeHex(data.toCharArray());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		return new SecretKeySpec(encoded, "AES");
    	
    	
    	 MessageDigest sha = null;
         try {
        	 String myKey="3iPFmNptVtTtZVZqZhgEoAW9R3mLaUX0";
             key = myKey.getBytes(StandardCharsets.UTF_8);
             sha = MessageDigest.getInstance("SHA-1");
             key = sha.digest(key);
             key = Arrays.copyOf(key, 16);
             secretKey = new SecretKeySpec(key, ALGORITHM);
             
             logger.info("Thejhbsdhjbcbshjbdjhcbjbsh :{}",Base64.getEncoder().encodeToString(secretKey.getEncoded()));
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         }
         return secretKey;
	}
    
    
    
    
    private static final String ALGORITHM = "AES";
    private static final String MODE = "CBC";
    private static final String PADDING = "PKCS5Padding";

    private static final String ENCRYPTION_KEY = "3iPFmNptVtTtZVZqZhgEoAW9R3mLaUX0";
    private static final String IV = "1234567890123456"; // Initialization Vector (IV)

    public  String encryptString(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
        SecretKey secretKey = new SecretKeySpec(hexStringToByteArray(ENCRYPTION_KEY), ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public  String decryptString(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM + "/" + MODE + "/" + PADDING);
        SecretKey secretKey = new SecretKeySpec(hexStringToByteArray(ENCRYPTION_KEY), ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(text));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}