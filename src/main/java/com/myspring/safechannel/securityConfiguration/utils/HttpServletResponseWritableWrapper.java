package com.myspring.safechannel.securityConfiguration.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;



import org.springframework.http.HttpStatus;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class HttpServletResponseWritableWrapper extends HttpServletResponseWrapper {

	ByteArrayOutputStream output;
	FilterServletOutputStream filterOutput;
	HttpStatus status = HttpStatus.OK;

	public HttpServletResponseWritableWrapper(HttpServletResponse response) {
		super(response);
		output = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (filterOutput == null) {
			filterOutput = new FilterServletOutputStream(output);
		}
		return filterOutput;
	}

	public byte[] getDataStream() {
		return output.toByteArray();
	}

}

