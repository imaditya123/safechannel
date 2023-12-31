package com.myspring.safechannel.httpcrypto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
//import javax.servlet.ServletInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;



public class CustomHttpServletRequest extends HttpServletRequestWrapper{
	
	   private static final Logger logger =  LoggerFactory.getLogger(CustomHttpServletRequest.class);

	private final ByteArrayInputStream decryptedDataBAIS;

	public CustomHttpServletRequest(HttpServletRequest request, byte[] decryptedData) {
		super(request);
		decryptedDataBAIS = new ByteArrayInputStream(decryptedData);
	}

	@Override
	public String getHeader(String headerName) {
		String headerValue = super.getHeader(headerName);
		if ("Accept".equalsIgnoreCase(headerName) || "Content-Type".equalsIgnoreCase(headerName)) {
			return headerValue.replaceAll(MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE);
		} 
		logger.info("Header name {}",headerName);
		logger.info("header value {}",headerValue);
		return headerValue;
	}

	
	@Override
	public Enumeration<String> getHeaders(String name) {
		return super.getHeaders(name);
	}

	@Override
	public String getContentType() {
		String contentTypeValue = super.getContentType();
		if (MediaType.TEXT_PLAIN_VALUE.equalsIgnoreCase(contentTypeValue)) {
			return MediaType.APPLICATION_JSON_VALUE;
		}
		logger.info("content type {}", contentTypeValue);
		return contentTypeValue;
	}

	@Override
	public BufferedReader getReader() throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(decryptedDataBAIS, "UTF_8"));
	}
	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {
			@Override
			public int read() {
				return decryptedDataBAIS.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				// TODO Auto-generated method stub

			}
		};
	}


}
