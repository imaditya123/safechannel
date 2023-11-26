package com.myspring.safechannel.httpcrypto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.apache.commons.codec.binary.Hex;

import static org.apache.commons.codec.binary.Hex.decodeHex;;

@Service
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class EncryptionService {
	private final Environment environment;

	private static  Logger logger = (Logger) LoggerFactory.getLogger(EncryptionService.class);

	static  IvParameterSpec iv = new IvParameterSpec(new byte[16]);

	

	public String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, loadAESKey(), iv);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			logger.info("Error while encrypting: " + e.toString());
		}
		return null;
	}

	public String decrypt(String strToDecrypt) {
		try {
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, loadAESKey(), iv);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			logger.info("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public SecretKey loadAESKey() throws IOException {
		String data =environment.getProperty("application.security.http.encryption-key");
		data = Hex.encodeHexString(data.getBytes());

		byte[] encoded;
		try {
			encoded = decodeHex(data.toCharArray());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new SecretKeySpec(encoded, "AES");
	}

}
