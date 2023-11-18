package com.myspring.safechannel.securityConfiguration;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;


import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.myspring.safechannel.securityConfiguration.utils.HttpServletRequestWritableWrapper;
import com.myspring.safechannel.securityConfiguration.utils.HttpServletResponseWritableWrapper;
import com.myspring.safechannel.securityConfiguration.utils.ResponseJSON;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpRequestsInterceptor implements Filter {
	
	private final Logger logger= LoggerFactory.getLogger(HttpRequestsInterceptor.class);

	@Autowired
	private EncryptDecrypt payloadEncryptDecrypt;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String requestData = null;
		String decodeData = null;

		ServletInputStream inputStream = null;

		if (((HttpServletRequest) request).getRequestURL().toString().contains("/updatePicture")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("/uploadAttachment")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("swagger")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("/csrf")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("/api-docs")) {
			chain.doFilter(request, response);
			return;
		}
		
//		Uncomment this for testing for not encrypted data
//		chain.doFilter(request, response);
//		return;


		try {

			inputStream = request.getInputStream();
			requestData = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//			log.info("reques data {}", requestData);
			if (!Strings.isEmpty(requestData)) {
				decodeData = payloadEncryptDecrypt.decrypt(requestData);
			}
			HttpServletRequestWritableWrapper requestWrapper = null;
			if (!Strings.isEmpty(decodeData)) {
//				log.info("decode data " + decodeData);
				byte[] bytes = decodeData.getBytes(StandardCharsets.UTF_8);

				requestWrapper = new HttpServletRequestWritableWrapper((HttpServletRequest) request, bytes);
			}

			HttpServletResponseWritableWrapper responseWrapper = new HttpServletResponseWritableWrapper(
					(HttpServletResponse) response);

			if (requestWrapper != null)
				chain.doFilter(requestWrapper, responseWrapper);
			else
				chain.doFilter(request, responseWrapper);

			String responseContent = new String(responseWrapper.getDataStream());
			logger.info("responseContent {}", responseContent);

			if (responseContent.endsWith("]") || responseContent.endsWith("}")) {
				String encrypt = payloadEncryptDecrypt.encrypt(responseContent);
				String decrypt = payloadEncryptDecrypt.decrypt(encrypt);
				logger.info("encrypted response", encrypt);
				logger.info("Decrypted response", decrypt);
				ResponseJSON ouput = new ResponseJSON(encrypt);
				ObjectMapper myObjectMapper = new ObjectMapper();
				myObjectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
				ObjectWriter ow = myObjectMapper.writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(ouput);
				logger.info("final json {}", json);

				response.setContentLength(json.length());
				response.getOutputStream().write(json.getBytes());
			} else {
				response.setContentLength(responseContent.length());
				response.getOutputStream().write(responseContent.getBytes());
			}

			response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");

		} catch (Exception exc) {
			logger.error("exception at dofilter {}", exc);
		}

	}

	@Override
	public void destroy() {
	}

}
