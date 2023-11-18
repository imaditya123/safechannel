package com.myspring.safechannel.securityConfiguration;



import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import static org.apache.commons.codec.binary.Hex.decodeHex;;


@Service
class EncryptDecrypt {
	 private static final Logger logger = (Logger) LoggerFactory.getLogger(EncryptDecrypt.class);
	 
		static final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
		
	 private EncryptDecrypt(){
		}



		public String encrypt(String strToEncrypt) {
			try {
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, loadAESKey(),iv);
				return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
			} catch (Exception e) {
				logger.info("Error while encrypting: " + e.toString());
			}
			return null;
		}

		public  String decrypt(String strToDecrypt) {
			try {

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, loadAESKey(),iv);
				return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
			} catch (Exception e) {
				logger.info("Error while decrypting: " + e.toString());
			}
			return null;
		}
		
		public  SecretKey loadAESKey() throws IOException {
			String data="ghWPhdH59zj5UiTrU8lgGRb2FLliJzgR";
			data=Hex.encodeHexString(data.getBytes());
			logger.info("Keys is {}",data);
			logger.info("Keys length is {}",data.length());
		
			byte[] encoded;
			try {
				encoded = decodeHex(data.toCharArray());
//				encoded=new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return new SecretKeySpec(encoded, "AES");
		}
	 
	

}