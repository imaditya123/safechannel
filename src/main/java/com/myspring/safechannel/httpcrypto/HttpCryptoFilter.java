package com.myspring.safechannel.httpcrypto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;


import jakarta.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class HttpCryptoFilter implements Filter {
	
	private final Logger logger= LoggerFactory.getLogger(HttpCryptoFilter.class);

	@Autowired
	private EncryptionService encryptionService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String requestData = null;
		String decodeData = null;
		

		ServletInputStream inputStream = null;

		if (
//				((HttpServletRequest) request).getRequestURL().toString().contains("/api/v1/books/")		||
		 ((HttpServletRequest) request).getRequestURL().toString().contains("/uploadAttachment")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("swagger")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("/csrf")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("/api/v1/upload")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("/api/v1/aws/")
				|| ((HttpServletRequest) request).getRequestURL().toString().contains("/api-docs")) {
			chain.doFilter(request, response);
			return;
		}
		



		try {

			inputStream = request.getInputStream();
			
			requestData = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			
			logger.info("request Content {}", requestData);
			if (!Strings.isEmpty(requestData)) {
				Map<String, Object> req = new ObjectMapper().readValue(requestData, HashMap.class);

				decodeData = encryptionService.decrypt(req.get("body").toString());
				logger.info("decoded req  Content {}", decodeData);
			}
			CustomHttpServletRequest requestWrapper = null;
			if (!Strings.isEmpty(decodeData)) {
				byte[] bytes = decodeData.getBytes(StandardCharsets.UTF_8);

				requestWrapper = new CustomHttpServletRequest((HttpServletRequest) request, bytes);
			}

			CustomHttpServletResponse responseWrapper = new CustomHttpServletResponse(
					(HttpServletResponse) response);

			if (requestWrapper != null)
				chain.doFilter(requestWrapper, responseWrapper);
			else
				chain.doFilter(request, responseWrapper);

			String responseContent = new String(responseWrapper.getDataStream());
			logger.info("responseContent {}", responseContent);

			if (responseContent.endsWith("]") || responseContent.endsWith("}")) {
				String encrypt = encryptionService.encrypt(responseContent);
				String decrypt = encryptionService.decrypt(encrypt);
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