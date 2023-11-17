package com.myspring.safechannel.securityConfiguration;

import java.lang.reflect.Type;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

//@RestControllerAdvice
public class HttpBodyConverter extends RequestBodyAdviceAdapter {

	EncryptDecrypt encryptDecrypt;

	public HttpBodyConverter(EncryptDecrypt encryptDecrypt) {
		super();
		this.encryptDecrypt = encryptDecrypt;
	}

	private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpBodyConverter.class);

	@Override
	public boolean supports(MethodParameter methodParameter, Type type,
			Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {

		Map<String, String> input = (Map) body;

		try {
			logger.info("The Original data is{}", "Aditya");
			String encrypted = encryptDecrypt.encryptString("Aditya");
			logger.info("The Encrypted data is {}", encrypted);

			String decrypted = encryptDecrypt.decryptString(encrypted);
			logger.info("The decrypted data is {}", decrypted);

		} catch (Exception ex) {

		}

		return body; 
	}

//	@Override
//	public boolean supports(MethodParameter methodParameter, Type targetType,
//			Class<? extends HttpMessageConverter<?>> converterType) {
//		// TODO Auto-generated method stub
//		return true;
//	}
}
