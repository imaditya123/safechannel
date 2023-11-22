package com.myspring.safechannel.httpcrypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.HttpStatus;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;



public class CustomHttpServletResponse  extends HttpServletResponseWrapper {

	ByteArrayOutputStream output;
	CustomServletOutputStream filterOutput;
	HttpStatus status = HttpStatus.OK;

	public CustomHttpServletResponse(HttpServletResponse response) {
		super(response);
		output = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (filterOutput == null) {
			filterOutput = new CustomServletOutputStream(output);
		}
		return filterOutput;
	}

	public byte[] getDataStream() {
		return output.toByteArray();
	}

}

