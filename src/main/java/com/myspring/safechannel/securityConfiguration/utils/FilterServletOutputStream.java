package com.myspring.safechannel.securityConfiguration.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

public class FilterServletOutputStream extends ServletOutputStream {

    private DataOutputStream stream;

    public FilterServletOutputStream(ByteArrayOutputStream output) {
        this.stream = new DataOutputStream(output);
    }

    @Override
    public void write(int b) throws IOException {
        stream.write(b);
    }

    // Override other methods of ServletOutputStream as needed

    @Override
    public boolean isReady() {
        // Implementation as needed
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        // Implementation as needed
    }
}
